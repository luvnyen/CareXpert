package com.example.carexpert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout

class profile_main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_main)

        val _btnProfile = findViewById<ConstraintLayout>(R.id.btnProfile)
        _btnProfile.setOnClickListener {
            startActivity(Intent(this@profile_main, profile_edit::class.java))
        }

        val _btnBMI = findViewById<ConstraintLayout>(R.id.btnBMI)
        _btnBMI.setOnClickListener {
            startActivity(Intent(this@profile_main, bmi::class.java))
        }

        val _btnChat = findViewById<ConstraintLayout>(R.id.btnChat)
        _btnChat.setOnClickListener {
            startActivity(Intent(this@profile_main, chat::class.java))
        }
    }
}