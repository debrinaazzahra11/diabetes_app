package com.example.diabetes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ActivityMasuk : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masuk)

        val buttonMasuk: Button = findViewById(R.id.buttonmasuk)
        buttonMasuk.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("destination", R.id.nav_tentang)
            startActivity(intent)
        }
    }
}