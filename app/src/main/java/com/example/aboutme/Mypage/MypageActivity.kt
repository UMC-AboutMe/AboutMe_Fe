package com.example.aboutme.Mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aboutme.databinding.ActivityMypageBinding

class MypageActivity : AppCompatActivity() {
    lateinit var binding : ActivityMypageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.infoBtn.setOnClickListener {
            val intent = Intent(this, MypageInfoActivity::class.java)
            startActivity(intent)
        }
        binding.insightBtn.setOnClickListener {
            val intent = Intent(this, MypageInsightActivity::class.java)
            startActivity(intent)
        }
        binding.settingBtn.setOnClickListener {
            val intent = Intent(this, MypageSettingActivity::class.java)
            startActivity(intent)
        }
        binding.notifBtn.setOnClickListener {
            val intent = Intent(this, MypageNotiActivity::class.java)
            startActivity(intent)
        }
        binding.helpBtn.setOnClickListener {
            val intent = Intent(this, MypageHelpActivity::class.java)
            startActivity(intent)
        }
        binding.backBtn.setOnClickListener {
            finish()
        }
    }


}