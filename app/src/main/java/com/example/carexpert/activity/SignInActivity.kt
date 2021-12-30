package com.example.carexpert.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.example.carexpert.R
import com.example.carexpert.setTextInputEmptyError
import com.example.carexpert.setUsername
import com.example.carexpert.username_global
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text


class SignInActivity : AppCompatActivity() {
    lateinit var sp : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        this.getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        sp = getSharedPreferences("usernameSP",Context.MODE_PRIVATE)

        // pindah ke halaman SignUp
        val _daftar = findViewById<TextView>(R.id.daftar)
        _daftar.setOnClickListener{
            val eIntent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(eIntent)
        }

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        val _usernameLayout = findViewById<TextInputLayout>(R.id.usernameLayout)
        val _username = findViewById<TextInputEditText>(R.id.username)

        val _passwordLayout = findViewById<TextInputLayout>(R.id.passwordLayout)
        val _password = findViewById<TextInputEditText>(R.id.password)

        val _tvWrongCredentials = findViewById<TextView>(R.id.tvWrongCredentials)

        val _verifikasi = findViewById<Button>(R.id.verifikasi)
        _verifikasi.setOnClickListener{
            if (TextUtils.isEmpty(_username.text) || TextUtils.isEmpty(_password.text)) {
                setTextInputEmptyError(_username, _usernameLayout, "Username")
                setTextInputEmptyError(_password, _passwordLayout, "Password")
            } else {
                _verifikasi.text = "Signing In..."
                db.collection("tbUser")
                    .get()
                    .addOnSuccessListener { result ->
                        var found = false
                        for (document in result){
                            if (document.data["username"].toString() == _username.text.toString() &&
                                document.data["password"].toString() == _password.text.toString()){
                                found = true
                                setUsername(_username.text.toString())

                                val editor = sp.edit()
                                editor.putString("spUsername", username_global)
                                editor.apply()

                                val eIntent = Intent(this@SignInActivity, HomeActivity::class.java).apply {
                                    putExtra("success_login_msg", "Welcome back!")
                                }
                                startActivity(eIntent)
                            }
                        }
                        if (!found){
                            _verifikasi.text = "Sign In"
                            _usernameLayout.error = "Enter a correct username"
                            _passwordLayout.error = "Enter a correct password"
                            _tvWrongCredentials.visibility = View.VISIBLE
                        }
                    }
                    .addOnFailureListener{
                        Log.d("Firebase", it.message.toString())
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        username_global = sp.getString("spUsername",null).toString()
        if (username_global != "" && username_global != "null"){
            val eIntent = Intent(this@SignInActivity, HomeActivity::class.java)
            startActivity(eIntent)
        }
    }
    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}