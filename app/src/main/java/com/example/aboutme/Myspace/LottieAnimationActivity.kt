package com.example.aboutme.Myspace

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.aboutme.R
import com.example.aboutme.bottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LottieAnimationActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lottie_animation)
    }

    override fun onResume() {
        super.onResume()

        // 바텀네비게이션에서 Lottie를 진행하라는 신호를 받는다
        val signal = intent.getStringExtra("Lottie")

        // 바텀네비게이션으로 보내는 다음 신호를 위해서 기존 intent에 새겨져있는 정보 삭제
        intent.removeExtra("stepcompleted")

        // 바텀네비게이션에서 Lottie를 진행하라는 신호를 받았을 때
        if (signal == "Lottie") {
            val intent = Intent(this, bottomNavigationView::class.java)
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)  // 3초간 애니메이션 효과
                intent.putExtra("stepCompleted", "myspace")
                startActivity(intent)
            }
        }
    }
}