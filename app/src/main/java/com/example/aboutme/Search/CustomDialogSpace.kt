package com.example.aboutme.Search

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.aboutme.databinding.ActivityCustomDialogSpaceBinding

// 커스텀 다이얼로그
class CustomDialogSpace(val content: String) : DialogFragment() {
    private var _binding: ActivityCustomDialogSpaceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityCustomDialogSpaceBinding.inflate(inflater, container, false)
        val view = binding.root
        // 레이아웃 배경을 투명하게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 제목, 내용 설정
        binding.dialogTv.text = content

        // 취소 버튼
        binding.noBtn.setOnClickListener {
            dismiss()
        }
        // 확인 버튼
        binding.yesBtn.setOnClickListener {
            dismiss()
        }
        // 다이얼로그를 하단으로 조정
        dialog?.window?.setGravity(Gravity.BOTTOM)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout( WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT ) }
}