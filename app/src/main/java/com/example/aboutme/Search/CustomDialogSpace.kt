package com.example.aboutme.Search

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
import com.example.aboutme.Search.api.SearchObj
import com.example.aboutme.Search.api.SearchResponse
import com.example.aboutme.databinding.ActivityCustomDialogSpaceBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 커스텀 다이얼로그
class CustomDialogSpace(val content: String,val memberId : Long) : DialogFragment() {
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
            shareSpace(memberId)
            dismiss()
        }
        // 다이얼로그를 하단으로 조정
        dialog?.window?.setGravity(Gravity.BOTTOM)

        //다이얼로그 외부 클릭 시 종료 x
        dialog?.setCanceledOnTouchOutside(false)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout( WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT )
    }

        fun getToken(context: Context): String {
        val pref = context.getSharedPreferences("pref", 0)
        val token: String? = pref.getString("token", null)
        Log.d("token", token ?: "null")
        return token ?: ""
    }
    //스페이스 공유 api
    private fun shareSpace(memberId : Long ) {
        Log.d("Retrofit_Share", "스페이스 공유 실행")

        val memberId = SearchResponse.RequestShareSpace(memberId)
        val token = getToken(requireContext())
        val call = SearchObj.getRetrofitService.postShareSpace(token, memberId)

        call.enqueue(object : Callback<SearchResponse.ResponseShareSpace> {
            override fun onResponse(
                call: Call<SearchResponse.ResponseShareSpace>,
                response: Response<SearchResponse.ResponseShareSpace>
            ) {
                if (response.isSuccessful) { // HTTP 응답 코드가 200에서 300 사이인지 확인
                    val response = response.body()
                    if (response != null) {
                        if (response.isSuccess) {
                            //성공했을 때
                            Log.d("Retrofit_Share_Success", response.toString())
                        } else {
                            //실패했을 때
                            Log.d("Retrofit_Share_Failed", response.toString())
                        }
                    }
                }
                else {
                    //Log.d("Retrofit_Share_Failed", response.toString())
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e(
                        "Retrofit_Share_Failed",
                        "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody"
                    )
                }
            }
            override fun onFailure(call: Call<SearchResponse.ResponseShareSpace>, t: Throwable) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Share_Error", errorMessage)
            }
        })
    }
}