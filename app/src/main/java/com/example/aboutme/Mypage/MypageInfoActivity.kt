package com.example.aboutme.Mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aboutme.databinding.ActivityMypageBinding
import com.example.aboutme.databinding.ActivityMypageInfoBinding

class MypageInfoActivity : AppCompatActivity() {
    lateinit var binding : ActivityMypageInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}