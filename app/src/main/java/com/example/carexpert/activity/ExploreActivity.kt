package com.example.carexpert.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carexpert.R
import com.example.carexpert.adapter.NewsAdapter
import com.example.carexpert.getCovidDataAPI
import com.example.carexpert.model.News
import com.example.carexpert.setOtherUsername
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class ExploreActivity : AppCompatActivity() {

    private lateinit var _lvBerita : RecyclerView
    private var arBerita = arrayListOf<News>()
    private lateinit var _SearchButton : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        this.supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        val _homeIcon = findViewById<ConstraintLayout>(R.id.homeIcon)
        _homeIcon.setOnClickListener {
            startActivity(Intent(this@ExploreActivity, HomeActivity::class.java))
        }

        val _profileIcon = findViewById<ConstraintLayout>(R.id.profileIcon)
        _profileIcon.setOnClickListener {
            startActivity(Intent(this@ExploreActivity, ProfileEditActivity::class.java))
        }

        val _username : TextInputEditText = findViewById(R.id.search_username)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        _username.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    if (!TextUtils.isEmpty(_username.text)) {
                        db.collection("tbUser")
                            .get()
                            .addOnSuccessListener { result ->
                                var userFound = false
                                for (document in result) {
                                    if (document.data["username"].toString() == _username.text.toString()) {
                                        userFound = true
                                        setOtherUsername(_username.text.toString())
                                        startActivity(Intent(this@ExploreActivity, SearchProfileActivity::class.java))
                                    }
                                }

                                if (!userFound) {
                                    Toast.makeText(this, "Username not found!", Toast.LENGTH_LONG).show()
                                }
                            }
                            .addOnFailureListener{
                                Log.d("Firebase", it.message.toString())
                            }
                    } else {
                        Toast.makeText(this, "Username cannot be empty!", Toast.LENGTH_LONG).show()
                    }

                    true
                }
                else -> false
            }
        }

        val _tvConfirmedCases = findViewById<TextView>(R.id.tvConfirmedCases)
        getCovidDataAPI("jumlah_positif",_tvConfirmedCases, this,"total")

        val _tvAdditionConfirmedCases = findViewById<TextView>(R.id.tvAdditionConfirmedCases)
        getCovidDataAPI("jumlah_positif",_tvAdditionConfirmedCases, this,"penambahan")

        val _tvDeathCases = findViewById<TextView>(R.id.tvDeathCases)
        getCovidDataAPI("jumlah_meninggal",_tvDeathCases, this,"total")

        val _tvAdditionDeathCases = findViewById<TextView>(R.id.tvAdditionDeathCases)
        getCovidDataAPI("jumlah_meninggal",_tvAdditionDeathCases, this,"penambahan")

        val _tvLastUpdated = findViewById<TextView>(R.id.tvLastUpdated)
        getCovidDataAPI("tanggal",_tvLastUpdated, this,"penambahan")

        val _tvSeeMore = findViewById<TextView>(R.id.tvSeeMore)
        _tvSeeMore.setOnClickListener {
            startActivity(Intent(this@ExploreActivity, COVID19DataActivity::class.java))
        }

        //Read Berita
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

