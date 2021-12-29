package com.example.carexpert.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carexpert.R
import com.example.carexpert.adapter.PostAdapter
import com.example.carexpert.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class SearchProfileActivity : AppCompatActivity() {
    private lateinit var _namalengkap : TextView
    private lateinit var _lvPost : RecyclerView
    private var arPost = arrayListOf<Post>()

    lateinit var _username_simpan : String


    override fun onCreate(savedInstanceState: Bundle?) {
        this.getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_profile)

        //Terima Intent
        val _username_akun = intent.getStringExtra(username)
        _username_simpan = _username_akun.toString()

        val _username_from_ = intent.getStringExtra(ExploreActivity.username)

        //Pindah ke halaman SignIn
        val _ImageView6 = findViewById<ImageView>(R.id.imageView7)
        _ImageView6.setOnClickListener{
            val eIntent = Intent(this@SearchProfileActivity, HomeActivity::class.java).apply {
                putExtra(HomeActivity.username, _username_from_)
            }
            startActivity(eIntent)
        }

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        val _username_from_explore = intent.getStringExtra(username_other)
        val _username : TextView = findViewById(R.id.username)
        _username.text = _username_from_explore

        _namalengkap = findViewById(R.id.namalengkap)

        _lvPost = findViewById(R.id.lvPost)
        readData_Profile(db, _username.text.toString())//_username_from_explore.toString())
        readData_Post(db, _username.text.toString())
    }

    companion object {
        const val username_other = "username_other"
        const val username = "username"
    }

    private fun readData_Profile(db: FirebaseFirestore, username : String){
        db.collection("tbUser")
            .get()
            .addOnSuccessListener { result ->
                //dataNama.clear()
                var found = 0
                for (document in result){
                    if (document.data["username"].toString() == username){
                        found = 1
                        _namalengkap.setText(document.data["nama"].toString())
                    }
                }
                if (found == 0){
                    _namalengkap.text = "Not Found"
                }
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }

    private fun readData_Post(db: FirebaseFirestore, username : String){
        db.collection("tbPost")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                //dataNama.clear()
//                var found = 0
                for (document in result){
                    if (document.data["username"].toString() == username){
//                        found = 1
                        val PostBaru = Post(
                            document.data["username"].toString(),
                            document.data["date"].toString(),
                            document.data["time"].toString(),
                            document.data["kota"].toString(),
                            document.data["provinsi"].toString(),
                            document.data["title"].toString(),
                            document.data["post"].toString())
                        arPost.add(PostBaru)
                    }
                    _lvPost.layoutManager = LinearLayoutManager(this)
                    val postAdapter = PostAdapter(arPost)
                    _lvPost.adapter = postAdapter

                    postAdapter.setOnItemClickCallback(object : PostAdapter.OnItemClickCallback{
                        override fun onItemClicked(data:Post){
                            //GetData(db, data)
                            val eIntent = Intent(this@SearchProfileActivity, CommentActivity::class.java)
                                .apply {
                                    //Kirim Username Akun

                                    putExtra(CommentActivity.username, _username_simpan)
                                    //Kirim dari Post
                                    putExtra(CommentActivity.username_post, data.username)
                                    putExtra(CommentActivity.date_post, data.date)
                                    putExtra(CommentActivity.time_post, data.time)
                                }
                            startActivity(eIntent)
                        }
                    })
                }
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}


