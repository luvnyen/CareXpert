package com.example.carexpert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class LoginPage : AppCompatActivity() {
    var dataNama : ArrayList<daftarUser> = ArrayList<daftarUser>()

    lateinit var _username : EditText
    lateinit var _password : EditText
    lateinit var _FailedSuccess : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        val _daftar = findViewById<TextView>(R.id.daftar)
        _daftar.setOnClickListener{
            val eIntent = Intent(this@LoginPage, SignUpPage::class.java)
            startActivity(eIntent)
        }

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        _username = findViewById<EditText>(R.id.username)
        _password = findViewById<EditText>(R.id.password)
        _FailedSuccess = findViewById<TextView>(R.id.FailedSucess)

        val _verifikasi = findViewById<Button>(R.id.verifikasi)
        _verifikasi.setOnClickListener{
            readData(db, _username.text.toString(), _password.text.toString())
        }
    }

    fun readData(db: FirebaseFirestore, username : String, password: String){
        db.collection("tbUser")
            .get()
            .addOnSuccessListener { result ->
                dataNama.clear()
                var found = 0
                for (document in result){
                    if (document.data.get("username").toString() == username &&
                        document.data.get("password").toString() == password){
                        found = 1
                        _FailedSuccess.setText("Success")
                        _username.setText("")
                        _password.setText("")

//                        val userObj = user(document.data.get("date").toString(), document.data.get("email").toString(),document.data.get("gender").toString(),document.data.get("nama").toString(),document.data.get("password").toString(), document.data.get("username").toString())
                        //Pindah Intent

                    }
                }
                if (found == 0){
                    _FailedSuccess.setText("Username/Password tidak sesuai")
                }
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}