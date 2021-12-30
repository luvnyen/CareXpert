package com.example.carexpert.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.bold
import com.example.carexpert.R
import com.example.carexpert.setTextInputEmptyError
import com.example.carexpert.username_global
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.math.RoundingMode
import java.text.DecimalFormat

class BMIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        val _exploreIcon = findViewById<ConstraintLayout>(R.id.exploreIcon)
        _exploreIcon.setOnClickListener {
            startActivity(Intent(this@BMIActivity, ExploreActivity::class.java))
        }

        val _homeIcon = findViewById<ConstraintLayout>(R.id.profileIcon)
        _homeIcon.setOnClickListener {
            startActivity(Intent(this@BMIActivity, HomeActivity::class.java))
        }

        // get user data
        db.collection("tbUser").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data["username"].toString() == username_global) {
                        val _tvProfileName = findViewById<TextView>(R.id.tvProfileName)
                        _tvProfileName.text = document.data["nama"].toString()
                    }
                }
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }

        val _btnProfile = findViewById<ConstraintLayout>(R.id.btnProfile)
        _btnProfile.setOnClickListener {
            startActivity(Intent(this@BMIActivity, ProfileEditActivity::class.java))
        }

        val _btnPosts = findViewById<ConstraintLayout>(R.id.btnChat)
        _btnPosts.setOnClickListener {
            startActivity(Intent(this@BMIActivity, ProfilePostsActivity::class.java))
        }

        val _heightLayout = findViewById<TextInputLayout>(R.id.heightLayout)
        val _edit_Height = findViewById<TextInputEditText>(R.id.edit_Height)

        val _weightLayout = findViewById<TextInputLayout>(R.id.weightLayout)
        val _edit_Weight = findViewById<TextInputEditText>(R.id.edit_Weight)

        val _tvBMI = findViewById<TextView>(R.id.tvBMI)
        val _tvCategory = findViewById<TextView>(R.id.tvCategory)
        val _tvWeightRange = findViewById<TextView>(R.id.tvWeightRange)

        val _btnCalculateBMI = findViewById<Button>(R.id.btnCalculateBMI)
        _btnCalculateBMI.setOnClickListener {
            if (TextUtils.isEmpty(_edit_Height.text) || TextUtils.isEmpty(_edit_Weight.text)) {
                setTextInputEmptyError(_edit_Height, _heightLayout, "Height")
                setTextInputEmptyError(_edit_Weight, _weightLayout, "Weight")
            } else if (_edit_Height.text.toString().toDouble() <= 0 || _edit_Weight.text.toString().toDouble() <= 0) {
                if (_edit_Height.text.toString().toDouble() <= 0) {
                    _heightLayout.error = "Height must be more than zero"
                }
                if (_edit_Weight.text.toString().toDouble() <= 0) {
                    _weightLayout.error = "Weight must be more than zero"
                }
            } else {
                val weightDouble = _edit_Weight.text.toString().toDouble()
                val heightDoubleMeter = _edit_Height.text.toString().toDouble() / 100
                val bmi = weightDouble / (heightDoubleMeter * heightDoubleMeter)
                val normalWeightRangeStart = 18.5 * (heightDoubleMeter * heightDoubleMeter)
                val normalWeightRangeEnd = 24.9 * (heightDoubleMeter * heightDoubleMeter)

                val category = when {
                    bmi < 18.5 -> {
                        "Underweight"
                    }
                    bmi in 18.5..24.9 -> {
                        "Normal"
                    }
                    bmi in 25.0..29.9 -> {
                        "Overweight"
                    }
                    else -> {
                        "Obese"
                    }
                }

                val df = DecimalFormat("#.#")
                df.roundingMode = RoundingMode.HALF_EVEN

                val formattedBMI = SpannableStringBuilder()
                                    .append("BMI: ")
                                    .bold { append(df.format(bmi)) }

                _tvBMI.visibility = View.VISIBLE
                _tvBMI.text = formattedBMI

                val formattedCategory = SpannableStringBuilder()
                                        .append("Category: ")
                                        .bold { append(category) }

                _tvCategory.visibility = View.VISIBLE
                _tvCategory.text = formattedCategory

                val formattedWeightRange = SpannableStringBuilder()
                                            .append("For your height, a normal weight range would be from ")
                                            .bold { append(df.format(normalWeightRangeStart)) }
                                            .append(" to ")
                                            .bold { append(df.format(normalWeightRangeEnd)) }
                                            .append(" kilograms.")

                _tvWeightRange.visibility = View.VISIBLE
                _tvWeightRange.text = formattedWeightRange
            }
        }

        val sp : SharedPreferences = getSharedPreferences("usernameSP", Context.MODE_PRIVATE)
        val _tvSignOut : TextView = findViewById(R.id.tvSignOut)
        _tvSignOut.setOnClickListener {
            username_global = ""
            val editor = sp.edit()
            editor.putString("spUsername", username_global)
            editor.apply()

            startActivity(Intent(this@BMIActivity, SignInActivity::class.java))
        }
    }
}