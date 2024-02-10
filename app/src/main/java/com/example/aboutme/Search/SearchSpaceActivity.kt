package com.example.aboutme.Search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.aboutme.MyprofileStorage.api.ProfStorageObj
import com.example.aboutme.R
import com.example.aboutme.Search.api.SearchItf
import com.example.aboutme.Search.api.SearchObj
import com.example.aboutme.Search.api.SearchResponse
import com.example.aboutme.databinding.ActivitySearchSpaceBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchSpaceActivity : AppCompatActivity() {

    lateinit var binding : ActivitySearchSpaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_space)

        binding = ActivitySearchSpaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Dialog
        binding.addBtn.setOnClickListener {
                CustomDialogSpace("내 스페이스도 공유 하시겠습니까?")
                .show(supportFragmentManager, "SpaceDialog")
        }
        //뒤로가기
        binding.backBtn.setOnClickListener {
            finish()
        }
        //제약 조건
        binding.searchBtn.setOnClickListener {
            //스페이스 검색 api
            getSearchSpace()

            if (binding.searchTv.text.toString() == "teddy") {
                binding.profBg.visibility = View.VISIBLE
                binding.addBtn.visibility = View.VISIBLE
                binding.exampleIv.visibility = View.VISIBLE
            }
            else {
                binding.profBg.visibility = View.GONE
                binding.addBtn.visibility = View.GONE
                binding.exampleIv.visibility = View.GONE
            }
        }
    }

    private fun getSearchSpace() {
        Log.d("Retrofit_Search", "보관함 조회 실행")
        val call = SearchObj.getRetrofitService.getSearchSpace("철수")

        call.enqueue(object : Callback<SearchResponse.ResponseSearchSpace> {
            override fun onResponse(
                call: Call<SearchResponse.ResponseSearchSpace>,
                response: Response<SearchResponse.ResponseSearchSpace>
            ) {
                Log.d("Retrofit_Search", response.toString())
                if (response.isSuccessful) {
                    val response = response.body()
                    Log.d("Retrofit_Search", response.toString())

                    if (response != null) {
                        if (response.isSuccess) {
                            //성공했을 때
                        } else {
                            //실패했을 때
                            Log.d("Retrofit_Search", response.message)

                        }
                    }
                }
            }
            override fun onFailure(call: Call<SearchResponse.ResponseSearchSpace>, t: Throwable) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Search", errorMessage)
            }
        })
    }
}