package com.example.aboutme.Myprofile

import androidx.appcompat.app.AppCompatActivity
import com.example.aboutme.databinding.ActivityEditprofileBinding
import com.google.android.material.tabs.TabLayout

class EditProfileActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityEditprofileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setTabLayout()
    }
    private fun setTabLayout() {

        binding.storeFragmentTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            // tab이 선택되었을 때
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> binding.tabLayoutContainer.()
                    1 -> binding.tabLayoutContainer.()
                }
            }
            // tab이 선택되지 않았을 때
            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
            // tab이 다시 선택되었을 때
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

}