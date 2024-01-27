package com.example.aboutme.MyprofileStorage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aboutme.Myprofile.BackProfileFragment
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentFrontprofileBinding
import com.example.aboutme.databinding.FragmentProfileStorageFrontBinding

class ProfileStorageFrontFragment : Fragment(){

    lateinit var binding: FragmentProfileStorageFrontBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentProfileStorageFrontBinding.inflate(inflater, container, false)

        binding.turnBtn.setOnClickListener {
            val ft = parentFragmentManager.beginTransaction()

            ft.replace(R.id.profileStorage_frame, ProfileStorageBackFragment()).commit()

        }

        return binding.root
    }

}