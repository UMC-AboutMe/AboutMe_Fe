package com.example.aboutme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.aboutme.databinding.ActivityAgitBinding

class AgitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_agit)
//        setContentView(binding.root)
//
//        val recyclerView = binding.recyclerView
//        recyclerView.layoutManager = GridLayoutManager(this, 2)
//        recyclerView.adapter = YourAdapter()
    }
}
