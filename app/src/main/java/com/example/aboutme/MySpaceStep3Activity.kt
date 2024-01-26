package com.example.aboutme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aboutme.databinding.ActivityMyspacestep3Binding

class MySpaceStep3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityMyspacestep3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyspacestep3Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}