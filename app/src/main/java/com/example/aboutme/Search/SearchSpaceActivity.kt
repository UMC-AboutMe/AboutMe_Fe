package com.example.aboutme.Search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.aboutme.R
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

//        //Dialog
//        binding.addBtn.setOnClickListener {
//            storeSpace()
//                CustomDialogSpace("내 스페이스도 공유 하시겠습니까?")
//                .show(supportFragmentManager, "SpaceDialog")
//        }
        //뒤로가기
        binding.backBtn.setOnClickListener {
            finish()
        }
        //제약 조건
        binding.searchBtn.setOnClickListener {
            //스페이스 검색 api
            getSearchSpace()
        }
    }

    private fun getSearchSpace() {
        Log.d("Retrofit_Search", "스페이스 검색 실행")
        val spaceName = binding.searchTv.toString()
        //val call = SearchObj.getRetrofitService.getSearchSpace(spaceName)

        //짱구는 임시값
        val call = SearchObj.getRetrofitService.getSearchSpace("짱구")

        call.enqueue(object : Callback<SearchResponse.ResponseSearchSpace> {
            override fun onResponse(
                call: Call<SearchResponse.ResponseSearchSpace>,
                response: Response<SearchResponse.ResponseSearchSpace>
            ) {
                if (response.isSuccessful) { // HTTP 응답 코드가 200에서 300 사이인지 확인
                    val response = response.body()
                    if (response != null) {
                        if (response.isSuccess) {
                            //성공했을 때
                            Log.d("Retrofit_Search_Success", response.toString())
                            binding.spaceView.visibility = View.VISIBLE
                            binding.spaceName.text = "${response.result.nickname}'s 스페이스"
                            roomType(response.result.roomType)
                            avatarType(response.result.characterType)

                            //Dialog
                            binding.addBtn.setOnClickListener {
                                storeSpace(response.result.spaceId)
                                CustomDialogSpace("내 스페이스도 공유 하시겠습니까?")
                                    .show(supportFragmentManager, "SpaceDialog")
                            }
                        } else {
                            //실패했을 때
                            Log.d("Retrofit_Search_Failed", response.message)
                            binding.spaceView.visibility = View.GONE
                        }
                    }
                }
                else {
                    Log.d("Retrofit_Search_Failed", response.toString())
                    Toast.makeText(this@SearchSpaceActivity, "존재하지 않는 스페이스입니다.", Toast.LENGTH_SHORT).show()
                    binding.spaceView.visibility = View.GONE
                }
            }
            override fun onFailure(call: Call<SearchResponse.ResponseSearchSpace>, t: Throwable) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Search_Error", errorMessage)
                binding.spaceView.visibility = View.GONE
            }
        })
    }

    fun roomType(Type : Int) {
        when(Type) {
            1-> binding.roomTypeBg.setImageResource(R.drawable.search_room1)
            2-> binding.roomTypeBg.setImageResource(R.drawable.search_room2)
            3-> binding.roomTypeBg.setImageResource(R.drawable.search_room3)
            4-> binding.roomTypeBg.setImageResource(R.drawable.search_room4)
        }
    }
    fun avatarType(Type : Int) {
        when(Type) {
            1-> binding.charTypeBg.setImageResource(R.drawable.step2_avatar_1)
            2-> binding.charTypeBg.setImageResource(R.drawable.step2_avatar_2)
            3-> binding.charTypeBg.setImageResource(R.drawable.step2_avatar_3)
            4-> binding.charTypeBg.setImageResource(R.drawable.step2_avatar_4)
            5-> binding.charTypeBg.setImageResource(R.drawable.step2_avatar_5)
            6-> binding.charTypeBg.setImageResource(R.drawable.step2_avatar_6)
            7-> binding.charTypeBg.setImageResource(R.drawable.step2_avatar_7)
            8-> binding.charTypeBg.setImageResource(R.drawable.step2_avatar_8)
            9-> binding.charTypeBg.setImageResource(R.drawable.step2_avatar_9)
        }
    }

    private fun storeSpace(spaceId : Long ) {
        Log.d("Retrofit_Search", "스페이스 저장 실행")

        //memberID는 임시값
        val call = SearchObj.getRetrofitService.postSpaceStorage(spaceId,6)

        call.enqueue(object : Callback<SearchResponse.ResponseSpaceStorage> {
            override fun onResponse(
                call: Call<SearchResponse.ResponseSpaceStorage>,
                response: Response<SearchResponse.ResponseSpaceStorage>
            ) {
                if (response.isSuccessful) { // HTTP 응답 코드가 200에서 300 사이인지 확인
                    val response = response.body()
                    if (response != null) {
                        if (response.isSuccess) {
                            //성공했을 때
                            Log.d("Retrofit_Storage_Success", response.toString())
                        } else {
                            //실패했을 때
                            Log.d("Retrofit_Storage_Failed", response.message)
                        }
                    }
                }
                else {
                    Log.d("Retrofit_Storage_Failed", response.toString())
                }
            }
            override fun onFailure(call: Call<SearchResponse.ResponseSpaceStorage>, t: Throwable) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Storage_Error", errorMessage)
            }
        })
    }
}