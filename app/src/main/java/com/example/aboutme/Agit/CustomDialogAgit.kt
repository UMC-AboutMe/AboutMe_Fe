package com.example.aboutme.Agit

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.aboutme.RetrofitMyspaceAgit.AgitMemberDelete
import com.example.aboutme.RetrofitMyspaceAgit.RetrofitClient
import com.example.aboutme.databinding.ActivityCustomDialogAgitBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 커스텀 다이얼로그
class CustomDialogAgit(val content: String, private val spaceId: Long) : DialogFragment() {
    private var _binding: ActivityCustomDialogAgitBinding? = null
    private val binding get() = _binding!!

    private lateinit var token: String // token 변수를 추가

    private fun getToken(context: Context): String? {
        val pref = context.getSharedPreferences("pref", 0)
        return pref.getString("Gtoken", null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityCustomDialogAgitBinding.inflate(inflater, container, false)
        val view = binding.root

        token = context?.let { getToken(it) }.toString() // SharedPreferences에서 토큰을 가져오는 함수를 호출하여 토큰 값을 가져옵니다.
        Log.d("커스텀다이얼로그토큰", token)

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
            val call = RetrofitClient.apitest.deleteAgitMember(spaceId, token)

            call.enqueue(object : Callback<AgitMemberDelete> { // API 호출(call, response 데이터 클래스 명시)
                override fun onResponse(call: Call<AgitMemberDelete>, response: Response<AgitMemberDelete>) {
                    if (response.isSuccessful) { // API 호출 성공시
                        Log.d("delete", "삭제 완료")
                    } else { // API 호출 실패시
                        handleApiError(response)
                    }
                }

                // API 호출 실패시
                override fun onFailure(call: Call<AgitMemberDelete>, t: Throwable) {
                    handleApiFailure(t)
                }
            })
        }
        // 다이얼로그를 중앙으로 조정
        dialog?.window?.setGravity(Gravity.CENTER)

        return view
    }

    private fun handleApiError(response: Response<AgitMemberDelete>) {
        Log.e("handleApiError", "API 호출 실패: ${response.code()}")
    }

    // API ERROR 표시
    private fun handleApiFailure(t: Throwable) {
        Log.e("handleApiFailure", "API 호출 실패: ${t.message}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout( WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT ) }
}