package com.example.carexpert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

class SignUpPage : AppCompatActivity() {
    var dataNama : ArrayList<daftarUser> = ArrayList<daftarUser>()

    lateinit var _nama : EditText
    lateinit var _username : EditText
    lateinit var _email : EditText
    lateinit var _password : EditText
    lateinit var _FailedSuccess : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        val _BackButton = findViewById<ImageView>(R.id.BackButton)
        _BackButton.setOnClickListener{
            val eIntent = Intent(this@SignUpPage, LoginPage::class.java)
            startActivity(eIntent)
        }

        val _TnCText = findViewById<TextView>(R.id.TnCText)
        _TnCText.setOnClickListener{
            val eIntent = Intent(this@SignUpPage, TnC::class.java)
            startActivity(eIntent)
        }

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        _nama = findViewById<EditText>(R.id.namalengkap)
        _username = findViewById<EditText>(R.id.user)
        _email = findViewById<EditText>(R.id.email)
        _password = findViewById<EditText>(R.id.Password)
        _FailedSuccess = findViewById(R.id.FailedSuccess)

        val _SignUpButton = findViewById<TextView>(R.id.SignUpButton)
        _SignUpButton.setOnClickListener{
            readData(db, _nama.text.toString(), _username.text.toString(),
                _email.text.toString(), _password.text.toString())
            //val eIntent = Intent(this@SignUpPage, LoginPage::class.java)
            //startActivity(eIntent)
        }

    }

    fun TambahData(db: FirebaseFirestore, nama: String, username: String, email: String, password: String){
        val namaBaru = daftarUser(nama, username, email, password)
        db.collection("tbUser").document(username)
            .set(namaBaru)
            .addOnSuccessListener {
                _nama.setText("")
                _username.setText("")
                _email.setText("")
                _password.setText("")
                Log.d("Firebase", "Simpan Data Berhasil")
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }

    fun readData(db: FirebaseFirestore, nama: String, username: String, email: String, password: String){
        db.collection("tbUser")
            .get()
            .addOnSuccessListener { result ->
                dataNama.clear()
                var found = 0
                for (document in result){
                    if (document.data.get("username").toString() == username ||
                        document.data.get("email").toString() == email){
                        found = 1
                        _FailedSuccess.setText("Username/Email telah dipakai")
                    }
                }
                if (found == 0){
                    _FailedSuccess.setText("Success")
                    TambahData(db, nama, username, email, password)
                }
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }

    //fun readData(db: FirebaseFirestore){
    //    db.collection("tbUser")
    //        .get()
    //        .addOnSuccessListener { result ->
    //            dataNama.clear()
    //            for (document in result){
    //                val namaBaru = daftarUser(document.data.get("nama").toString(),
    //                    document.data.get("username").toString(),
    //                    document.data.get("email").toString(),
    //                    document.data.get("password").toString())
    //                dataNama.add(namaBaru)
    //            }
    //        }
    //        .addOnFailureListener{
    //            Log.d("Firebase", it.message.toString())
    //        }
    //}
}