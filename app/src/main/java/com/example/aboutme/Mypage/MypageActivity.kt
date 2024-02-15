package com.example.aboutme.Mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.aboutme.Mypage.api.MyPageObj
import com.example.aboutme.Mypage.api.MyPageResponse
import com.example.aboutme.MyprofileStorage.api.ProfStorageObj
import com.example.aboutme.MyprofileStorage.api.ProfStorageResponse
import com.example.aboutme.databinding.ActivityMypageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageActivity : AppCompatActivity() {
    lateinit var binding : ActivityMypageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //일단 임의값
        getMypage(6)
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    //마이페이지 api
    private fun getMypage(memberId : Long ) {
        val call = MyPageObj.getRetrofitService.getMypage(memberId)

        call.enqueue(object : Callback<MyPageResponse.ResponseMypage> {
            override fun onResponse(
                call: Call<MyPageResponse.ResponseMypage>,
                response: Response<MyPageResponse.ResponseMypage>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()
                    if (response != null) {
                        if (response.isSuccess) {
                            // 성공했을 때
                            Log.d("Retrofit_Get_Success", response.toString())
                            binding.profNameTv.text = response.result.my_info.profile_name
                            //binding.spaceNameTv.text = response.result.my_info.space_name
                            binding.profInsight.text = response.result.insight.profile_shared_num.toString()
                            binding.spaceNameTv.text = response.result.my_info.space_name
                            binding.spaceInsight.text = response.result.insight.space_shared_num.toString()

                        }
                        //} else {
                            // 실패했을 때
                            Log.d("Retrofit_Get_Failed", response.toString())
                        }
                    }
                else {
                    Log.d("Retrofit_Get_Failed", response.toString())
                }
            }
            override fun onFailure(
                call: Call<MyPageResponse.ResponseMypage>,
                t: Throwable
            ) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Get_Error", errorMessage)
            }
        })
    }
}