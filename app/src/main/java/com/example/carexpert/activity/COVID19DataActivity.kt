package com.example.carexpert.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.carexpert.R
import com.example.carexpert.getCovidDataAPI
import com.example.carexpert.getCovidDataAPIWorldwide

class COVID19DataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_covid19_data)

        val _backButton = findViewById<ImageView>(R.id.backButton)
        _backButton.setOnClickListener {
            startActivity(Intent(this@COVID19DataActivity, ExploreActivity::class.java))
        }

        // WORLDWIDE

        val _tvConfirmedCasesWorldwide = findViewById<TextView>(R.id.tvConfirmedCasesWorldwide)
        getCovidDataAPIWorldwide("positif",_tvConfirmedCasesWorldwide, this)

        val _tvDeathCasesWorldwide = findViewById<TextView>(R.id.tvDeathCasesWorldwide)
        getCovidDataAPIWorldwide("meninggal",_tvDeathCasesWorldwide, this)

        // INDONESIA

        val _tvConfirmedCasesIndonesia = findViewById<TextView>(R.id.tvConfirmedCasesIndonesia)
        getCovidDataAPI("jumlah_positif",_tvConfirmedCasesIndonesia, this,"total")
        val _tvAdditionConfirmedCasesIndonesia = findViewById<TextView>(R.id.tvAdditionConfirmedCasesIndonesia)
        getCovidDataAPI("jumlah_positif",_tvAdditionConfirmedCasesIndonesia, this,"penambahan")

        val _tvDeathCasesIndonesia = findViewById<TextView>(R.id.tvDeathCasesIndonesia)
        getCovidDataAPI("jumlah_meninggal",_tvDeathCasesIndonesia, this,"total")
        val _tvAdditionDeathCasesIndonesia = findViewById<TextView>(R.id.tvAdditionDeathCasesIndonesia)
        getCovidDataAPI("jumlah_meninggal",_tvAdditionDeathCasesIndonesia, this,"penambahan")

        val _tvRecoveredCasesIndonesia = findViewById<TextView>(R.id.tvRecoveredCasesIndonesia)
        getCovidDataAPI("jumlah_sembuh",_tvRecoveredCasesIndonesia, this,"total")
        val _tvAdditionRecoveredCasesIndonesia = findViewById<TextView>(R.id.tvAdditionRecoveredCasesIndonesia)
        getCovidDataAPI("jumlah_sembuh",_tvAdditionRecoveredCasesIndonesia, this,"penambahan")

        val _tvTreatedCasesIndonesia = findViewById<TextView>(R.id.tvTreatedCasesIndonesia)
        getCovidDataAPI("jumlah_dirawat",_tvTreatedCasesIndonesia, this,"total")
        val _tvAdditionTreatedCasesIndonesia = findViewById<TextView>(R.id.tvAdditionTreatedCasesIndonesia)
        getCovidDataAPI("jumlah_dirawat",_tvAdditionTreatedCasesIndonesia, this,"penambahan")

        val _tvLastUpdatedIndonesia = findViewById<TextView>(R.id.tvLastUpdatedIndonesia)
        getCovidDataAPI("tanggal",_tvLastUpdatedIndonesia, this,"penambahan")
    }
}