package com.example.carexpert.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.carexpert.R
import com.example.carexpert.model.User
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class SignUpActivity : AppCompatActivity() {
    private var dataNama : ArrayList<User> = ArrayList()

    private lateinit var _nama : EditText
    private var _gender = ""
    private lateinit var _email : EditText
    private lateinit var _password : EditText
    private lateinit var _username : EditText
    private lateinit var _FailedSuccess : TextView


    private lateinit var _rlaki : RadioButton
    private lateinit var _rperempuan : RadioButton
    private lateinit var _rgroup : RadioGroup
    private lateinit var _fromDate : EditText

    private var datePickerDialog: DatePickerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        this.supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        //Pindah ke halaman TnC
        val _TnCText = findViewById<TextView>(R.id.TnCText)
        _TnCText.setOnClickListener{
            val eIntent = Intent(this@SignUpActivity, TnCActivity::class.java)
            startActivity(eIntent)
        }

        //Pindah ke halaman SignIn
        val _ImageView6 = findViewById<ImageView>(R.id.imageView6)
        _ImageView6.setOnClickListener{
            val eIntent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(eIntent)
        }

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        _nama = findViewById(R.id.namalengkap)
        _email = findViewById(R.id.email)
        _password = findViewById(R.id.Password)
        _username= findViewById(R.id.username)

        //Gender Radio Button
        _rlaki = findViewById(R.id.rlaki)
        _rperempuan = findViewById(R.id.rperempuan)
        _rgroup = findViewById(R.id.rgroup)
        _rgroup.setOnCheckedChangeListener { _, i ->
            val rb = findViewById<RadioButton>(i)
            if (rb != null) {
                _gender = rb.text.toString()
            }
        }

        //DateOfBirth Calendar
        _fromDate = findViewById(R.id.date)
        _fromDate.setOnClickListener { // calender class's instance and get current date , month and year from calender
            val c: Calendar = Calendar.getInstance()
            val mYear: Int = c.get(Calendar.YEAR) // current year
            val mMonth: Int = c.get(Calendar.MONTH) // current month
            val mDay: Int = c.get(Calendar.DAY_OF_MONTH) // current day
            // date picker dialog
            datePickerDialog = DatePickerDialog(
                this@SignUpActivity,
                { _, year, monthOfYear, dayOfMonth -> // set day of month , month and year value in the edit text
                    _fromDate.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                }, mYear, mMonth, mDay
            )
            datePickerDialog!!.show()
        }

        //Button Create Account
        val _SignUpButton = findViewById<TextView>(R.id.SignUpButton)
        _SignUpButton.setOnClickListener{
            readData(db, _nama.text.toString(), _username.text.toString(),_gender, _fromDate.text.toString(), //4
                _email.text.toString(), _password.text.toString())
            val eIntent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(eIntent)
        }
    }

    private fun TambahData(db: FirebaseFirestore, nama: String, username: String, gender: String, date: String, email: String, password: String){
        val namaBaru = User(date, email, gender, nama, password, username)
        db.collection("tbUser").document(username)
            .set(namaBaru)
            .addOnSuccessListener {
                _nama.setText("")
                _username.setText("")
                _email.setText("")
                _password.setText("")
                _fromDate.setText("")
                Log.d("Firebase", "Simpan Data Berhasil")
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }

    @SuppressLint("SetTextI18n")
    fun readData(db: FirebaseFirestore, nama: String, username: String, gender: String, date: String, email: String, password: String){
        db.collection("tbUser")
            .get()
            .addOnSuccessListener { result ->
                dataNama.clear()
                var found = 0
                for (document in result){
                    if (document.data["email"].toString() == email || document.data["username"].toString() == email){
                        found = 1
                        _FailedSuccess.text = "Username / Email telah dipakai"
                    }
                }
                if (found == 0){
                    Toast.makeText(applicationContext, "Pendaftaran berhasil", Toast.LENGTH_SHORT).show()
                    TambahData(db, nama, username, gender, date, email, password)
                }
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}