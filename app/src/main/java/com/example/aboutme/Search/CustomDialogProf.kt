package com.example.aboutme.Search

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
import com.example.aboutme.databinding.ActivityCustomDialogProfBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomDialogProf : DialogFragment() {
    private var _binding: ActivityCustomDialogProfBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityCustomDialogProfBinding.inflate(inflater, container, false)
        val view = binding.root
        // 레이아웃 배경을 투명하게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 확인 버튼
        binding.yesBtn.setOnClickListener {
            dismiss()
        }
        // 다이얼로그를 하단으로 조정
        dialog?.window?.setGravity(Gravity.BOTTOM)

        //다이얼로그 외부 클릭 시 종료 x
        dialog?.setCanceledOnTouchOutside(false)

        getProfiles()
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
    //마이프로필 목록 조회
    private fun getProfiles(){
    Log.d("Retrofit_Add","프로필 공유 실행")
    val call = SearchObj.getRetrofitService.getProfileList(6)

    call.enqueue(object : Callback<SearchResponse.ResponseGetProfiles> {
        override fun onResponse(
            call: Call<SearchResponse.ResponseGetProfiles>,
            response: Response<SearchResponse.ResponseGetProfiles>
        ) {
            Log.d("Retrofit_Add",response.toString())
            if(response.isSuccessful){
                val response=response.body()
                Log.d("Retrofit_Add",response.toString())
                if(response != null) {
                    if(response.isSuccess){
                        //성공했을 때
                    }
                    else{
                        //실패했을 때
                        Log.d("Retrofit_Add",response.message)

                    }
                }
            }
        }
        override fun onFailure(call: Call<SearchResponse.ResponseGetProfiles>, t:Throwable) {
            val errorMessage = "Call Failed:  ${t.message}"
            Log.d("Retrofit_Add",errorMessage)
        }
    }
    )
}