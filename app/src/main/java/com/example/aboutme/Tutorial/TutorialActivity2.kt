package com.example.aboutme.Tutorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.aboutme.R
import com.example.aboutme.databinding.ActivityTutorial1Binding
import com.example.aboutme.databinding.ActivityTutorial2Binding

class TutorialActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityTutorial2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding = DataBindingUtil.setContentView(this, R.layout.activity_tutorial2)
        binding = ActivityTutorial2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        //binding= DataBindingUtil.setContentView(this, R.layout.activity_tutorial1)
    }

}