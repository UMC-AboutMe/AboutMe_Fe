package com.example.aboutme.Tutorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aboutme.bottomNavigationView
import com.example.aboutme.databinding.ActivityTutorial3Binding

class TutorialActivity3 : AppCompatActivity() {

    private lateinit var binding: ActivityTutorial3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding = DataBindingUtil.setContentView(this, R.layout.activity_tutorial3)
        binding = ActivityTutorial3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.okbtn.setOnClickListener {
            val intent= Intent(this, bottomNavigationView::class.java)
            startActivity(intent)
        }
    }

}