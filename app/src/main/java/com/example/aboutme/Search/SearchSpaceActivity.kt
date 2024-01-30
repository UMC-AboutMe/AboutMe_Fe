package com.example.aboutme.Search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.aboutme.R
import com.example.aboutme.databinding.ActivitySearchSpaceBinding

class SearchSpaceActivity : AppCompatActivity() {

    lateinit var binding : ActivitySearchSpaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_space)

        binding = ActivitySearchSpaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Dialog
        binding.addBtn.setOnClickListener {
                CustomDialogSpace("내 스페이스도 공유 하시겠습니까?")
                .show(supportFragmentManager, "SpaceDialog")
        }
        //뒤로가기
        binding.backBtn.setOnClickListener {
            finish()
        }
        //제약 조건
        binding.searchBtn.setOnClickListener {
            if (binding.searchTv.text.toString() == "teddy") {
                binding.profBg.visibility = View.VISIBLE
                binding.addBtn.visibility = View.VISIBLE
                binding.exampleIv.visibility = View.VISIBLE
                binding.myspaceName.visibility=View.VISIBLE
            }
            else {
                binding.profBg.visibility = View.GONE
                binding.addBtn.visibility = View.GONE
                binding.exampleIv.visibility = View.GONE
                binding.myspaceName.visibility=View.GONE
            }
        }
    }
}