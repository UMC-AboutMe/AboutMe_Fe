package com.example.aboutme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.aboutme.databinding.ActivityTutorial1Binding

class TutorialActivity1 : AppCompatActivity() {

    private lateinit var binding : ActivityTutorial1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= DataBindingUtil.setContentView(this, R.layout.activity_tutorial1)
        binding.nextIv.setOnClickListener {
            val intent= Intent(this, TutorialActivity2::class.java)
            startActivity(intent)
        }
    }
}