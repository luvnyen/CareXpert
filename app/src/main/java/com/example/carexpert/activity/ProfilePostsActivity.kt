package com.example.carexpert.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carexpert.R
import com.example.carexpert.adapter.PostAdapter
import com.example.carexpert.model.Post
import com.example.carexpert.setPostOther
import com.example.carexpert.username_global
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ProfilePostsActivity : AppCompatActivity() {

    private lateinit var _lvPost : RecyclerView
    private var arPost = arrayListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_posts)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        val _exploreIcon = findViewById<ConstraintLayout>(R.id.exploreIcon)
        _exploreIcon.setOnClickListener {
            startActivity(Intent(this@ProfilePostsActivity, ExploreActivity::class.java))
        }

        val _homeIcon = findViewById<ConstraintLayout>(R.id.homeIcon)
        _homeIcon.setOnClickListener {
            startActivity(Intent(this@ProfilePostsActivity, HomeActivity::class.java))
        }

        // get user data
        db.collection("tbUser").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data["username"].toString() == username_global) {
                        val _tvProfileName = findViewById<TextView>(R.id.tvProfileName)
                        _tvProfileName.text = document.data["nama"].toString()
                    }
                }
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }

        val _btnProfile = findViewById<ConstraintLayout>(R.id.btnProfile)
        _btnProfile.setOnClickListener {
            startActivity(Intent(this@ProfilePostsActivity, ProfileEditActivity::class.java))
        }

        val _btnBMI = findViewById<ConstraintLayout>(R.id.btnBMI)
        _btnBMI.setOnClickListener {
            startActivity(Intent(this@ProfilePostsActivity, BMIActivity::class.java))
        }

        _lvPost = findViewById(R.id.lvPost)

        db.collection("tbPost")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    if (document.data["username"].toString() == username_global){
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
                }

                val _tvNoPost = findViewById<TextView>(R.id.tvNoPost)
                if (arPost.isEmpty()) {
                    _tvNoPost.visibility = View.VISIBLE
                }

                _lvPost.layoutManager = LinearLayoutManager(this)
                val postAdapter = PostAdapter(arPost)
                _lvPost.adapter = postAdapter

                postAdapter.setOnItemClickCallback(object : PostAdapter.OnItemClickCallback {
                    override fun onItemClicked(data:Post){
                        setPostOther(data.username, data.date, data.time)
                        startActivity(Intent(this@ProfilePostsActivity, CommentActivity::class.java))
                    }
                })
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}