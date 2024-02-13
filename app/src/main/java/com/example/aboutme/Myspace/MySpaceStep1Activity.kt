package com.example.aboutme.Myspace

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.aboutme.R
import com.example.aboutme.bottomNavigationView
import com.example.aboutme.databinding.ActivityMyspacestep1Binding

class MySpaceStep1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityMyspacestep1Binding

    private val sharedViewModel: MyspaceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyspacestep1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("isCreated", "${sharedViewModel.isCreated}")

        // 다음 버튼 클릭시
        binding.nextIbStep1.setOnClickListener {
            // EditText에서 텍스트 가져오기
            val inputText = binding.nickname.text.toString()

            // ViewModel에 데이터 저장
            sharedViewModel.nickname = inputText

            val intent = Intent(this, MySpaceStep2Activity::class.java)
            startActivity(intent)

            // 애니메이션 설정
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // 홈로고 클릭시
        binding.memberLogo.setOnClickListener {
            val intent = Intent(this, bottomNavigationView::class.java)
            startActivity(intent)

            // 애니메이션 설정
            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
        }
    }

    override fun onBackPressed() {
        // 뒤로 가기 버튼을 눌렀을 때 bottomNavigationView로 이동
        val intent = Intent(this, bottomNavigationView::class.java)
        startActivity(intent)
        super.finish()
    }
}
