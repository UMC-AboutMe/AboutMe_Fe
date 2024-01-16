package com.example.aboutme

import android.app.Activity
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
import com.example.aboutme.databinding.FragmentMyprofileBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheet2 : BottomSheetDialogFragment() {

    lateinit var binding : FragmentMyprofileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentMyprofileBinding.inflate(inflater,container,false)

        return inflater.inflate(R.layout.fragment_sharebottomsheet2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<ImageButton>(R.id.shareBottomSheet_image_btn)?.setOnClickListener {

        }

    }



}