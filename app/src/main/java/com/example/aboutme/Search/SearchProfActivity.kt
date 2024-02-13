package com.example.aboutme.Search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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

        //Dialog
        binding.addBtn.setOnClickListener {
            //보관함 추가하기 api
            postProfStorage()

            //상대방 마이프로필 내 보관함에 추가하기
            //CustomDialog("내 프로필도 공유 하시겠습니까?")
            //    .show(supportFragmentManager, "ProfDialog")
        }
        //뒤로 가기
        binding.backBtn.setOnClickListener {
            finish()
        }
        //제약 조건
        binding.searchBtn.setOnClickListener {
            if (binding.searchTv.text.toString() == "123456") {
                binding.profView.visibility = View.VISIBLE
            }
            else {
                binding.profView.visibility = View.GONE
            }
        }
    }

    //상대방 마이프로필 내 보관함에 추가하기
    private fun postProfStorage(){
        Log.d("Retrofit_Add","보관함 추가 실행")
        val requestStoreProf = SearchResponse.RequestStoreProf(listOf(87694))
        val call = SearchObj.getRetrofitService.postProfStorage(6, requestStoreProf)

        call.enqueue(object : Callback<SearchResponse.ResponseStoreProf> {
            override fun onResponse(
                call: Call<SearchResponse.ResponseStoreProf>, response: Response<SearchResponse.ResponseStoreProf>
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
            override fun onFailure(call:Call<SearchResponse.ResponseStoreProf>, t:Throwable) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Add",errorMessage)
            }
        })
    }

    //마이프로필 상대방에게 공유
    private fun postShareProf(){
        Log.d("Retrofit_Add","프로필 공유 실행")
        val requestShareProf = SearchResponse.RequestShareProf(6,listOf(109349))
        val call = SearchObj.getRetrofitService.postShareProf(6, requestShareProf)

        call.enqueue(object : Callback<SearchResponse.ResponseShareProf> {
            override fun onResponse(
                call: Call<SearchResponse.ResponseShareProf>,
                response: Response<SearchResponse.ResponseShareProf>
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
            override fun onFailure(call:Call<SearchResponse.ResponseShareProf>, t:Throwable) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Add",errorMessage)
            }
        }
        )
    }
}