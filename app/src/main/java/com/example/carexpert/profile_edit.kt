package com.example.carexpert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore

class profile_edit : AppCompatActivity() {
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

        // get user data
        db.collection("tbUser").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data.get("username").toString() == "jovian6") {
                        _edit_FullName.setText(document.data.get("nama").toString())
                        _edit_Email.setText(document.data.get("email").toString())
                        _edit_Gender.setText(document.data.get("gender").toString())
                        _edit_Password.setText(document.data.get("password").toString())
                    }
                }
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }

        val _btnProfile = findViewById<ConstraintLayout>(R.id.btnProfile)
        _btnProfile.setOnClickListener {
            // intent here ...
        }

        val _btnBMI = findViewById<ConstraintLayout>(R.id.btnBMI)
        _btnBMI.setOnClickListener {
            // intent here ...
        }

        val _btnChat = findViewById<ConstraintLayout>(R.id.btnChat)
        _btnChat.setOnClickListener {
            // intent here ...
        }

        val _btnUpdateUserData = findViewById<Button>(R.id.btnUpdateUserData)
        _btnUpdateUserData.setOnClickListener {
            if (TextUtils.isEmpty(_edit_FullName.getText()) || TextUtils.isEmpty(_edit_Email.getText()) || TextUtils.isEmpty(_edit_Password.getText())) {
                setTextInputError(_edit_FullName, _fullNameLayout, "Full Name")
                setTextInputError(_edit_Email, _emailLayout, "E-mail Address")
                setTextInputError(_edit_Password, _passwordLayout, "Password")
            } else {
                val userObj = user("tes", _edit_Email.text.toString(), _edit_Gender.text.toString(), _edit_FullName.text.toString(), _edit_Password.text.toString(), "jovian6")

                // update user data
                db.collection("tbUser").document(userObj.username)
                    .set(userObj)
                    .addOnSuccessListener {
                        Log.d("Firebase", "Successfully updated user data!")
                    }
                    .addOnFailureListener {
                        Log.d("Firebase", it.message.toString())
                    }
            }
        }
    }

    fun setTextInputError(_textInput: TextInputEditText, _textInputLayout: TextInputLayout, attr: String) {
        if (TextUtils.isEmpty(_textInput.getText())) {
            _textInputLayout.error = "${attr} cannot be empty"
        }
    }
}