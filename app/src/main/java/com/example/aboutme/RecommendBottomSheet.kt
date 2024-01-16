package com.example.aboutme

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.example.aboutme.databinding.FragmentBackprofileBinding
import com.example.aboutme.databinding.FragmentMyprofileBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RecommendBottomSheet : BottomSheetDialogFragment() {

    lateinit var binding : FragmentBackprofileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentBackprofileBinding.inflate(inflater,container,false)

        return inflater.inflate(R.layout.fragment_recommendbottomsheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<ImageButton>(R.id.mbti_btn)?.setOnClickListener {

        }

    }



}