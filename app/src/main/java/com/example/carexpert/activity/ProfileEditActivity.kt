package com.example.carexpert.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.carexpert.R
import com.example.carexpert.setTextInputEmptyError
import com.example.carexpert.model.User
import com.example.carexpert.setOtherUsername
import com.example.carexpert.username_global
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ProfileEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        val _fullNameLayout = findViewById<TextInputLayout>(R.id.fullNameLayout)
        val _edit_FullName = findViewById<TextInputEditText>(R.id.edit_FullName)

        val _emailLayout = findViewById<TextInputLayout>(R.id.emailLayout)
        val _edit_Email = findViewById<TextInputEditText>(R.id.edit_Email)

        val _edit_Gender = findViewById<AutoCompleteTextView>(R.id.edit_Gender)

        val _passwordLayout = findViewById<TextInputLayout>(R.id.passwordLayout)
        val _edit_Password = findViewById<TextInputEditText>(R.id.edit_Password)

        val _tvProfileName = findViewById<TextView>(R.id.tvProfileName)

        // dropdown menu list of items
        val items = listOf("Pria", "Wanita")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            items
        )
        _edit_Gender.setAdapter(adapter)

        // get user data
        db.collection("tbUser").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data["username"].toString() == username_global) {
                        _edit_FullName.setText(document.data["nama"].toString())
                        _edit_Email.setText(document.data["email"].toString())
                        if (document.data["gender"].toString() == "Pria") _edit_Gender.setText(items[0], false) else _edit_Gender.setText(items[1], false)
                        _edit_Password.setText(document.data["password"].toString())
                        _tvProfileName.text = document.data["nama"].toString()
                    }
                }
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }

        val _btnBMI = findViewById<ConstraintLayout>(R.id.btnBMI)
        _btnBMI.setOnClickListener {
            startActivity(Intent(this@ProfileEditActivity, BMIActivity::class.java))
        }

        val _btnChat = findViewById<ConstraintLayout>(R.id.btnChat)
        _btnChat.setOnClickListener {
            startActivity(Intent(this@ProfileEditActivity, ChatActivity::class.java))
        }

        val _btnUpdateUserData = findViewById<Button>(R.id.btnUpdateUserData)
        _btnUpdateUserData.setOnClickListener {
            if (TextUtils.isEmpty(_edit_FullName.text) || TextUtils.isEmpty(_edit_Email.text) || TextUtils.isEmpty(_edit_Password.text)) {
                setTextInputEmptyError(_edit_FullName, _fullNameLayout, "Full Name")
                setTextInputEmptyError(_edit_Email, _emailLayout, "E-mail Address")
                setTextInputEmptyError(_edit_Password, _passwordLayout, "Password")
            } else {
                db.collection("tbUser")
                    .get()
                    .addOnSuccessListener { result ->
                        var foundEmailAddressDouble = false
                        for (document in result) {
                            if (document.data["email"].toString() == _edit_Email.text.toString() && document.data["username"].toString() != username_global) {
                                foundEmailAddressDouble = true
                                _emailLayout.error = "E-mail Address has already been taken"
                            }
                        }

                        if (!foundEmailAddressDouble) {
                            _emailLayout.error = ""

                            // get local time
                            val date = Calendar.getInstance().time
                            val formatter = SimpleDateFormat.getDateTimeInstance()
                            val formatedDate = formatter.format(date)

                            val userObj = User(formatedDate, _edit_Email.text.toString(), _edit_Gender.text.toString(), _edit_FullName.text.toString(), _edit_Password.text.toString(), username_global)

                            // update user data
                            db.collection("tbUser").document(userObj.username)
                                .set(userObj)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Successfully updated biodata!", Toast.LENGTH_LONG).show()
                                }
                                .addOnFailureListener {
                                    Log.d("Firebase", it.message.toString())
                                }
                        }
                    }
                    .addOnFailureListener{
                        Log.d("Firebase", it.message.toString())
                    }
            }
        }
    }
}