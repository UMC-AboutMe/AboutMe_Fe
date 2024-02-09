package com.example.aboutme.Tutorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.aboutme.R
import com.example.aboutme.Search.CustomDialog
import com.example.aboutme.databinding.ActivityTutorial1Binding

class TutorialActivity1 : AppCompatActivity() {

    private lateinit var binding : ActivityTutorial1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorial1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        //Dialog

        //CustomDialogAlarm()
        CustomDialogAlarm("알림을 보내도록 허용하시겠습니까?")

            .show(supportFragmentManager, "AlarmDialog")
        //binding= DataBindingUtil.setContentView(this, R.layout.activity_tutorial1)
        binding.nextBtn1.setOnClickListener {
            val intent= Intent(this, TutorialActivity2::class.java)
            startActivity(intent)
        }
    }
    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}