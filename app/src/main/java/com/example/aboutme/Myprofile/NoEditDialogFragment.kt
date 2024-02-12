package com.example.aboutme.Myprofile

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.PostProfile
import com.example.aboutme.RetrofitMyprofileData.ResponsePostProfile
import com.example.aboutme.databinding.ActivityNameDialogBinding
import com.example.aboutme.databinding.FragmentFrontprofileBinding
import com.example.aboutme.databinding.FragmentMainprofileBinding
import com.example.aboutme.databinding.FragmentNamelimitDialogBinding
import com.example.aboutme.databinding.FragmentNoeditDialogBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoEditDialogFragment():DialogFragment(){

    lateinit var binding: FragmentNoeditDialogBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentNoeditDialogBinding.inflate(inflater, container, false)


        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //dialog?.window?.setBackgroundDrawableResource(R.drawable.nameedit_box)
        dialog?.window?.setGravity(Gravity.CENTER)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.okayNameBtn.setOnClickListener {

            val intent = Intent(activity, MainActivity2::class.java)
            startActivity(intent)
            // 다이얼로그 종료
            dismiss()
        }

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()

        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceHeight = size.y
        val deviceWidth = size . x

        // 다이얼로그의 높이를 디바이스 높이의 25%로 설정
        params?.height = (deviceHeight * 0.25).toInt()
        params?.width = (deviceWidth * 0.85).toInt()

        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

}