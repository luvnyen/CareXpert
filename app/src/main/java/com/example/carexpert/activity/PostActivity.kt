package com.example.carexpert.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.carexpert.*
import com.example.carexpert.model.Post
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.widget.AdapterView

import android.widget.AdapterView.OnItemClickListener




class PostActivity : AppCompatActivity() {
    private var dataPost : ArrayList<Post> = ArrayList()

//    private lateinit var _date : TextView
//    private lateinit var _time : TextView

    private lateinit var _spinnerKota: AutoCompleteTextView
    private lateinit var _kotaLayout: TextInputLayout

    private lateinit var _spinnerProvinsi: AutoCompleteTextView
    private lateinit var _provinsiLayout: TextInputLayout

    private lateinit var _title_post: TextInputEditText
    private lateinit var _titleLayout: TextInputLayout

    private lateinit var _post: TextInputEditText
    private lateinit var _postLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        this.getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val _backButton = findViewById<ImageView>(R.id.BackButton2)
        _backButton.setOnClickListener{
            startActivity(Intent(this@PostActivity, HomeActivity::class.java))
        }

        val _spinner_provinsi : AutoCompleteTextView = findViewById(R.id.spinner_provinsi)
        val items_province: MutableList<String> = mutableListOf()
        getProvinceDataAPI(items_province, this)
        val adapter_province = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            items_province
        )
        _spinner_provinsi.setAdapter(adapter_province)

        val _spinner_kota : AutoCompleteTextView = findViewById(R.id.spinner_kota)
        _spinner_kota.isEnabled = false;
        _spinner_kota.isClickable = false;

        _spinner_provinsi.setOnItemClickListener { _, _, _, _ ->
            val index: Int = items_province.indexOf(_spinner_provinsi.text.toString())
            i_global = index

            _spinner_kota.setText("")
            val items_kota: MutableList<String> = mutableListOf()

            getCityDataAPI(items_kota, this)

            val adapter_kota = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                items_kota
            )
            adapter_kota.notifyDataSetChanged()

            _spinner_kota.isEnabled = true;
            _spinner_kota.isClickable = true;
            _kotaLayout.hint = "City"
            _spinner_kota.setAdapter(adapter_kota)
        }

//        _spinner_provinsi.setOnClickListener {
//            if (!TextUtils.isEmpty(_spinner_provinsi.getText())){
//                getCityDataAPI(_spinner_provinsi.text.toString())
//                var _spinner_kota : AutoCompleteTextView = findViewById(R.id.spinner_kota)
//                val items_kota: MutableList<String> = mutableListOf()
//                getCityDataAPI_(items_kota, this)
//                val adapter_kota = ArrayAdapter(
//                    this,
//                    android.R.layout.simple_spinner_dropdown_item,
//                    items_kota
//                )
//                _spinner_kota.setAdapter(adapter_kota)
//            }
//        }

        // Get username
//        var _username_from_login = intent.getStringExtra(username)
//        _username = findViewById(R.id.username)
//        _username.text = username_global


//        //Get Date and Time Post
//        val current = LocalDateTime.now()
//        val formatter_date = DateTimeFormatter.ofPattern("dd/MM/yyyy")
//        val formatter_time = DateTimeFormatter.ofPattern("HH:mm:ss")
//        val formatted_date = current.format(formatter_date)
//        val formatted_time = current.format(formatter_time)
//        _date = findViewById(R.id.date)
//        _date.setText(formatted_date)
//        _time = findViewById(R.id.time)
//        _time.setText(formatted_time)
//
//        //Spinner Kota
        _spinnerKota = findViewById(R.id.spinner_kota)
//        _spinnerKota.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View,
//                position: Int,
//                id: Long
//            ) {
//                parent.getItemAtPosition(position)
//            }
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        })
//
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.kota,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            _spinnerKota.adapter = adapter
//        }
//
//        //Spinner Provinsi
        _spinnerProvinsi = findViewById(R.id.spinner_provinsi)
//        _spinnerProvinsi.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View,
//                position: Int,
//                id: Long
//            ) {
//                parent.getItemAtPosition(position)
//            }
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        })
//
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.Provinsi,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            _spinnerProvinsi.adapter = adapter
//        }
//

        _kotaLayout = findViewById(R.id.kotaLayout)
        _provinsiLayout = findViewById(R.id.provinsiLayout)

        _title_post = findViewById(R.id.title_post)
        _titleLayout = findViewById(R.id.titleLayout)

        _post = findViewById(R.id.post)
        _postLayout = findViewById(R.id.postLayout)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        val _btnPost : Button = findViewById(R.id.BtnPost)
        _btnPost.setOnClickListener {
            if (TextUtils.isEmpty(_spinnerKota.text) || TextUtils.isEmpty(_spinnerProvinsi.text) || TextUtils.isEmpty(_title_post.text) || TextUtils.isEmpty(_post.text)) {
                setAutoCompleteTextViewEmptyError(_spinnerKota, _kotaLayout, "City")
                setAutoCompleteTextViewEmptyError(_spinnerProvinsi, _provinsiLayout, "Province")
                setTextInputEmptyError(_title_post, _titleLayout, "Post Title")
                setTextInputEmptyError(_post, _postLayout, "Post Description")
            } else {
                //            TambahData(db, _username.text.toString(), _date.text.toString(), _time.text.toString(),
//                _spinnerKota.getSelectedItem().toString(),
//                _spinnerProvinsi.getSelectedItem().toString(),
//                _title.text.toString(), _post.text.toString())

//                val eIntent = Intent(this@PostActivity, HomeActivity::class.java)
//                startActivity(eIntent)
            }
        }
    }

//    private fun TambahData(db: FirebaseFirestore, username : String, date : String, time : String, Kota : String,
//                           Provinsi : String, Expired : String, Post : String){
//        val namaBaru = Post(username, date, time, Kota, Provinsi, Expired, Post)
//        db.collection("tbPost")
//            .add(namaBaru)
//            .addOnSuccessListener {
//                _title.setText("")
//                _post.setText("")
//                readData(db)
//                Log.d("Firebase", "Simpan Data Berhasil")
//            }
//            .addOnFailureListener{
//                Log.d("Firebase", it.message.toString())
//            }
//    }
//
//    private fun readData(db: FirebaseFirestore){
//        db.collection("tbPost")
//            .get()
//            .addOnSuccessListener { result ->
//                dataPost.clear()
//                for (document in result){
//                    val namaBaru = Post(
//                        document.data["username"].toString(),
//                        document.data["date"].toString(),
//                        document.data["time"].toString(),
//                        document.data["kota"].toString(),
//                        document.data["provinsi"].toString(),
//                        document.data["title"].toString(),
//                        document.data["post"].toString())
//                    dataPost.add(namaBaru)
//                }
//                //lvAdapter.notifyDataSetChanged()
//            }
//            .addOnFailureListener{
//                Log.d("Firebase", it.message.toString())
//            }
//    }
}