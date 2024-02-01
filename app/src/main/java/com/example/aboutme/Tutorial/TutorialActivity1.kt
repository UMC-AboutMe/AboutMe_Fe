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
        CustomDialogAlarm("내 프로필도 공유 하시겠습니까?")
            .show(supportFragmentManager, "AlarmDialog")
        //binding= DataBindingUtil.setContentView(this, R.layout.activity_tutorial1)
        binding.nextBtn1.setOnClickListener {
            val intent= Intent(this, TutorialActivity2::class.java)
            //시작 애니메이션만 제거 intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }
    override fun onPause() {
        super.onPause()
        //시작,종료 애니메이션 모두 제거
        overridePendingTransition(0, 0)
    }
}