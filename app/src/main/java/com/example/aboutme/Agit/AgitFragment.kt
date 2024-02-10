package com.example.aboutme.Agit

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.aboutme.RetrofitMyspaceAgit.AgitSpaceData
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyspaceAgit.ResultModel
import com.example.aboutme.RetrofitMyspaceAgit.RetrofitClient
import com.example.aboutme.RetrofitMyspaceAgit.YourResponseType
import com.example.aboutme.databinding.FragmentAgitBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgitFragment : Fragment() {

    private lateinit var binding: FragmentAgitBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var rvAdapter: AgitSpaceRVAdapter
    private val itemList = mutableListOf<AgitSpaceData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAgitBinding.inflate(inflater, container, false)

        // 스와이프 리프레쉬 레이아웃 초기화
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setColorSchemeColors(
            // 스와이프 리프레쉬 레이아웃 색깔 변경 -> 블랙, 화이트
            ContextCompat.getColor(requireContext(), R.color.black),
            ContextCompat.getColor(requireContext(), R.color.white)
        )

        // 스와이프 리프레쉬 동작
        swipeRefreshLayout.setOnRefreshListener {
            // Coroutine을 사용하여 지연 작업 수행(UI 응답없음 방지를 위한 순차적 실행)
            CoroutineScope(Dispatchers.Main).launch {
                isLoading(true)
                fetchData()
                delay(1500)
                isLoading(false)
                swipeRefreshLayout.isRefreshing = false // 새로고침 완료 시 리프레시 아이콘 감춤
            }
        }

        // 초기 화면은 항상 최신화 상태 유지
        CoroutineScope(Dispatchers.Main).launch {
            fetchData()
        }

        return binding.root
    }

    // 새로고침 상태 여부에 따른 shimmer effect 나타내기
    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmerLayout.startShimmer()
            binding.shimmerLayout.visibility = View.VISIBLE
            binding.spaceStorageRv.visibility = View.GONE
        }
        else {
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.GONE
            binding.spaceStorageRv.visibility = View.VISIBLE
        }
    }

    private fun fetchData() {
        // retrofitclient에서 통신 방법 설정(GET, POST, DELETE, PATCH)
        val call = RetrofitClient.apitest.getMySpaces("4")
//        val call_favorite = RetrofitClient.apitest.agitFavorite()

        call.enqueue(object : Callback<YourResponseType> { // API 호출(call, response 데이터 클래스 명시)
            override fun onResponse(call: Call<YourResponseType>, response: Response<YourResponseType>) {
                if (response.isSuccessful) { // API 호출 성공시
                    val result = response.body()?.result
                    result?.let {
                        updateUI(it)
                    }
                } else { // API 호출 실패시
                    handleApiError(response)
                }
            }

            // API 호출 실패시
            override fun onFailure(call: Call<YourResponseType>, t: Throwable) {
                handleApiFailure(t)
            }
        })
    }

    private fun updateUI(result: ResultModel) {
        // 서버에서 memberSpaceList 추출
        val dataList = result.memberSpaceList

        // 업데이트를 위해 기존 데이터 제거
        itemList.clear()

        // 서버에서 추출한 유저데이터리스트를 바탕으로 itemList에 하나씩 추가
        for (spaceModel in dataList) {
            val spaceIdLong = spaceModel.spaceId.toLong()
            itemList.add(AgitSpaceData(R.drawable.agit_space, "${spaceModel.nickname}'s 스페이스", spaceModel.favorite, spaceIdLong, spaceModel.characterType, spaceModel.roomType))

            // API TEST
            Log.d("API TEST", "Space ID: ${spaceModel.spaceId}")
            Log.d("API TEST", "Nickname: ${spaceModel.nickname}")
            Log.d("API TEST", "Character Type: ${spaceModel.characterType}")
            Log.d("API TEST", "Room Type: ${spaceModel.roomType}")
            Log.d("API TEST", "Favorite: ${spaceModel.favorite}")
        }

        rvAdapter = AgitSpaceRVAdapter(itemList)

        binding.spaceStorageRv.adapter = rvAdapter
        binding.spaceStorageRv.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    // API ERROR 표시
    private fun handleApiError(response: Response<YourResponseType>) {
        Log.e("handleApiError", "API 호출 실패: ${response.code()}")
    }

    // API ERROR 표시
    private fun handleApiFailure(t: Throwable) {
        Log.e("handleApiFailure", "API 호출 실패: ${t.message}")
    }
}