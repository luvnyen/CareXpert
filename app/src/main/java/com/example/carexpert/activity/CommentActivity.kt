package com.example.carexpert.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carexpert.R
import com.example.carexpert.adapter.CommentAdapter
import com.example.carexpert.model.Comment
import com.example.carexpert.setAutoCompleteTextViewEmptyError
import com.example.carexpert.setTextInputEmptyError
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommentActivity : AppCompatActivity() {
    private lateinit var _username : TextView
    private lateinit var _date : TextView
    private lateinit var _time : TextView
    private lateinit var _spinner_kota : TextView
    private lateinit var _spinner_provinsi : TextView
    private lateinit var _title_post : TextView
    private lateinit var _post : TextView

    private lateinit var _send : ImageView

    private lateinit var _comment : TextInputEditText
    private lateinit var _commentLayout : TextInputLayout

    private var arComment = arrayListOf<Comment>()
    private lateinit var _lvComment : RecyclerView

    lateinit var _username_simpan : String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        this.supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        //Terima Intent
        val _username_akun = intent.getStringExtra(HomeActivity.username)
        _username_simpan = _username_akun.toString()

        _username = findViewById(R.id.username)
        _date = findViewById(R.id.date)
        _time = findViewById(R.id.time)
        _spinner_kota = findViewById(R.id.spinner_kota)
        _spinner_provinsi = findViewById(R.id.spinner_provinsi)
        _title_post = findViewById(R.id.title_post)
        _post = findViewById(R.id.post)

        //Terima Intent
        //val _username_akun = intent.getStringExtra(username)
        val _username_post = intent.getStringExtra(username_post)
        val _date_post = intent.getStringExtra(date_post)
        val _time_post = intent.getStringExtra(time_post)

        //Username di klik ke Search Profile
        _username.setOnClickListener {
            val eIntent = Intent(this@CommentActivity, SearchProfileActivity::class.java).apply {
                putExtra(SearchProfileActivity.username, _username_akun)
                putExtra(SearchProfileActivity.username_other, _username.text.toString())
            }
            startActivity(eIntent)
        }

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        //Cari Post
        readData_Post(db, _username_post.toString(), _date_post.toString(), _time_post.toString())

        //Pindah ke halaman Home
        val _ImageView6 = findViewById<ImageView>(R.id.BackButton3)
        _ImageView6.setOnClickListener{
            val eIntent = Intent(this@CommentActivity, HomeActivity::class.java).apply {
                putExtra(HomeActivity.username, _username_akun)
            }
            startActivity(eIntent)
        }

        //Kirim Komen
        _send = findViewById(R.id.send)
        _comment = findViewById(R.id.comment)
        _commentLayout = findViewById(R.id.commentLayout)
        _send.setOnClickListener {
            if (TextUtils.isEmpty(_comment.text)) {
                setTextInputEmptyError(_comment, _commentLayout, "Comment")
            } else {
                //date and time upload
                val current = LocalDateTime.now()
                val formatter_date = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val formatter_time = DateTimeFormatter.ofPattern("HH:mm:ss")
                val formatted_date = current.format(formatter_date)
                val formatted_time = current.format(formatter_time)
                TambahComment(db, _username.text.toString(), _date.text.toString(), _time.text.toString(),
                    _username_simpan, _comment.text.toString(), formatted_date.toString(),
                    formatted_time.toString())

                //Refresh Comment Lists
                _lvComment = findViewById(R.id.lvComment)
                readData_Comments(db, _username_post.toString(), _date_post.toString(), _time_post.toString())
            }
        }

        //Refresh Comment Lists
        _lvComment = findViewById(R.id.lvComment)
        readData_Comments(db, _username_post.toString(), _date_post.toString(), _time_post.toString())

    }

    private fun readData_Comments(db: FirebaseFirestore, username_post: String, date_post: String,
                                  time_post: String){
        db.collection("tbComment")
            .get()
            .addOnSuccessListener { result ->
                arComment.clear()
                for (document in result){
                    if (document.data["username_post"].toString() == username_post &&
                        document.data["date_post"].toString() == date_post &&
                        document.data["time_post"].toString() == time_post ){
                        val PostBaru = Comment(
                            document.data["username_post"].toString(),
                            document.data["date_post"].toString(),
                            document.data["time_post"].toString(),
                            document.data["username_comment"].toString(),
                            document.data["comment"].toString(),
                            document.data["date_comment"].toString(),
                            document.data["time_comment"].toString()
                        )
                        arComment.add(PostBaru)
                    }
                }
                _lvComment.layoutManager = LinearLayoutManager(this)
                val commentAdapter = CommentAdapter(arComment)
                _lvComment.adapter = commentAdapter

                commentAdapter.setOnItemClickCallback(object : CommentAdapter.OnItemClickCallback{
                    override fun onItemClicked(data:Comment){
                        //GetData(db, data)
                        val eIntent = Intent(this@CommentActivity, SearchProfileActivity::class.java)
                            .apply {
                                //Kirim Username Akun
                                putExtra(SearchProfileActivity.username, _username_simpan)
                                //Kirim dari Comment
                                putExtra(SearchProfileActivity.username_other, data.username_comment)
                            }
                        startActivity(eIntent)
                    }
                })


            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }

    private fun TambahComment(db: FirebaseFirestore, username_post: String, date_post: String,
                              time_post: String, username_comment: String, comment: String,
                              date_comment: String, time_comment: String){
        val namaBaru = Comment(username_post, date_post, time_post, username_comment,
            comment, date_comment, time_comment)
        db.collection("tbComment")
            .add(namaBaru)
            .addOnSuccessListener {
                _comment.setText("")
                Log.d("Firebase", "Simpan Data Berhasil")
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }

    private fun readData_Post(db: FirebaseFirestore, username : String, date : String, time : String){
        db.collection("tbPost")
            .get()
            .addOnSuccessListener { result ->
//                var found = 0
                for (document in result){
                    if (document.data["username"].toString() == username &&
                        document.data["date"].toString() == date &&
                        document.data["time"].toString() == time ){
//                        found = 1
                        _username.text = document.data["username"].toString()
                        _date.text = document.data["date"].toString()
                        _time.text = document.data["time"].toString()
                        _spinner_kota.text = document.data["kota"].toString()
                        _spinner_provinsi.text = document.data["provinsi"].toString()
                        _title_post.text = document.data["title"].toString()
                        _post.text = document.data["post"].toString()
                    }
                }
//                if (found == 0){
//
//                }
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }

    companion object {
        const val username = "username"
        const val username_post = "username_post"
        const val date_post = "date_post"
        const val time_post = "time_post"
    }
}


