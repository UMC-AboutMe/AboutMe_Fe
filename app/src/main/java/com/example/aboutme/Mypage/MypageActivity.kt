package com.example.aboutme.Mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.aboutme.MainActivity
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
        binding.button.setOnClickListener {

            //아직 토큰 연결이 안되서 일단 주석처리
            //deleteUser(token)
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
                    //Log.d("Retrofit_Get_Failed", response.toString())
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e(
                        "Retrofit_Get_Failed",
                        "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody"
                    )
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
    //회원 탈퇴 api
    private fun deleteUser(memberId: Long) {
        val call = MyPageObj.getRetrofitService.deleteUser(memberId)

        call.enqueue(object : Callback<MyPageResponse.ResponseDeleteUser> {
            override fun onResponse(
                call: Call<MyPageResponse.ResponseDeleteUser>,
                response: Response<MyPageResponse.ResponseDeleteUser>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()
                    if (response != null) {
                        if (response.isSuccess) {
                            // 성공했을 때
                            Log.d("Retrofit_Delete_Success", response.toString())

                            Toast.makeText(this@MypageActivity, "탈퇴가 정상적으로 처리되었습니다.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@MypageActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }else {
                        // 실패했을 때
                        Log.d("Retrofit_Delete_Failed", response.toString())
                    }
                } else {
                    //Log.d("Retrofit_Get_Failed", response.toString())
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e(
                        "Retrofit_Delete_Failed",
                        "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody"
                    )
                }
            }

            override fun onFailure(
                call: Call<MyPageResponse.ResponseDeleteUser>,
                t: Throwable
            ) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Delete_Error", errorMessage)
            }
        })
    }
}