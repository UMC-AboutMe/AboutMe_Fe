package com.example.aboutme

import android.R.id.edit
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.aboutme.databinding.FragmentFrontprofileBinding


class FrontProfileFragment : Fragment() {

    lateinit var binding: FragmentFrontprofileBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentFrontprofileBinding.inflate(inflater,container,false)

        binding.turnBtn.setOnClickListener {
            val ft = parentFragmentManager.beginTransaction()

            ft.replace(R.id.profile_frame, BackProfileFragment()).commit()
        }


        binding.profileIv.setOnClickListener {

            val bottomSheet = BottomSheet()

            bottomSheet.show(childFragmentManager, bottomSheet.tag)

        }

        val str: String = binding.profileNameEt.text.toString()


        binding.profileNameEt.setOnClickListener {

            if (str.isEmpty()) {
                Toast.makeText(requireContext(), "이름은 필수 입력사항입니다", Toast.LENGTH_LONG).show()
            }

        }

        return binding.root
    }


}

