package com.example.aboutme.MyprofileStorage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.aboutme.MyprofileStorage.api.ProfStorageObj
import com.example.aboutme.MyprofileStorage.api.ProfStorageResponse
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentProfilestorageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileStorageFragment : Fragment() {

    lateinit var binding: FragmentProfilestorageBinding
    private val itemList = mutableListOf<ProfileData>()
    private lateinit var rvAdapter: ProfileRVAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilestorageBinding.inflate(inflater, container, false)

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
                getProfiles()
                delay(1500)
                isLoading(false)
                swipeRefreshLayout.isRefreshing = false // 새로고침 완료 시 리프레시 아이콘 감춤
            }
        }

        // 초기 화면은 항상 최신화 상태 유지
        CoroutineScope(Dispatchers.Main).launch {
            getProfiles()
        }

        return binding.root
    }
    // 새로고침 상태 여부에 따른 shimmer effect 나타내기
    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmerLayout.startShimmer()
            binding.shimmerLayout.visibility = View.VISIBLE
            binding.profileStorageRv.visibility = View.GONE
        }
        else {
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.GONE
            binding.profileStorageRv.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        //val intent = Intent(requireContext(), ProfileStorageDetailFragment::class.java)

        //프로필 보관함 조회 api
        getProfiles()

        binding.searchBtn.setOnClickListener {
            //getSearchProfiles()
            getSearchProfiles(binding.searchTv.toString())
        }

        rvAdapter.setOnItemClickListener(object : ProfileRVAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Log.d("프로필 클릭", "$position 클릭")
                val fragment = ProfileStorageDetailFragment()

                val bundle = Bundle()
                bundle.putLong("profId", itemList[position].profile_id)
                fragment.arguments = bundle // 프래그먼트에 번들 설정

                val fragmentTransaction = parentFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.detailLayout, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        })
    }

    private fun initRecycler() {
        rvAdapter = ProfileRVAdapter(itemList) // rvAdapter 초기화
        binding.profileStorageRv.adapter = rvAdapter
        binding.profileStorageRv.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    //프로필 보관함 조회 api
    private fun getProfiles(){
        val call = ProfStorageObj.getRetrofitService.getProfStorage(6)

        call.enqueue(object : Callback<ProfStorageResponse.ResponeProfStorage> {
            override fun onResponse(
                call: Call<ProfStorageResponse.ResponeProfStorage>,
                response: Response<ProfStorageResponse.ResponeProfStorage>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()
                    if (response != null) {
                        if (response.isSuccess) {
                            // 성공했을 때
                            itemList.clear()
                            Log.d("Retrofit_Get_Success", response.toString())
                            response.result.member_profiles.forEach { memberProfile ->
                                val profileId = memberProfile.profile.profile_id
                                val isFavorite = memberProfile.favorite // favorite 값 가져오기
                                itemList.add(ProfileData(R.drawable.avatar_basic, "프로필 $profileId", profileId.toLong(),isFavorite))
                            }
                            rvAdapter.notifyDataSetChanged() // 얘가 없으면 아이템이 갱신되지 않는다! 중요
                        } else {
                            // 실패했을 때
                            Log.d("Retrofit_Get_Failed", response.toString())
                        }

                    }
                }
                else {
                    Log.d("Retrofit_Get_Failed", response.toString())
                }
            }

            override fun onFailure(
                call: Call<ProfStorageResponse.ResponeProfStorage>,
                t: Throwable
            ) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Get_Error", errorMessage)
            }
        }
        )
    }

    //프로필 보관함 검색 api
    private fun getSearchProfiles(Name : String){
    //private fun getSearchProfiles(){
        val call = ProfStorageObj.getRetrofitService.getSearchProf("아",1)

        call.enqueue(object : Callback<ProfStorageResponse.ResponseSearchProf> {
            override fun onResponse(
                call: Call<ProfStorageResponse.ResponseSearchProf>,
                response: Response<ProfStorageResponse.ResponseSearchProf>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()
                    if (response != null) {
                        if (response.isSuccess) {
                            //성공했을 때
                            itemList.clear()
                            Log.d("Retrofit_Get_Success", response.toString())
                            response.result.memberProfileList.forEach { profile ->
                                val imageResId = when {
                                    profile.image.type == "CHARACTER" && profile.image.characterType in 1..8 -> {
                                        when (profile.image.characterType) {
                                            1 -> R.drawable.prof_avater1
                                            2 -> R.drawable.prof_avater2
                                            3 -> R.drawable.prof_avater3
                                            4 -> R.drawable.prof_avater4
                                            5 -> R.drawable.prof_avater5
                                            6 -> R.drawable.prof_avater6
                                            7 -> R.drawable.prof_avater7
                                            8 -> R.drawable.prof_avater8
                                            else -> R.drawable.prof_avater9
                                        }
                                    }
                                    profile.image.type == "USER_IMAGE" -> R.drawable.prof_avater1
                                    else -> R.drawable.avatar_basic
                                }
                                val isFavorite = profile.favorite // favorite 값 가져오기
                                itemList.add(ProfileData(imageResId, profile.profileName,profile.profileId.toLong(),isFavorite))
                            }


                            rvAdapter.notifyDataSetChanged() //얘가 없으면 아이템이 갱신되지 않는다! 중요
                        } else {
                            //실패했을 때
                            Log.d("Retrofit_Get_Failed", response.toString())
                        }
                    }
                }
                else {
                    Log.d("Retrofit_Get_Failed", response.toString())
                }
            }

            override fun onFailure(
                call: Call<ProfStorageResponse.ResponseSearchProf>,
                t: Throwable
            ) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Get_Error", errorMessage)
            }
        }
        )
    }
}

