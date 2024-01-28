package com.example.aboutme.Myprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentMainprofileBinding

class MainProfileFragment : Fragment() {

    lateinit var binding: FragmentMainprofileBinding
    private val multiList = mutableListOf<MultiProfileData>()
    private lateinit var vpadapter : MainProfileVPAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainprofileBinding.inflate(inflater, container, false)

        initViewPager()

        // ViewPager2 설정


        // 첫 번째 뷰페이저의 페이지 변경 이벤트 처리



        return binding.root
    }

    private fun initViewPager() {
        val multiList = mutableListOf<MultiProfileData>()

        multiList.add(MultiProfileData(R.drawable.myprofile_character, "1", "010-1234-5678"))
        multiList.add(MultiProfileData(R.drawable.myprofile_character, "2", "010-1234-5678"))
        multiList.add(MultiProfileData(R.drawable.myprofile_character, "3", "010-1234-5678"))


        vpadapter = MainProfileVPAdapter()

        vpadapter.submitList(multiList)

        binding.mainProfileVp.adapter = vpadapter

        binding.mainProfileVp.setCurrentItem(0, false)

    }
}