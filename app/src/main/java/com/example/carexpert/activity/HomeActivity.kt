package com.example.carexpert.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carexpert.*
import com.example.carexpert.adapter.PostAdapter
import com.example.carexpert.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeActivity : AppCompatActivity() {

    private lateinit var _lvPost : RecyclerView
    private var arPost = arrayListOf<Post>()
    private lateinit var _write : Button
    private lateinit var _spinner : Spinner
    private lateinit var _spinner2 : Spinner

    lateinit var _username_simpan : String

    companion object {
        const val username = "username"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        this.supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Pindah ke halaman Explore
        val _explore = findViewById<ImageView>(R.id.explore)
        _explore.setOnClickListener{
            val eIntent = Intent(this@HomeActivity, ExploreActivity::class.java)
            startActivity(eIntent)
        }

        //Read all Post
        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        _lvPost = findViewById(R.id.lvPost)
        readData(db)

        //Pindah ke halaman Post
        _write = findViewById(R.id.button)
        _write.setOnClickListener{
            val eIntent = Intent(this@HomeActivity, PostActivity::class.java)
            startActivity(eIntent)
        }

        //Spinner Province
        var _spinner_provinsi : AutoCompleteTextView = findViewById(R.id.spinner_provinsi)
        val items_province: MutableList<String> = mutableListOf()
        getProvinceDataAPI(items_province, this)
        val adapter_province = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            items_province
        )
        _spinner_provinsi.setAdapter(adapter_province)

        //Spinner Kota
        var _spinner_kota : AutoCompleteTextView = findViewById(R.id.spinner_kota)

        _spinner_provinsi.setOnItemClickListener(AdapterView.OnItemClickListener { arg0, arg1, arg2, arg3 ->
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
            adapter_kota.notifyDataSetChanged()
            _spinner_kota.setAdapter(adapter_kota)

        })

        //Spinner Kota Item Selected (Read Posts by Filter Province n City)
        _spinner_kota.setOnItemClickListener(AdapterView.OnItemClickListener { arg0, arg1, arg2, arg3 ->

        })

//        //Filter Spinner 1
//        _spinner = findViewById(R.id.spinner1)
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.type,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            _spinner.adapter = adapter
//        }
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.type,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            _spinner.adapter = adapter
//        }
//        _spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
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
//        //Filter Spinner 2
//        _spinner2 = findViewById(R.id.spinner2)
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.type,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            _spinner2.adapter = adapter
//        }
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.type,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            _spinner2.adapter = adapter
//        }
//        _spinner2.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
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
}