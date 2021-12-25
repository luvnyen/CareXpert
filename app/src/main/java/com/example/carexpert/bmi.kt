package com.example.carexpert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.bold
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.math.RoundingMode
import java.text.DecimalFormat

class bmi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

        val _btnProfile = findViewById<ConstraintLayout>(R.id.btnProfile)
        _btnProfile.setOnClickListener {
            startActivity(Intent(this@bmi, profile_edit::class.java))
        }

        val _btnChat = findViewById<ConstraintLayout>(R.id.btnChat)
        _btnChat.setOnClickListener {
            startActivity(Intent(this@bmi, chat::class.java))
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
            if (TextUtils.isEmpty(_edit_Height.getText()) || TextUtils.isEmpty(_edit_Weight.getText())) {
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

                var category = "undefined"
                if (bmi < 18.5) {
                    category = "Underweight"
                } else if (bmi >= 18.5 && bmi <= 24.9) {
                    category = "Normal"
                } else if (bmi >= 25.0 && bmi <= 29.9) {
                    category = "Overweight"
                } else {
                    category = "Obese"
                }

                val df = DecimalFormat("#.#")
                df.roundingMode = RoundingMode.HALF_EVEN

                val formattedBMI = SpannableStringBuilder()
                                    .append("BMI: ")
                                    .bold { append(df.format(bmi)) }

                _tvBMI.setVisibility(View.VISIBLE)
                _tvBMI.setText(formattedBMI)

                val formattedCategory = SpannableStringBuilder()
                                        .append("Category: ")
                                        .bold { append(category) }

                _tvCategory.setVisibility(View.VISIBLE)
                _tvCategory.setText(formattedCategory)

                val formattedWeightRange = SpannableStringBuilder()
                                            .append("For your height, a normal weight range would be from ")
                                            .bold { append(df.format(normalWeightRangeStart)) }
                                            .append(" to ")
                                            .bold { append(df.format(normalWeightRangeEnd)) }
                                            .append(" kilograms.")

                _tvWeightRange.setVisibility(View.VISIBLE)
                _tvWeightRange.setText(formattedWeightRange)
            }
        }
    }
}