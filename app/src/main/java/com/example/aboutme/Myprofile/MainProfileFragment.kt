package com.example.aboutme.Myprofile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentMainprofileBinding

class MainProfileFragment : Fragment(), FrontProfileFragment.OnProfileNameChangeListener {

    lateinit var binding: FragmentMainprofileBinding
    private val multiList = mutableListOf<MultiProfileData>() // 전역 변수로 multiList 선언
    private lateinit var vpadapter : MainProfileVPAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainprofileBinding.inflate(inflater, container, false)

        //initViewPager()





        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SharedPreferences에서 데이터를 읽어옴

        initViewPager()
    }

    private fun initViewPager() {
        val multiList = mutableListOf<MultiProfileData>()

        /*multiList.add(MultiProfileData(R.drawable.myprofile_character, "1", "010-1234-5678"))
        multiList.add(MultiProfileData(R.drawable.myprofile_character, "2", "010-1234-5678"))
        multiList.add(MultiProfileData(R.drawable.myprofile_character, "3", "010-1234-5678"))*/

        val sharedPreferences = requireContext().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "")

        //initViewPager()

        if (name != null) {
            multiList.add(MultiProfileData(R.drawable.myprofile_character, name, "010-1234-5678"))
            Log.d("profile!!", "success")
        } else {
            Log.e("MainProfileFragment", "Name is null")
        }


        vpadapter = MainProfileVPAdapter()

        vpadapter.submitList(multiList)

        binding.mainProfileVp.adapter = vpadapter

        binding.mainProfileVp.setCurrentItem(0, false)

    }

    override fun onProfileNameChanged(name: String) {
        // 변경된 이름 처리
    }
}