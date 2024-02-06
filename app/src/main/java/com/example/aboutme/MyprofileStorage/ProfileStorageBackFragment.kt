package com.example.aboutme.MyprofileStorage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aboutme.Myprofile.FrontProfileFragment
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentBackprofileBinding
import com.example.aboutme.databinding.FragmentProfileStorageBackBinding

class ProfileStorageBackFragment : Fragment() {

    lateinit var binding: FragmentProfileStorageBackBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = FragmentProfileStorageBackBinding.inflate(inflater, container, false)

        binding.turnBtn2.setOnClickListener {
            val ft = parentFragmentManager.beginTransaction()

            ft.replace(R.id.profileStorage_frame, ProfileStorageFrontFragment()).commit()
        }

        return binding.root
    }
}