package com.example.carexpert.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carexpert.*
import com.example.carexpert.adapter.PostAdapter
import com.example.carexpert.model.Post
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeActivity : AppCompatActivity() {

    private lateinit var _lvPost : RecyclerView
    private var arPost = arrayListOf<Post>()
    private lateinit var _write : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        this.supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val success_login_msg = intent.getStringExtra("success_login_msg")
        if (!success_login_msg.isNullOrBlank()) {
            Toast.makeText(this, success_login_msg, Toast.LENGTH_LONG).show()
        }

        val success_register_msg = intent.getStringExtra("success_register_msg")
        if (!success_register_msg.isNullOrBlank()) {
            Toast.makeText(this, success_register_msg, Toast.LENGTH_LONG).show()
        }

        val success_post_msg = intent.getStringExtra("success_post_msg")
        if (!success_post_msg.isNullOrBlank()) {
            Toast.makeText(this, success_post_msg, Toast.LENGTH_LONG).show()
        }

        val _exploreIcon = findViewById<ConstraintLayout>(R.id.exploreIcon)
        _exploreIcon.setOnClickListener {
            startActivity(Intent(this@HomeActivity, ExploreActivity::class.java))
        }

        val _profileIcon = findViewById<ConstraintLayout>(R.id.profileIcon)
        _profileIcon.setOnClickListener {
            startActivity(Intent(this@HomeActivity, ProfileEditActivity::class.java))
        }

        val _tvNoPost = findViewById<TextView>(R.id.tvNoPost)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        _lvPost = findViewById(R.id.lvPost)
        readData(db)

        _write = findViewById(R.id.button)
        _write.setOnClickListener{
            startActivity(Intent(this@HomeActivity, PostActivity::class.java))
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
        val _kotaLayout = findViewById<TextInputLayout>(R.id.kotaLayout)
        _spinner_kota.isEnabled = false;
        _spinner_kota.isClickable = false;

        val _tvResetFilter = findViewById<TextView>(R.id.tvResetFilter)

        _spinner_provinsi.setOnItemClickListener { _, _, _, _ ->
            _tvResetFilter.visibility = View.VISIBLE

            _lvPost = findViewById(R.id.lvPost)
            readData_Province(db, _spinner_provinsi.getText().toString())

            val index: Int = items_province.indexOf(_spinner_provinsi.getText().toString())
            i_global = index

            _spinner_kota.setText("")
            val items_kota: MutableList<String> = mutableListOf()

            getCityDataAPI(items_kota, this)
            val adapter_kota = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                items_kota
            )
            _spinner_kota.isEnabled = true;
            _spinner_kota.isClickable = true;
            _kotaLayout.hint = "City"
            adapter_kota.notifyDataSetChanged()
            _spinner_kota.setAdapter(adapter_kota)
        }

        _tvResetFilter.setOnClickListener {
            readData(db)

            _tvNoPost.visibility = View.GONE
            _tvResetFilter.visibility = View.GONE

            _spinner_kota.setText("")
            _spinner_provinsi.setText("")

            _spinner_kota.clearFocus()
            _spinner_provinsi.clearFocus()

            _spinner_kota.isEnabled = false;
            _spinner_kota.isClickable = false;
            _kotaLayout.hint = "Please select a province first"
        }

        // Spinner Kota Item Selected (Read Posts by Filter Province n City)
        _spinner_kota.setOnItemClickListener({ _, _, _, _ ->
            readData_Kota(db, _spinner_provinsi.getText().toString(),
                _spinner_kota.getText().toString())
            _tvResetFilter.visibility = View.VISIBLE
        })
    }

    private fun readData_Kota(db: FirebaseFirestore, province : String, city : String){
        db.collection("tbPost")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                arPost.clear()
                for (document in result){
                    if (document.data["provinsi"].toString() == province.toString() &&
                        document.data["kota"].toString() == city.toString()){
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
                } else {
                    _tvNoPost.visibility = View.GONE
                }

                _lvPost.layoutManager = LinearLayoutManager(this)
                val postAdapter = PostAdapter(arPost)
                _lvPost.adapter = postAdapter

                postAdapter.setOnItemClickCallback(object : PostAdapter.OnItemClickCallback{
                    override fun onItemClicked(data:Post){
                        val eIntent = Intent(this@HomeActivity, CommentActivity::class.java)
                        setPostOther(data.username, data.date, data.time)
                        startActivity(eIntent)
                    }
                })
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }

    private fun readData_Province(db: FirebaseFirestore, province : String){
        db.collection("tbPost")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                arPost.clear()
                for (document in result){
                    if (document.data["provinsi"].toString() == province.toString()){
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
                } else {
                    _tvNoPost.visibility = View.GONE
                }
                
                _lvPost.layoutManager = LinearLayoutManager(this)
                val postAdapter = PostAdapter(arPost)
                _lvPost.adapter = postAdapter

                postAdapter.setOnItemClickCallback(object : PostAdapter.OnItemClickCallback{
                    override fun onItemClicked(data:Post){
                        //GetData(db, data)
                        val eIntent = Intent(this@HomeActivity, CommentActivity::class.java)
                        setPostOther(data.username, data.date, data.time)
                        startActivity(eIntent)
                    }
                })
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }

    private fun readData(db: FirebaseFirestore){
        db.collection("tbPost")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                arPost.clear()
                for (document in result){
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
                        val eIntent = Intent(this@HomeActivity, CommentActivity::class.java)
                        setPostOther(data.username, data.date, data.time)
                        startActivity(eIntent)
                    }
                })
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}