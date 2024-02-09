package com.example.aboutme.Tutorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.aboutme.R
import com.example.aboutme.Search.CustomDialog
import com.example.aboutme.bottomNavigationView
import com.example.aboutme.databinding.ActivityTutorial1Binding

class TutorialActivity1 : AppCompatActivity() {

    private lateinit var binding : ActivityTutorial1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorial1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        //Dialog
        CustomDialogAlarm("알림을 보내도록 허용하시겠습니까?")
            .show(supportFragmentManager, "AlarmDialog")
        //binding= DataBindingUtil.setContentView(this, R.layout.activity_tutorial1)
        binding.tutNextBtn1.setOnClickListener {
            binding.tutNextBtn2.visibility = View.VISIBLE
            binding.tut2Tv.visibility = View.VISIBLE

            binding.tutNextBtn1.visibility = View.GONE
            binding.tut1Tv.visibility = View.GONE
            binding.tutNextBtn3.visibility = View.GONE
            binding.tut3Tv.visibility = View.GONE
        }
        binding.tutNextBtn2.setOnClickListener {
            binding.tutNextBtn3.visibility = View.VISIBLE
            binding.tut3Tv.visibility = View.VISIBLE

            binding.tutNextBtn1.visibility = View.GONE
            binding.tut1Tv.visibility = View.GONE
            binding.tutNextBtn2.visibility = View.GONE
            binding.tut2Tv.visibility = View.GONE
        }
        binding.tutNextBtn3.setOnClickListener {
            val intent= Intent(this, bottomNavigationView::class.java)
            startActivity(intent)
        }
    }
    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}