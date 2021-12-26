package com.example.carexpert.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.carexpert.R
import com.example.carexpert.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PostActivity : AppCompatActivity() {
    private var dataPost : ArrayList<Post> = ArrayList()

    private lateinit var _username : TextView
    private lateinit var _date : TextView
    private lateinit var _time : TextView
    private lateinit var _spinnerKota : Spinner
    private lateinit var _spinnerProvinsi : Spinner
    private lateinit var _title : EditText
    private lateinit var _post : EditText

    companion object {
        const val username = "username"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        this.getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        //Pindah ke halaman SignIn
        val _username_from_login = intent.getStringExtra(username)
        val _ImageView6 = findViewById<ImageView>(R.id.BackButton2)
        _ImageView6.setOnClickListener{
            val eIntent = Intent(this@PostActivity, HomeActivity::class.java).apply {
                putExtra(HomeActivity.username, _username_from_login)
            }
            startActivity(eIntent)
        }

        //Get username
        //var _username_from_login = intent.getStringExtra(username)
        _username = findViewById(R.id.username)
        _username.text = _username_from_login


        //Get Date and Time Post
        val current = LocalDateTime.now()
        val formatter_date = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formatter_time = DateTimeFormatter.ofPattern("HH:mm:ss")
        val formatted_date = current.format(formatter_date)
        val formatted_time = current.format(formatter_time)
        _date = findViewById(R.id.date)
        _date.setText(formatted_date)
        _time = findViewById(R.id.time)
        _time.setText(formatted_time)

        //Spinner Kota
        _spinnerKota = findViewById(R.id.spinner_kota)
        _spinnerKota.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                parent.getItemAtPosition(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        ArrayAdapter.createFromResource(
            this,
            R.array.kota,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            _spinnerKota.adapter = adapter
        }

        //Spinner Provinsi
        _spinnerProvinsi = findViewById(R.id.spinner_provinsi)
        _spinnerProvinsi.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                parent.getItemAtPosition(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        ArrayAdapter.createFromResource(
            this,
            R.array.Provinsi,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            _spinnerProvinsi.adapter = adapter
        }

        //Get Title and Post
        _title = findViewById(R.id.title_post)
        _post = findViewById(R.id.post)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        //Button Post
        val _btnPost : Button = findViewById(R.id.BtnPost)
        _btnPost.setOnClickListener {
            _btnPost.isSelected != _btnPost.isSelected
            TambahData(db, _username.text.toString(), _date.text.toString(), _time.text.toString(),
                _spinnerKota.getSelectedItem().toString(),
                _spinnerProvinsi.getSelectedItem().toString(),
                _title.text.toString(), _post.text.toString())

                val eIntent = Intent(this@PostActivity, HomeActivity::class.java)
                startActivity(eIntent)
        }
    }

    private fun TambahData(db: FirebaseFirestore, username : String, date : String, time : String, Kota : String,
                           Provinsi : String, Expired : String, Post : String){
        val namaBaru = Post(username, date, time, Kota, Provinsi, Expired, Post)
        db.collection("tbPost")
            .add(namaBaru)
            .addOnSuccessListener {
                _title.setText("")
                _post.setText("")
                readData(db)
                Log.d("Firebase", "Simpan Data Berhasil")
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }

    private fun readData(db: FirebaseFirestore){
        db.collection("tbPost")
            .get()
            .addOnSuccessListener { result ->
                dataPost.clear()
                for (document in result){
                    val namaBaru = Post(
                        document.data["username"].toString(),
                        document.data["date"].toString(),
                        document.data["time"].toString(),
                        document.data["kota"].toString(),
                        document.data["provinsi"].toString(),
                        document.data["title"].toString(),
                        document.data["post"].toString())
                    dataPost.add(namaBaru)
                }
                //lvAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}