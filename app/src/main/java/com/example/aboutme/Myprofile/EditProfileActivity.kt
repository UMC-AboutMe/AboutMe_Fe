package com.example.aboutme.Myprofile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.aboutme.R
import com.example.aboutme.databinding.ActivityEditprofileBinding
import com.example.aboutme.databinding.FragmentEditprofilebackBinding
import com.example.aboutme.databinding.FragmentEditprofilefrontBinding
import com.google.android.material.tabs.TabLayout

class EditProfileActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityEditprofileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val profileId = intent.getStringExtra("profileId")
        Log.d("intent데이터",profileId.toString())

        supportFragmentManager.beginTransaction()
            .replace(R.id.tab_layout_container, EditProfileFrontFragment().apply {
                arguments = Bundle().apply {
                    // profileId를 가져와 Bundle에 추가
                    val profileId = intent.getStringExtra("profileId")
                    putString("profilId1", profileId)
                    val dialogName = intent.getStringExtra("dialogName")
                    putString("dialogName",dialogName)
                }
            })
            .commit()


        setTabLayout()

        /*binding.profileEditPreviewBtn.setOnClickListener {
            val intent2 = Intent(this, PreviewProfileActivity::class.java)
            intent2.putExtra("prodilId2",profileId)
            startActivity(intent2)
        }*/



    }


    private fun setTabLayout() {

        binding.storeFragmentTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            // tab이 선택되었을 때
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.tab_layout_container, EditProfileFrontFragment())
                            .commit()
                    }
                    1 -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.tab_layout_container, EditProfileBackFragment().apply {
                                arguments = Bundle().apply {
                                    val profileId = intent.getStringExtra("profileId")
                                    putString("profilId1", profileId)
                                }
                            })
                            .commit()
                    }
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