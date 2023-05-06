package com.example.influxapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn1 = findViewById(R.id.inco)
        btn2 = findViewById(R.id.editi)
        btn3 = findViewById(R.id.readi)

        btn1.setOnClickListener{
            val intent = Intent(this, addsavings::class.java)
            startActivity(intent)

        }

        btn2.setOnClickListener{
            val intent = Intent(this, Edit_savings::class.java)
            startActivity(intent)

        }

        btn3.setOnClickListener{
            val intent = Intent(this, Readsavings::class.java)
            startActivity(intent)

        }

    }
}
