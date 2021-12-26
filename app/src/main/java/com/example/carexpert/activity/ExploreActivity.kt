package com.example.carexpert.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carexpert.R
import com.example.carexpert.adapter.NewsAdapter
import com.example.carexpert.model.News
import com.google.firebase.firestore.FirebaseFirestore

class ExploreActivity : AppCompatActivity() {

    private lateinit var _lvBerita : RecyclerView
    private var arBerita = arrayListOf<News>()
    private lateinit var _SearchButton : ImageView

    companion object {
        const val username = "username"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        this.supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        //Pindah ke halaman Home
        val _username_from_ = intent.getStringExtra(username)
        val _home = findViewById<ImageView>(R.id.home)
        _home.setOnClickListener{
            val eIntent = Intent(this@ExploreActivity, HomeActivity::class.java).apply{
                putExtra(HomeActivity.username, _username_from_)
            }
            startActivity(eIntent)
        }

        //Search User by username
        val _username : EditText = findViewById(R.id.search_username)
        //var _username_from_ = intent.getStringExtra(username)
        _SearchButton = findViewById(R.id.SearchButton)
        _SearchButton.setOnClickListener{
            val eIntent = Intent(this@ExploreActivity, SearchProfileActivity::class.java).apply {
                putExtra(SearchProfileActivity.username_other, _username.text.toString())
                putExtra(SearchProfileActivity.username, _username_from_.toString())
            }
            startActivity(eIntent)
        }

        _username.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    val eIntent = Intent(this@ExploreActivity, SearchProfileActivity::class.java).apply {
                        putExtra(SearchProfileActivity.username_other, _username.text.toString())
                        putExtra(SearchProfileActivity.username, _username_from_.toString())
                    }
                    startActivity(eIntent)
                    true
                }
                else -> false
            }
        }

        //Read Berita
        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        _lvBerita = findViewById(R.id.lvBerita)
        readData(db)
    }

    private fun readData(db: FirebaseFirestore){
        db.collection("tbNews")
            .get()
            .addOnSuccessListener { result ->
                arBerita.clear()
                for (document in result){
                    val PostBaru = News(
                        document.data["judul"].toString(),
                        document.data["isi"].toString(),
                        document.data["link"].toString())
                    arBerita.add(PostBaru)
                }
                _lvBerita.layoutManager = LinearLayoutManager(this)
                val newsAdapter = NewsAdapter(arBerita)
                _lvBerita.adapter = newsAdapter
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}

