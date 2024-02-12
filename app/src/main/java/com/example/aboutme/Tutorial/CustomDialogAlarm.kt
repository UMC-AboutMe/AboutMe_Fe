package com.example.aboutme.Tutorial

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import androidx.fragment.app.DialogFragment
import com.example.aboutme.R
import com.example.aboutme.Search.CustomDialogProf
import com.example.aboutme.databinding.ActivityCustomDialogAlarmBinding
import com.example.aboutme.databinding.ActivityCustomDialogBinding

class CustomDialogAlarm() : DialogFragment() {
    private var _binding: ActivityCustomDialogAlarmBinding? = null
    private val binding get() = _binding!!
    private lateinit var notificationHelper: NotificationHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityCustomDialogAlarmBinding.inflate(inflater, container, false)
        val view = binding.root
        // 레이아웃 배경을 투명하게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 취소 버튼
        binding.noBtn.setOnClickListener {
            dismiss()
        }
        // 확인 버튼
        binding.yesBtn.setOnClickListener {
            dismiss()

            //상단바 알람
            notificationHelper = NotificationHelper(requireContext())
            showNotification("AboutMe", "text알람입니다")
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
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    //상단바 알림
    private fun showNotification(title: String, message: String) {
        val nb: NotificationCompat.Builder =
            notificationHelper.getChannelNotification(title, message)

        notificationHelper.getManager().notify(1, nb.build())
    }
}