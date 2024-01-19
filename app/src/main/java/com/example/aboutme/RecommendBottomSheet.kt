package com.example.aboutme

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.aboutme.databinding.FragmentBackprofileBinding
import com.example.aboutme.databinding.FragmentMyprofileBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class RecommendBottomSheet : DialogFragment() {

    lateinit var binding : FragmentBackprofileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentBackprofileBinding.inflate(inflater,container,false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.window?.setBackgroundDrawableResource(R.drawable.bottomsheetbox)
        dialog?.window?.setGravity(Gravity.BOTTOM)



        return inflater.inflate(R.layout.fragment_recommendbottomsheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<ImageButton>(R.id.mbti_btn)?.setOnClickListener {
            initMbti()

        }

    }

    private fun initMbti(){
        mbtiSelectedListener?.onMbtiSelected()
    }

    interface OnMbtiSelectedListener{
        fun onMbtiSelected()
    }

    private var mbtiSelectedListener : OnMbtiSelectedListener? = null

    fun setOnMbtiSelectedListener(listener: OnMbtiSelectedListener){
        this.mbtiSelectedListener = listener
    }

    override fun onResume() {
        super.onResume()

        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceHeight = size.y

        // 다이얼로그의 높이를 디바이스 높이의 20%로 설정
        params?.height = (deviceHeight * 0.2).toInt()

        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }




}