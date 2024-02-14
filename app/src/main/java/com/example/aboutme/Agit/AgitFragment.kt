package com.example.aboutme.Agit

import android.app.ActivityOptions
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.aboutme.RetrofitMyspaceAgit.AgitSpaceData
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyspaceAgit.ResultModel
import com.example.aboutme.RetrofitMyspaceAgit.RetrofitClient
import com.example.aboutme.RetrofitMyspaceAgit.YourResponseType
import com.example.aboutme.bottomNavigationView
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
    private val filteredItemList = mutableListOf<AgitSpaceData>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAgitBinding.inflate(inflater, container, false)

        binding.homeLogo.setOnClickListener {
            // 홈화면 이동시 애니메이션 효과
            val intent = Intent(activity, bottomNavigationView::class.java)
            val options = ActivityOptions.makeCustomAnimation(requireContext(), R.anim.fade_in, R.anim.fade_out)
            requireActivity().startActivity(intent, options.toBundle())
        }

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

    // API 호출
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

    // API 호출 후 가져온 결과값을 바탕으로 UI를 최신화
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

        // 북마크가 표시되어있는 아이템부터 정렬
        itemList.sortByDescending { it.isBookmarked }

        // 아이템들이 새롭게 추가된 itemList로 설정
        rvAdapter = AgitSpaceRVAdapter(itemList)

        // 실제 리사이클러뷰에 해당하는 spaceStorageRv에 새롭게 반영한 itemList 어댑터 연결
        binding.spaceStorageRv.adapter = rvAdapter

        // 리사이클러뷰 표시 형태 설정
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


    // 아지트 최신화가 되어있는 상태에서 검색을 하므로 onViewCreated에서 필터링을 진행하는 함수 설정
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 기존의 리사이클러뷰 아이템 목록을 복사하여 filteredItemList에 저장
        filteredItemList.addAll(itemList)

        // 검색창 EditText에 텍스트가 입력될 때마다 호출되는 TextWatcher 설정
        binding.agitSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력 전
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 바뀔 때마다 호출되는 메서드
                val filterText = s.toString().trim()
                filterItems(filterText)
            }

            override fun afterTextChanged(s: Editable?) {
                // 입력 후
            }
        })
        
        // 검색창 edittext에서 키보드상으로 완료 버튼을 누를 경우 검색버튼을 누른 것과 같은 효과
        binding.agitSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // 검색버튼을 누를 경우 발동되는 검색 효과
                // 사실상 검색 효과에 해당하는 필터링이 텍스트를 입력할 때마다 발동되므로 적을 필요가 없음

                // 키보드 숨기기
                val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.agitSearch.windowToken, 0)

                return@setOnEditorActionListener true
            }
            false
        }

        // 검색창 검색 버튼 클릭시 키보드 숨김처리
        binding.spaceStorageSearchIcon.setOnClickListener {
            // 키보드 숨기기
            val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.agitSearch.windowToken, 0)
        }

        // 화면 내 빈 공간 클릭시 키보드 숨김처리
        binding.fragmentContainer.setOnClickListener {
            val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        // 화면 내 빈 공간 클릭시 키보드 숨김처리
        binding.swipeRefreshLayout.setOnClickListener {
            val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    // 실시간 필터링 효과
    private fun filterItems(filterText: String) {
        // filteredItemList을 초기화하여 이전 필터링 결과를 제거
        filteredItemList.clear()

        // 검색어가 비어있으면 전체 아이템을 보여줌
        if (filterText.isEmpty()) {
            filteredItemList.addAll(itemList)
        } else {
            // onCreatView에서 처음 생성한 itemList를 순회하면서 검색어와 일치하는 아이템만 filteredItemList에 추가
            for (item in itemList) {
                if (item.spaceName.contains(filterText, ignoreCase = true)) {
                    filteredItemList.add(item)
                }
            }
        }

        // 아이템들이 새롭게 추가된 filteredItemList로 설정
        rvAdapter = AgitSpaceRVAdapter(filteredItemList)

        // 실제 리사이클러뷰에 해당하는 spaceStorageRv에 새롭게 반영한 itemList 어댑터 연결
        binding.spaceStorageRv.adapter = rvAdapter

        // 리사이클러뷰 표시 형태 설정
        binding.spaceStorageRv.layoutManager = GridLayoutManager(requireContext(), 2)
    }
}