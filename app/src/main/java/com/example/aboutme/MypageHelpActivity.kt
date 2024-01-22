package com.example.aboutme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aboutme.databinding.ActivityMypageBinding
import com.example.aboutme.databinding.ActivityMypageHelpBinding

class MypageHelpActivity : AppCompatActivity() {
    lateinit var binding : ActivityMypageHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}