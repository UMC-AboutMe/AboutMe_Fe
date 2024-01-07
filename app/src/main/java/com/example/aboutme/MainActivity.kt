package com.example.aboutme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.aboutme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.loginKakao.setOnClickListener {
            Toast.makeText(this, "kakaoclick", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, TutorialActivity1::class.java)
            startActivity(intent)
        }

        binding.loginGoogle.setOnClickListener {
            Toast.makeText(this, "googleclick", Toast.LENGTH_SHORT).show()
        }
    }
}