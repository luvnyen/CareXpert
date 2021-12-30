package com.example.carexpert.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.carexpert.R
import com.example.carexpert.username_global

class ProfilePostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_post)

        val _btnProfile = findViewById<ConstraintLayout>(R.id.btnProfile)
        _btnProfile.setOnClickListener {
            startActivity(Intent(this@ProfilePostActivity, ProfileEditActivity::class.java))
        }

        val _btnBMI = findViewById<ConstraintLayout>(R.id.btnBMI)
        _btnBMI.setOnClickListener {
            startActivity(Intent(this@ProfilePostActivity, BMIActivity::class.java))
        }

        val _btnChat = findViewById<ConstraintLayout>(R.id.btnChat)
        _btnChat.setOnClickListener {
            startActivity(Intent(this@ProfilePostActivity, ChatActivity::class.java))
        }

        //Sign Out
        var sp : SharedPreferences = getSharedPreferences("usernameSP", Context.MODE_PRIVATE)
        val _tvSignOut : TextView = findViewById(R.id.tvSignOut)
        _tvSignOut.setOnClickListener {
            username_global = ""
            val editor = sp.edit()
            editor.putString("spUsername", username_global)
            editor.apply()

            startActivity(Intent(this@ProfilePostActivity, SignInActivity::class.java))
        }
    }
}