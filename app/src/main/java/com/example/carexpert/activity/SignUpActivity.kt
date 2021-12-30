package com.example.carexpert.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.example.carexpert.*
import com.example.carexpert.model.User
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SignUpActivity : AppCompatActivity() {
    private var dataNama : ArrayList<User> = ArrayList()

    private lateinit var _nama : TextInputEditText
    private lateinit var _gender : AutoCompleteTextView
    private lateinit var _email : TextInputEditText
    private lateinit var _password : TextInputEditText
    private lateinit var _confirmPassword : TextInputEditText
    private lateinit var _username : TextInputEditText
    private lateinit var _FailedSuccess : TextView
    private lateinit var _fromDate : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        this.supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        // pindah ke halaman TnC
        val _TnCText = findViewById<TextView>(R.id.TnCText)
        _TnCText.setOnClickListener{
            val eIntent = Intent(this@SignUpActivity, TnCActivity::class.java)
            startActivity(eIntent)
        }

        // pindah ke halaman SignIn
        val _ImageView6 = findViewById<ImageView>(R.id.imageView6)
        _ImageView6.setOnClickListener{
            val eIntent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(eIntent)
        }

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        _nama = findViewById(R.id.namalengkap)
        _email = findViewById(R.id.email)
        _password = findViewById(R.id.Password)
        _confirmPassword = findViewById(R.id.ConfirmPassword)
        _gender = findViewById(R.id.gender)
        _username= findViewById(R.id.username)

        val usernameLayout = findViewById<TextInputLayout>(R.id.usernameLayout)
        val namalengkapLayout = findViewById<TextInputLayout>(R.id.namalengkapLayout)
        val emailLayout = findViewById<TextInputLayout>(R.id.emailLayout)
        val genderLayout = findViewById<TextInputLayout>(R.id.genderLayout)
        val passwordLayout = findViewById<TextInputLayout>(R.id.passwordLayout)
        val confirmPasswordLayout = findViewById<TextInputLayout>(R.id.confirmPasswordLayout)

        // dropdown menu list of items
        val items = listOf("Pria", "Wanita")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            items
        )
        _gender.setAdapter(adapter)

        //Button Create Account
        val _SignUpButton = findViewById<TextView>(R.id.SignUpButton)
        _SignUpButton.setOnClickListener {
            setTextInputEmptyError(_username, usernameLayout, "Username")
            setTextInputEmptyError(_password, passwordLayout, "Password")
            setTextInputEmptyError(_confirmPassword, confirmPasswordLayout, "Confirm Password")
            setAutoCompleteTextViewEmptyError(_gender, genderLayout, "Gender")
            setTextInputEmptyError(_nama, namalengkapLayout, "Full Name")
            setTextInputEmptyError(_email, emailLayout, "E-mail Address")

            var isPasswordDouble = false

            if (_password.text.toString() != _confirmPassword.text.toString() && !TextUtils.isEmpty(_password.text) && !TextUtils.isEmpty(_confirmPassword.text)) {
                isPasswordDouble = true
                passwordLayout.error = "Password did not match"
                confirmPasswordLayout.error = "Password did not match"
            }

            var foundUsernameDouble = false
            var foundEmailAddressDouble = false

            db.collection("tbUser")
                .get()
                .addOnSuccessListener { result ->
                    dataNama.clear()
                    for (document in result) {
                        if (document.data["email"].toString() == _email.text.toString() || document.data["username"].toString() == _username.text.toString()){
                            if (!TextUtils.isEmpty(_email.text)) {
                                if (document.data["email"].toString() == _email.text.toString()) {
                                    foundEmailAddressDouble = true
                                    emailLayout.error = "E-mail Address has already been taken"
                                }
                            }

                            if (!TextUtils.isEmpty(_username.text)) {
                                if (document.data["username"].toString() == _username.text.toString()) {
                                    foundUsernameDouble = true
                                    usernameLayout.error = "Username has already been taken"
                                }
                            }
                        }
                    }

                    if (!TextUtils.isEmpty(_username.text)) {
                        if (!foundUsernameDouble) {
                            usernameLayout.error = ""
                        }
                        if (_username.text.toString().contains(" ")) {
                            usernameLayout.error = "Username cannot contain spaces"
                        } else {
                            usernameLayout.error = ""
                        }
                    }

                    if (!TextUtils.isEmpty(_password.text)) {
                        if (_password.text.toString().length < 6) {
                            passwordLayout.error = "Password should be minimum of 6 characters"
                        }

                        if (_password.text.toString().length >= 6 && !isPasswordDouble) {
                            passwordLayout.error = ""
                        }
                    }


                    if (!foundEmailAddressDouble && !TextUtils.isEmpty(_email.text)) {
                        emailLayout.error = ""
                    }

                    if (!TextUtils.isEmpty(_username.text) && !TextUtils.isEmpty(_password.text) && !TextUtils.isEmpty(_confirmPassword.text) && !TextUtils.isEmpty(_gender.text) && !TextUtils.isEmpty(_nama.text) && !TextUtils.isEmpty(_email.text) && _password.text.toString().length >= 6) {
                        if (!foundEmailAddressDouble && !foundUsernameDouble && !isPasswordDouble) {
                            val date = Calendar.getInstance().time
                            val formatter = SimpleDateFormat.getDateInstance()
                            val formatedDate = formatter.format(date)

                            TambahData(db, _nama.text.toString(), _username.text.toString(), _gender.text.toString(), formatedDate, _email.text.toString(), _password.text.toString())
                        }
                    }
                }
                .addOnFailureListener{
                    Log.d("Firebase", it.message.toString())
                }
        }
    }

    private fun TambahData(db: FirebaseFirestore, nama: String, username: String, gender: String, date: String, email: String, password: String){
        val namaBaru = User(date, email, gender, nama, password, username)
        db.collection("tbUser").document(username)
            .set(namaBaru)
            .addOnSuccessListener {
                setUsername(username)

                val sp : SharedPreferences = getSharedPreferences("usernameSP", Context.MODE_PRIVATE)
                val editor = sp.edit()
                editor.putString("spUsername", username_global)
                editor.apply()

                val eIntent = Intent(this@SignUpActivity, HomeActivity::class.java).apply {
                    putExtra("success_register_msg", "Successfully registered!")
                }
                startActivity(eIntent)
                Log.d("Firebase", "Successfully registered!")
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}