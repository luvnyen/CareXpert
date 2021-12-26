package com.example.carexpert.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.carexpert.R
import com.example.carexpert.model.User
import com.google.firebase.firestore.FirebaseFirestore


class SignInActivity : AppCompatActivity() {
    private var dataNama : ArrayList<User> = ArrayList()

    private lateinit var _username : EditText
    private lateinit var _password : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        this.getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //Pindah ke halaman SignUp
        val _daftar = findViewById<TextView>(R.id.daftar)
        _daftar.setOnClickListener{
            val eIntent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(eIntent)
        }

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        //Button Login
        _username = findViewById<EditText>(R.id.username)
        _password = findViewById<EditText>(R.id.password)
        val _verifikasi = findViewById<Button>(R.id.verifikasi)
        _verifikasi.setOnClickListener{
            _verifikasi.isSelected != _verifikasi.isSelected
            readData(db, _username.text.toString(), _password.text.toString())
        }
    }

    private fun readData(db: FirebaseFirestore, username : String, password: String){
        db.collection("tbUser")
            .get()
            .addOnSuccessListener { result ->
                dataNama.clear()
                var found = 0
                for (document in result){
                    if (document.data["username"].toString() == username &&
                        document.data["password"].toString() == password){
                        found = 1
                        Toast.makeText(applicationContext, "Login Success", Toast.LENGTH_SHORT).show()

                        _password.setText("")
                        val eIntent = Intent(this@SignInActivity,HomeActivity::class.java).apply {
                            putExtra(HomeActivity.username, _username.text.toString())
                        }
                        startActivity(eIntent)
                        _username.setText("")
                    }
                }
                if (found == 0){
                    Toast.makeText(applicationContext, "Username/Password salah", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}