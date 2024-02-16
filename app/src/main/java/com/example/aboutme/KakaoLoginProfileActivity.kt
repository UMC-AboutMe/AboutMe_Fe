package com.example.aboutme

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.aboutme.Tutorial.TutorialActivity1
import com.example.aboutme.databinding.ActivityKakaoLoginProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class KakaoLoginProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKakaoLoginProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityKakaoLoginProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인텐트로부터 데이터 가져오기
        val email = intent.getStringExtra("email")
        val name = intent.getStringExtra("name")
        val profileImageUrl = intent.getStringExtra("profile")

        // 가져온 데이터를 화면에 설정
        binding.emailTv.text = email
        binding.nameTv.text = name

        // Glide 등의 라이브러리를 사용하여 프로필 이미지 표시
        Glide.with(this).load(profileImageUrl).into(binding.profileIv)
    }

    override fun onResume() {
        super.onResume()

        val intent = Intent(this, TutorialActivity1::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)  // 1초간 프로필 화면 보여주기
            startActivity(intent)
        }
    }
}