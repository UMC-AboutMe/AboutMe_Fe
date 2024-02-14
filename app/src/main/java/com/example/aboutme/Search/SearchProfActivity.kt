package com.example.aboutme.Search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.aboutme.R
import com.example.aboutme.Search.api.SearchItf
import com.example.aboutme.Search.api.SearchObj
import com.example.aboutme.Search.api.SearchResponse
import com.example.aboutme.databinding.ActivitySearchProfBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchProfActivity : AppCompatActivity() {

    lateinit var binding : ActivitySearchProfBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchProfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뒤로 가기
        binding.backBtn.setOnClickListener {
            finish()
        }
        //제약 조건
        binding.searchBtn.setOnClickListener {
            //val number = binding.searchTv.text.toString().toInt()
            getSearchProf(12026)
        }
    }

    //상대방 마이프로필 내 보관함에 추가하기
    private fun postProfStorage(list : Int){
        Log.d("Retrofit_Add","보관함 추가 실행")
        val requestStoreProf = SearchResponse.RequestStoreProf(listOf(list))
        val call = SearchObj.getRetrofitService.postProfStorage(6, requestStoreProf)

        call.enqueue(object : Callback<SearchResponse.ResponseStoreProf> {
            override fun onResponse(
                call: Call<SearchResponse.ResponseStoreProf>,
                response: Response<SearchResponse.ResponseStoreProf>
            ) {
                Log.d("Retrofit_Add", response.toString())
                if (response.isSuccessful) {
                    val response = response.body()
                    Log.d("Retrofit_Add", response.toString())

                    if (response != null) {
                        if (response.isSuccess) {
                            //성공했을 때
                        } else {
                            //실패했을 때
                            Log.d("Retrofit_Add", response.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<SearchResponse.ResponseStoreProf>, t: Throwable) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Add", errorMessage)
            }
        })
    }
    //마이프로필 검색
    private fun getSearchProf(Number : Int ) {
        val call = SearchObj.getRetrofitService.getSearchProf(Number)
        call.enqueue(object : Callback<SearchResponse.ResponseSearchProf> {
            override fun onResponse(
                call: Call<SearchResponse.ResponseSearchProf>,
                response: Response<SearchResponse.ResponseSearchProf>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()
                    Log.d("Retrofit_Search", response.toString())
                    if (response != null) {
                        if (response.isSuccess) {
                            //성공했을 때
                            binding.profView.visibility = View.VISIBLE
                            Log.d("Retrofit_Search", response.toString())

                            val imageResId = when {
                                response.result.profile_image.type == "CHARACTER" && response.result.profile_image.characterType in 1..8 -> {
                                    when (response.result.profile_image.characterType) {
                                        1 -> R.drawable.prof_avater1.toString()
                                        2 -> R.drawable.prof_avater2.toString()
                                        3 -> R.drawable.prof_avater3.toString()
                                        4 -> R.drawable.prof_avater4.toString()
                                        5 -> R.drawable.prof_avater5.toString()
                                        6 -> R.drawable.prof_avater6.toString()
                                        7 -> R.drawable.prof_avater7.toString()
                                        8 -> R.drawable.prof_avater8.toString()
                                        else -> R.drawable.prof_avater9.toString()
                                    }
                                }
                                response.result.profile_image.type == "USER_IMAGE" -> response.result.profile_image.profile_image_url
                                    ?: ""
                                else -> R.drawable.avatar_basic.toString()
                        }
                            if (imageResId.startsWith("http")) {
                                // URL인 경우 Glide를 사용하여 이미지 로드 및 표시
                                Glide.with(this@SearchProfActivity)
                                    .load(imageResId)
                                    .into(binding.imageView2)
                            } else {
                                // 리소스 아이디인 경우 setImageResource() 메서드를 사용하여 이미지 설정
                                binding.imageView2.setImageResource(imageResId.toInt())
                            }
                            response.result.front_features.forEach { profile
                                ->
                                if (profile.key == "name" )
                                    binding.profName.text = profile.value
                                else
                                    binding.profNum.text = profile.value
                            }

                            //Dialog
                            binding.addBtn.setOnClickListener {
                                Toast.makeText(this@SearchProfActivity, "보관함에 저장되었습니다.", Toast.LENGTH_SHORT).show()

                                //보관함 추가하기 api
                                postProfStorage(Number)
                                //상대방 마이프로필 내 보관함에 추가하기
                                CustomDialog("내 프로필도 공유 하시겠습니까?")
                                    .show(supportFragmentManager, "ProfDialog")
                            }
                        } else {
                            //실패했을 때
                            Log.d("Retrofit_Search", response.message)
                            binding.profView.visibility = View.GONE
                        }
                    }
                } else {
                    Log.d("Retrofit_Search_Failed", response.toString())
                    Toast.makeText(this@SearchProfActivity, "존재하지 않는 프로필입니다.", Toast.LENGTH_SHORT).show()
                    binding.profView.visibility = View.GONE
                }
            }
            override fun onFailure(call: Call<SearchResponse.ResponseSearchProf>, t: Throwable) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Search", errorMessage)
            }
        })
    }
}