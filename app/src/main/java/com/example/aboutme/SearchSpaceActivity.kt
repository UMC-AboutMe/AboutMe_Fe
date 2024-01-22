package com.example.aboutme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aboutme.databinding.ActivitySearchProfBinding
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
            CustomDialog("내 스페이스도 공유 하시겠습니까?")
                .show(supportFragmentManager, "SpaceDialog")
        }
    }
}