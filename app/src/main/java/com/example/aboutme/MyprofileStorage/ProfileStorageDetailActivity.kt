package com.example.aboutme.MyprofileStorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.aboutme.Myprofile.BackProfileFragment
import com.example.aboutme.Myprofile.FrontProfileFragment
import com.example.aboutme.R

class ProfileStorageDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_storage_detail)

        setFrag1(0)

        val receivedIntent = intent

    }

    private fun setFrag1(fragNum : Int){
        val ft = supportFragmentManager.beginTransaction()
        when(fragNum)
        {
            0 -> {
                Log.d("MyProfileFragment", "FrontProfileFragment로 교체 중")
                ft.replace(R.id.profileStorage_frame, ProfileStorageFrontFragment()).commit()
            }

            1 -> {
                Log.d("MyProfileFragment", "BackProfileFragment로 교체 중")
                ft.replace(R.id.profileStorage_frame, ProfileStorageBackFragment()).commit()
            }
        }
    }

}