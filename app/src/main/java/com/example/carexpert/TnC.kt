package com.example.carexpert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class TnC : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tn_c)

        val _BackButton = findViewById<ImageView>(R.id.BackButton)
        _BackButton.setOnClickListener{
            val eIntent = Intent(this@TnC, SignUpPage::class.java)
            startActivity(eIntent)
        }
    }
}