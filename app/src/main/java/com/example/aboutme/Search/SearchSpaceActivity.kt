package com.example.aboutme.Search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
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
            val keyword = binding.searchTv.text.toString()
            getSearchSpace(keyword)
        }
        // 검색창 edittext에서 키보드상으로 완료 버튼을 누를 경우 검색버튼을 누른 것과 같은 효과
        binding.searchTv.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // 검색버튼을 누를 경우 발동되는 검색 효과
                // 사실상 검색 효과에 해당하는 필터링이 텍스트를 입력할 때마다 발동되므로 적을 필요가 없음
                // 키보드 숨기기
                val inputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.searchTv.windowToken, 0)

                return@setOnEditorActionListener true
            }
            false
        }
        // 화면 내 빈 공간 클릭시 키보드 숨김처리
        binding.searchProf.setOnClickListener {
            val inputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }
    }

    private fun getSearchSpace(keyword:String) {
        Log.d("Retrofit_Search", "스페이스 검색 실행")
        val call = SearchObj.getRetrofitService.getSearchSpace(keyword)

        //짱구는 임시값
        //val call = SearchObj.getRetrofitService.getSearchSpace("짱구")

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
                                Toast.makeText(this@SearchSpaceActivity, "스페이스가 아지트에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                                storeSpace(response.result.spaceId)
                                //서버로부터 멤버아이디를 받아와야함.
                                //CustomDialogSpace("내 스페이스도 공유 하시겠습니까?",memberId)
                                //일단 임의값 1로 설정
                                CustomDialogSpace("내 스페이스도 공유 하시겠습니까?",1)
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
                    //Log.d("Retrofit_Search_Failed", response.toString())
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e(
                        "Retrofit_Get_Failed",
                        "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody"
                    )
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
    //스페이스 저장 api
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