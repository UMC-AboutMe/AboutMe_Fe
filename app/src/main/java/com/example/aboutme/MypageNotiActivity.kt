package com.example.aboutme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aboutme.databinding.ActivityMypageBinding
import com.example.aboutme.databinding.ActivityMypageNotiBinding

class MypageNotiActivity : AppCompatActivity() {
    lateinit var binding : ActivityMypageNotiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageNotiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}