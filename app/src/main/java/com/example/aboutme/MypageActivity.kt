package com.example.aboutme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aboutme.databinding.ActivityMypageBinding
import com.example.aboutme.databinding.ActivitySearchProfBinding

class MypageActivity : AppCompatActivity() {
    lateinit var binding : ActivityMypageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_mypage)

        binding.backBtn.setOnClickListener {
            finish()
        }
    }


}