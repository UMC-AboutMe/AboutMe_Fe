package com.example.aboutme.MyprofileStorage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.aboutme.MyprofileStorage.api.ProfStorageObj
import com.example.aboutme.MyprofileStorage.api.ProfStorageResponse
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentProfilestorageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileStorageFragment : Fragment() {

    lateinit var binding: FragmentProfilestorageBinding
    private val itemList = mutableListOf<ProfileData>()
    private lateinit var rvAdapter: ProfileRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilestorageBinding.inflate(inflater, container, false)
        return binding.root
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
                //position인덱스의 리스트 가져오기
                //itemList[position]
                //startActivity(intent)
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                val fragment = ProfileStorageDetailFragment()
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
                            //성공했을 때
                            itemList.clear()
                            Log.d("Retrofit_Get_Success", response.toString())
                            response.result.member_profiles.forEach { memberProfile ->
                                val profileId = memberProfile.profile.profile_id
                                itemList.add(ProfileData(R.drawable.avatar_basic, "프로필 $profileId", profileId.toLong()))
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
                                itemList.add(ProfileData(imageResId, profile.profileName,profile.profileId.toLong()))
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

