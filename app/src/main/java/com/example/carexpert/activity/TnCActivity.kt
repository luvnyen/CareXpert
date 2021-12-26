package com.example.carexpert.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.carexpert.R

class TnCActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        this.supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tnc)

        //Button Accept
        val _BackButton = findViewById<Button>(R.id.BackButton)
        _BackButton.setOnClickListener{
            val eIntent = Intent(this@TnCActivity, SignUpActivity::class.java)
            startActivity(eIntent)
        }
    }
}