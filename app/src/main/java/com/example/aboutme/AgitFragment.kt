package com.example.aboutme

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.aboutme.databinding.FragmentAgitBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgitFragment : Fragment() {

    lateinit var binding : FragmentAgitBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentAgitBinding.inflate(inflater,container,false)

        setAgitFragment()

        return binding.root
    }

    private fun setAgitFragment() {
        // Retrofit을 사용하여 API 호출
        val call = RetrofitClient.apitest.getMySpaces("1")
        val call_add = RetrofitClient.apitest.addspace("1")
        val itemList = mutableListOf<AgitSpaceData>()

        // 리사이클러뷰에 예시 데이터 생성
        itemList.add(AgitSpaceData(R.drawable.agit_space, "00's 스페이스"))
        itemList.add(AgitSpaceData(R.drawable.agit_space, "00's 스페이스"))
        itemList.add(AgitSpaceData(R.drawable.agit_space, "00's 스페이스"))
        itemList.add(AgitSpaceData(R.drawable.agit_space, "00's 스페이스"))

        // API 호출로 서버에 저장되어있는 사용자들의 스페이스 정보 추출
        call_add.enqueue(object : Callback<YourResponseType> {
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

            private fun updateUI(result: ResultModelAdd?) {
                // TODO: 결과를 화면에 표시하는 작업 수행

                // Smart cast 오류를 해결하기 위해 명시적으로 null 체크
                result.let {
                    // result가 null이 아닌 경우에만 이 블록이 실행됨
                    // TODO: 로그 추가
                    Log.d("MySpaceStep1Activity", "API 호출 성공: $it")
                    Log.d("API TEST", "Result: $result")

//                    val dataList = result?.memberSpaceList
//
//                    if (dataList != null) {
//                        for (spaceModel in dataList) {
//                            // AgitSpaceData에 API에서 가져온 nickname을 설정
//                            itemList.add(AgitSpaceData(R.drawable.agit_space, "${spaceModel.nickname}'s 스페이스"))
//
//                            // API 호출 테스트 코드
//                            Log.d("API TEST", "Space ID: ${spaceModel.space_id}")
//                            Log.d("API TEST", "Nickname: ${spaceModel.nickname}")
//                            Log.d("API TEST", "Character Type: ${spaceModel.characterType}")
//                            Log.d("API TEST", "Room Type: ${spaceModel.roomType}")
//                            Log.d("API TEST", "Favorite: ${spaceModel.favorite}")
//                        }
//
//                        val rvadapter = AgitSpaceRVAdapter(itemList)
//
//                        binding.spaceStorageRv.adapter = rvadapter
//
//                        binding.spaceStorageRv.layoutManager = GridLayoutManager(requireContext(), 2)
//                    }

                    val spaceid = result?.spaceId
                    Log.d("spaceID", "spaceid: $spaceid")
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
