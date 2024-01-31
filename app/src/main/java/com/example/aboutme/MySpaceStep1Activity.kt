package com.example.aboutme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.aboutme.databinding.ActivityMyspacestep1Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MySpaceStep1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityMyspacestep1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyspacestep1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextIbStep1.setOnClickListener {
            // EditText에서 텍스트 가져오기
            val inputText = binding.nickname.text.toString()

            // API 호출
            callYourApi()
        }
    }

    private fun callYourApi() {
        // Retrofit을 사용하여 API 호출
        val call = RetrofitClient.apitest.getMySpaces("1")

        call.enqueue(object : Callback<YourResponseType> {
            override fun onResponse(call: Call<YourResponseType>, response: Response<YourResponseType>) {
                if (response.isSuccessful) {
                    val result = response.body()?.result

                    // API 응답 결과를 처리하는 작업 수행
                    result?.let {
                        updateUI(it)
                    }
                } else {
                    // API 오류 처리
                    handleApiError(response)
                }
            }

            private fun updateUI(result: ResultModel?) {
                // TODO: 결과를 화면에 표시하는 작업 수행

                // Smart cast 오류를 해결하기 위해 명시적으로 null 체크
                result.let {
                    // result가 null이 아닌 경우에만 이 블록이 실행됨
                    // TODO: 로그 추가
                    Log.d("MySpaceStep1Activity", "API 호출 성공: $it")
                    Log.d("API TEST", "Result: $result")

                    val dataList = result?.memberSpaceList

                    if (dataList != null) {
                        for (spaceModel in dataList) {
                            Log.d("API TEST", "Space ID: ${spaceModel.space_id}")
                            Log.d("API TEST", "Nickname: ${spaceModel.nickname}")
                            Log.d("API TEST", "Character Type: ${spaceModel.characterType}")
                            Log.d("API TEST", "Room Type: ${spaceModel.roomType}")
                            Log.d("API TEST", "Favorite: ${spaceModel.favorite}")
                        }
                    }
                }
            }

            private fun handleApiError(response: Response<YourResponseType>) {
                // TODO: API 오류를 처리하는 작업 수행

                // TODO: 로그 추가
                Log.e("handleApiError", "API 호출 실패: ${response.code()}")
            }

            override fun onFailure(call: Call<YourResponseType>, t: Throwable) {
                // API 호출 실패 처리
                handleApiFailure(t)
            }

            private fun handleApiFailure(t: Throwable) {
                // TODO: API 호출 실패를 처리하는 작업 수행

                // TODO: 로그 추가
                Log.e("handleApiFailure", "API 호출 실패: ${t.message}")
            }
        })
    }
}
