package com.example.aboutme.Tutorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.aboutme.Myspace.MySpaceStep1Activity
import com.example.aboutme.R
import com.example.aboutme.bottomNavigationView
import com.example.aboutme.databinding.ActivityTutorial2Binding
import com.example.aboutme.databinding.ActivityTutorial3Binding
import com.google.android.material.bottomnavigation.BottomNavigationView

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