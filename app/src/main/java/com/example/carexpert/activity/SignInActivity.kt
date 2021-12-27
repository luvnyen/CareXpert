package com.example.carexpert.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.carexpert.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore


class SignInActivity : AppCompatActivity() {

    private lateinit var _username : EditText
    private lateinit var _password : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        this.getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // pindah ke halaman SignUp
        val _daftar = findViewById<TextView>(R.id.daftar)
        _daftar.setOnClickListener{
            val eIntent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(eIntent)
        }

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        val _usernameLayout = findViewById<TextInputLayout>(R.id.usernameLayout)
        _username = findViewById(R.id.username)

        val _passwordLayout = findViewById<TextInputLayout>(R.id.passwordLayout)
        _password = findViewById(R.id.password)

        val _tvWrongCredentials = findViewById<TextView>(R.id.tvWrongCredentials)

        val _verifikasi = findViewById<Button>(R.id.verifikasi)
        _verifikasi.setOnClickListener{
            userSignIn(db, _username.text.toString(), _password.text.toString(), _usernameLayout, _passwordLayout, _tvWrongCredentials, _verifikasi)
        }
    }

    private fun userSignIn(db: FirebaseFirestore, username : String, password: String, usernameLayout: TextInputLayout, passwordLayout: TextInputLayout, tvWrongCredentials: TextView, btnSignIn: Button){
        btnSignIn.text = "Signing In..."
        db.collection("tbUser")
            .get()
            .addOnSuccessListener { result ->
                var found = false
                for (document in result){
                    if (document.data["username"].toString() == username &&
                        document.data["password"].toString() == password){
                        found = true
                        val eIntent = Intent(this@SignInActivity, HomeActivity::class.java).apply {
                            putExtra(HomeActivity.username, _username.text.toString())
                            putExtra("success_login_msg", "Welcome back!")
                        }
                        startActivity(eIntent)
                    }
                }
                if (!found){
                    btnSignIn.text = "Sign In"
                    usernameLayout.error = "Enter a correct username"
                    passwordLayout.error = "Enter a correct password"
                    tvWrongCredentials.visibility = View.VISIBLE
                }
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}