package com.example.aboutme.Myspace

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.aboutme.Myprofile.SharedViewModel
import com.example.aboutme.bottomNavigationView
import com.example.aboutme.databinding.ActivityMyspacestep1Binding

class MySpaceStep1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityMyspacestep1Binding
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyspacestep1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel 초기화
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        binding.nextIbStep1.setOnClickListener {
            // EditText에서 텍스트 가져오기
            val inputText = binding.nickname.text.toString()

            // ViewModel에 데이터 저장
            sharedViewModel.nickname = inputText

            // 데이터는 ViewModel에 저장되어 있으므로 Bundle 사용할 필요 없음
            val intent = Intent(this, Test2Activity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        // 뒤로 가기 버튼을 눌렀을 때 bottomNavigationView로 이동
        val intent = Intent(this, bottomNavigationView::class.java)
        startActivity(intent)
        super.finish()
    }
}
