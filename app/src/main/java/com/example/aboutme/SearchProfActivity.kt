package com.example.aboutme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.aboutme.databinding.ActivitySearchProfBinding

class SearchProfActivity : AppCompatActivity() {

    lateinit var binding : ActivitySearchProfBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchProfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Dialog
        binding.addBtn.setOnClickListener {
            CustomDialog("내 프로필도 공유 하시겠습니까?")
                .show(supportFragmentManager, "ProfDialog")
        }
        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.searchBtn.setOnClickListener {
            if (binding.searchTv.text.toString() == "123456") {
                binding.profBg.visibility = View.VISIBLE
                binding.addBtn.visibility = View.VISIBLE
                binding.exampleIv.visibility = View.VISIBLE
            }
            else {
                binding.profBg.visibility = View.GONE
                binding.addBtn.visibility = View.GONE
                binding.exampleIv.visibility = View.GONE
            }
        }

    }
}