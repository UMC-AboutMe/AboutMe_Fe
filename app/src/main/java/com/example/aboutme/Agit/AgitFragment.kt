package com.example.aboutme.Agit

import android.app.ActivityOptions
import android.content.Context
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
import java.time.LocalDate

class AgitFragment : Fragment() {

    private lateinit var binding: FragmentAgitBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var rvAdapter: AgitSpaceRVAdapter
    private val itemList = mutableListOf<AgitSpaceData>()
    private val filteredItemList = mutableListOf<AgitSpaceData>()

    lateinit var token: String // token 변수를 추가


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAgitBinding.inflate(inflater, container, false)

        // 소셜 로그인 액티비티에서 가져온 jwttoken을 참조한다.
        fun getToken(context: Context): String? {
            val pref = context.getSharedPreferences("pref", 0)
            return pref.getString("Gtoken", null)
        }

        token = context?.let { getToken(it) }.toString() // SharedPreferences에서 토큰을 가져오는 함수를 호출하여 토큰 값을 가져옵니다.

        // 홈로고 클릭시 동작 설정
        binding.homeLogo.setOnClickListener {
            val intent = Intent(activity, bottomNavigationView::class.java)

            // 홈화면 이동시 애니메이션 효과
            val options = ActivityOptions.makeCustomAnimation(requireContext(), R.anim.zoom_in, R.anim.zoom_out)
            requireActivity().startActivity(intent, options.toBundle())
        }

        // 스와이프 리프레쉬 레이아웃 초기화
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setColorSchemeColors(
            // 스와이프 리프레쉬 레이아웃 색깔 변경 -> 블랙, 화이트
            ContextCompat.getColor(requireContext(), R.color.black),
            ContextCompat.getColor(requireContext(), R.color.white)
        )

        // 스와이프 리프레쉬 레이아웃 관련 동작 설정
        swipeRefreshLayout.setOnRefreshListener {
            // Coroutine을 사용하여 지연 작업 수행(UI 응답없음 방지를 위한 순차적 실행)
            CoroutineScope(Dispatchers.Main).launch {
                isLoading(true)
                fetchData()
                delay(1500)
                isLoading(false)
                swipeRefreshLayout.isRefreshing = false // 새로고침 완료 시 리프레시 아이콘 감추기
            }

            // 새로고침할 때 검색창의 텍스트 초기화
            binding.agitSearch.text.clear()

            // 새로고침할 때 키보드 숨기기
            val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.agitSearch.windowToken, 0)
        }

        // 초기 화면은 항상 최신화 상태 유지
        CoroutineScope(Dispatchers.Main).launch {
            fetchData()
             /* 새로고침하는 활동은 회면요소가 다 뜬 이후에 진행하므로 onViewCreated에서 설정해야할 것 같지만 그렇지 않다.
             바텀네비게이션에서 아지트로 접속하는 순간 최신화가 되어있어야하므로 화면 최신화를 하는 함수인 fetchData를 onCreateView에서 한 번만 선언해둔다. */
        }

        return binding.root
    }


    // 새로고침 상태 여부에 따른 shimmer effect 설정(예시 -> isLoading이 true일 경우 shimmerlayout 표시, 리사이클러뷰 표시X)
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

    // 로그인한 계정에 저장되어있는 멤버 리스트를 가져오기 위한 API 호출(onCreateView, onViewCreated에서 모두 사용됨)
    private fun fetchData() {
        // retrofitclient에서 통신 방법 설정(GET, POST, DELETE, PATCH)
        // SharedPreferences에서 토큰을 가져오는 함수
        val call = token.let { RetrofitClient.apitest.getMySpaces(it) }
        Log.d("토큰아지트", token)
//        val call_favorite = RetrofitClient.apitest.agitFavorite()

        call.enqueue(object : Callback<YourResponseType> { // API 호출(call, response 데이터 클래스 명시)
            override fun onResponse(call: Call<YourResponseType>, response: Response<YourResponseType>) {
                if (response.isSuccessful) { // 성공시
                    val result = response.body()?.result
                    result?.let {
                        updateUI(it)
                    }
                    Log.d("성공?", "$result")
                } else { // 실패시
                    handleApiError(response)
                }
            }

            // API 호출 실패시
            override fun onFailure(call: Call<YourResponseType>, t: Throwable) {
                handleApiFailure(t)
            }
        })
    }

    // API 호출 후 가져온 멤버 리스트 정보들을 바탕으로 UI를 최신화
    private fun updateUI(result: ResultModel) {
        // 서버에서 memberSpaceList 추출
        val dataList = result.memberSpaceList

        Log.d("업데이트", "$dataList")

        // 업데이트를 위해 기존 데이터 제거
        itemList.clear()

        // 서버에서 추출한 유저데이터리스트를 바탕으로 itemList에 하나씩 추가
        for (spaceModel in dataList) {
            val spaceIdLong = spaceModel.spaceId.toLong()

            itemList.add(AgitSpaceData("${spaceModel.nickname}'s 스페이스", spaceModel.favorite, spaceIdLong, spaceModel.characterType, spaceModel.roomType))

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
        rvAdapter = AgitSpaceRVAdapter(requireContext().resources, itemList)

        // 실제 리사이클러뷰에 해당하는 spaceStorageRv에 새롭게 반영한 itemList 어댑터 연결
        binding.spaceStorageRv.adapter = rvAdapter

        // 리사이클러뷰 표시 형태 설정
        binding.spaceStorageRv.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    // API ERROR 표시
    private fun handleApiError(response: Response<YourResponseType>) {
        Log.e("handleApiError", "API 호출 실패: ${response.code()}")
    }
    private fun handleApiFailure(t: Throwable) {
        Log.e("handleApiFailure", "API 호출 실패: ${t.message}")
    }


    // onViewCreate에서 아지트 최신화를 이미 했으므로 onViewCreated에서는 각종 검색 관련 기능 구현
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 기존의 리사이클러뷰 아이템 목록을 복사하여 filteredItemList에 저장
        filteredItemList.addAll(itemList)

        // 검색창 agitSearch에 텍스트가 한 글자씩 입력될 때마다 필터링이 자동으로 되어야 하므로 텍스트 입력을 감지하는 Listener 설정
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
        
        // 검색창 agitSearch에서 키보드상으로 완료 버튼을 누를 경우 실제 검색버튼을 누른 것과 같은 효과
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
        rvAdapter = AgitSpaceRVAdapter(requireContext().resources, filteredItemList)

        // 실제 리사이클러뷰에 해당하는 spaceStorageRv에 새롭게 반영한 itemList 어댑터 연결
        binding.spaceStorageRv.adapter = rvAdapter

        // 리사이클러뷰 표시 형태 설정
        binding.spaceStorageRv.layoutManager = GridLayoutManager(requireContext(), 2)
    }
}