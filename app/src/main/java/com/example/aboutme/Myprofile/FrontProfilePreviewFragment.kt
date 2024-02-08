package com.example.aboutme.Myprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentFrontprofileBinding

class FrontProfilePreviewFragment : Fragment(){

    lateinit var binding: FragmentFrontprofileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFrontprofileBinding.inflate(inflater, container, false)

        binding.turnBtn.setOnClickListener {
            val ft = requireActivity().supportFragmentManager.beginTransaction()

            ft.replace(R.id.profile_frame2, BackProfilePreviewFragment()).commit()
        }

        return binding.root
    }


}