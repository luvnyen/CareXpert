package com.example.carexpert.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carexpert.R
import com.example.carexpert.adapter.NewsAdapter
import com.example.carexpert.getCovidDataAPI
import com.example.carexpert.model.News
import com.example.carexpert.setOtherUsername
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class ExploreActivity : AppCompatActivity() {

    private lateinit var _lvBerita : RecyclerView
    private var arBerita = arrayListOf<News>()
    private lateinit var _SearchButton : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        this.supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        //Pindah ke halaman Home
        val _home = findViewById<ImageView>(R.id.home)
        _home.setOnClickListener{
            val eIntent = Intent(this@ExploreActivity, HomeActivity::class.java)
            startActivity(eIntent)
        }

        //Search User by username
        val _username : EditText = findViewById(R.id.search_username)
        //var _username_from_ = intent.getStringExtra(username)
        _SearchButton = findViewById(R.id.SearchButton)
        _SearchButton.setOnClickListener{
            val eIntent = Intent(this@ExploreActivity, SearchProfileActivity::class.java)
            setOtherUsername(_username.text.toString())
            startActivity(eIntent)
        }

        _username.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    val eIntent = Intent(this@ExploreActivity, SearchProfileActivity::class.java)
                    setOtherUsername(_username.text.toString())
                    startActivity(eIntent)
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

