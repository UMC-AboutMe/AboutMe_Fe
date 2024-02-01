package com.example.aboutme.MyprofileStorage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aboutme.R

class ProfileStorageDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_profile_storage_detail, container, false)

        setFrag(0)

        return view
    }

    private fun setFrag(fragNum: Int) {
        val ft = childFragmentManager.beginTransaction()
        when (fragNum) {
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
