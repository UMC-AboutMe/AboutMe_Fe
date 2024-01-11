package com.example.aboutme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aboutme.databinding.FragmentFrontprofileBinding

class FrontProfileFragment : Fragment() {

    lateinit var binding : FragmentFrontprofileBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentFrontprofileBinding.inflate(inflater,container, false)

        binding.turnBtn.setOnClickListener {
            val ft = parentFragmentManager.beginTransaction()

            ft.replace(R.id.profile_frame, BackProfileFragment()).commit()
        }



        return binding.root
    }
}