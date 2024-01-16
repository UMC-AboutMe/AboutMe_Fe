package com.example.aboutme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aboutme.databinding.FragmentBackprofileBinding
import com.example.aboutme.databinding.FragmentFrontprofileBinding

class BackProfileFragment : Fragment() {

    lateinit var binding : FragmentBackprofileBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentBackprofileBinding.inflate(inflater,container, false)

        binding.turnBtn2.setOnClickListener {
            val ft = parentFragmentManager.beginTransaction()

            ft.replace(R.id.profile_frame, FrontProfileFragment()).commit()
        }

        binding.backProfileEt1.setOnClickListener {

            val bottomSheet = RecommendBottomSheet()

            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        binding.backProfileEt2.setOnClickListener {

            val bottomSheet = RecommendBottomSheet()

            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        binding.backProfileEt3.setOnClickListener {

            val bottomSheet = RecommendBottomSheet()

            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        binding.backProfileEt4.setOnClickListener {

            val bottomSheet = RecommendBottomSheet()

            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        binding.backProfileEt5.setOnClickListener {

            val bottomSheet = RecommendBottomSheet()

            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }



        return binding.root
    }
}