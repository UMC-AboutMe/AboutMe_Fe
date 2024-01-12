package com.example.aboutme

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.aboutme.databinding.FragmentFrontprofileBinding


class FrontProfileFragment : Fragment() {



    lateinit var binding : FragmentFrontprofileBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentFrontprofileBinding.inflate(inflater,container, false)

        binding.turnBtn.setOnClickListener {
            val ft = parentFragmentManager.beginTransaction()

            ft.replace(R.id.profile_frame, BackProfileFragment()).commit()
        }

        binding.profileIv.setOnClickListener {

            //갤러리 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)

        }


        return binding.root
    }

    //이미지 가져오기
    private val activityResult : ActivityResultLauncher<Intent> = registerForActivityResult(

        ActivityResultContracts.StartActivityForResult()){

        //결과 코드 ok,결과 널 아닐때
            if (it.resultCode == RESULT_OK && it.data != null){

            //값 담기
                val uri = it.data!!.data

                Glide.with(requireContext()).load(uri) //이미지
                    .into(binding.profileIv) //이미지 보여줄 위치
        }
    }


}