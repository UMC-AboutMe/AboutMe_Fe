package com.example.aboutme

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.aboutme.databinding.FragmentFrontprofileBinding
import com.example.aboutme.databinding.FragmentSharebottomsheetBinding


class FrontProfileFragment : Fragment() {

    lateinit var binding: FragmentFrontprofileBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val frontProfileBinding = FragmentFrontprofileBinding.inflate(inflater, container, false)
        val shareBottomSheetBinding =
            FragmentSharebottomsheetBinding.inflate(inflater, container, false)

        // 데이터 클래스에 담아서 반환


        binding = FragmentFrontprofileBinding.inflate(inflater,container,false)

        binding.turnBtn.setOnClickListener {
            val ft = parentFragmentManager.beginTransaction()

            ft.replace(R.id.profile_frame, BackProfileFragment()).commit()
        }


        binding.profileIv.setOnClickListener {

            val bottomSheet = BottomSheet()

            bottomSheet.show(childFragmentManager, bottomSheet.tag)

        }



        return binding.root
    }


}

