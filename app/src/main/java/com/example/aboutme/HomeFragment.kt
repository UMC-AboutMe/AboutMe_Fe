package com.example.aboutme

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aboutme.Alarm.AlarmActivity
import com.example.aboutme.Mypage.MypageActivity
import com.example.aboutme.Search.SearchProfActivity
import com.example.aboutme.Search.SearchSpaceActivity
import com.example.aboutme.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.myprofBgIv.setOnClickListener{
            startActivity(Intent(requireActivity(), SearchProfActivity::class.java))
        }
        binding.myspaceBgIv.setOnClickListener{
            startActivity(Intent(requireActivity(), SearchSpaceActivity::class.java))
        }
        binding.mypageBtn.setOnClickListener{
            startActivity(Intent(requireActivity(), MypageActivity::class.java))
        }
        binding.alarmBtn.setOnClickListener{
            startActivity(Intent(requireActivity(), AlarmActivity::class.java))
        }
        return binding.root
    }


}