package com.example.aboutme

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.aboutme.databinding.FragmentFrontprofileBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheet() : BottomSheetDialogFragment(){

    lateinit var binding : FragmentFrontprofileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentFrontprofileBinding.inflate(inflater,container,false)

        return inflater.inflate(R.layout.fragment_sharebottomsheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<ImageButton>(R.id.shareBottomSheet_image_btn)?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)

        }

    }
    private val activityResult : ActivityResultLauncher<Intent> = registerForActivityResult(

        ActivityResultContracts.StartActivityForResult()){

        //결과 코드 ok,결과 널 아닐때
        if (it.resultCode == RESULT_OK && it.data != null){

            //값 담기
            val uri = it.data!!.data


            Glide.with(requireContext()).load(uri) //이미지
                .into(binding.profileIv) //이미지 보여줄 위치

            Log.d("Gallery", "shareBottomSheetImageBtn clicked")
        }
    }
}