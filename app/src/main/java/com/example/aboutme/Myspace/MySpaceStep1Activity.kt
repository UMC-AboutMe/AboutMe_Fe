package com.example.aboutme.Myspace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aboutme.databinding.ActivityMyspacestep1Binding

class MySpaceStep1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityMyspacestep1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyspacestep1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextIbStep1.setOnClickListener {
            val intent = Intent(this, MySpaceStep2Activity::class.java)
            startActivity(intent)
        }
    }
}