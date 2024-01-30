package com.example.aboutme

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aboutme.databinding.ActivityMyspacestep1Binding

class MySpaceStep1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityMyspacestep1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyspacestep1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextIbStep1.setOnClickListener {

            // EditText에서 텍스트 가져오기
            val inputText = binding.nickname.text.toString()

            val intent = Intent(this, MySpaceStep2Activity::class.java)
            intent.putExtra("nickname", inputText)

            startActivityWithAnimation(intent)
        }
    }

    private fun startActivityWithAnimation(intent: Intent) {
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }
}