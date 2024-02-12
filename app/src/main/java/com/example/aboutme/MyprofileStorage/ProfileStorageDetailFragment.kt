package com.example.aboutme.MyprofileStorage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aboutme.MyprofileStorage.api.ProfStorageObj
import com.example.aboutme.MyprofileStorage.api.ProfStorageResponse
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentProfileStorageDetailBinding
import com.kakao.sdk.user.model.Profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileStorageDetailFragment : Fragment() {
    lateinit var binding: FragmentProfileStorageDetailBinding
    private var items: MutableList<ProfileData>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileStorageDetailBinding.inflate(inflater)
        setFrag(0)

        binding.trashButton.setOnClickListener {
            val profileId = arguments?.getLong("profId")
            Log.d("retro", "$profileId")
            deleteProfiles(profileId!!.toLong(), 6)
        }

        val bundle = arguments
        if (bundle != null) {
            val profId = bundle.getLong("profId", -1)
            Log.d("ProfileStorageDetail", "Received profId: $profId")

            //프로필 상세 정보 조회
            getProfile(profId)
        } else {
            Log.e("ProfileStorageDetail", "Bundle is null")
        }

        return binding.root
    }

    private fun setFrag(fragNum: Int) {
        val ft = childFragmentManager.beginTransaction()
        when (fragNum) {
            0 -> {
                Log.d("MyProfileFragment", "FrontProfileFragment로 교체 중")
                ft.replace(R.id.profileStorage_frame, ProfileStorageFrontFragment()).commit()
            }

            1 -> {
                Log.d("MyProfileFragment", "BackProfileFragment로 교체 중")
                ft.replace(R.id.profileStorage_frame, ProfileStorageBackFragment()).commit()
            }
        }
    }

    //프로필 보관함 삭제 api
    private fun deleteProfiles(profId: Long, memberId: Int) {
        Log.d("Retrofit", "delete 함수 호출됨")
        val call = ProfStorageObj.getRetrofitService.deleteProfStorage(profId, 6)

        call.enqueue(object : Callback<ProfStorageResponse.ResponseDeleteProf> {
            override fun onResponse(
                call: Call<ProfStorageResponse.ResponseDeleteProf>,
                response: Response<ProfStorageResponse.ResponseDeleteProf>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()
                    if (response != null) {
                        if (response.isSuccess) {
                            //성공했을 때
                            Log.d("Retrofit_Delete", "처리에 성공함")
                        } else {
                            //실패했을 때
                            Log.d("Retrofit_Delete", "처리에 실패함")
                        }
                    }
                }
            }

            override fun onFailure(
                call: Call<ProfStorageResponse.ResponseDeleteProf>,
                t: Throwable
            ) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Delete", errorMessage)
            }
        }
        )
    }

    //마이프로필 조회 - 단건
    private fun getProfile(profId : Long ) {
        val call = ProfStorageObj.getRetrofitService.getProfList(profId)

        call.enqueue(object : Callback<ProfStorageResponse.ResponseProf> {
            override fun onResponse(
                call: Call<ProfStorageResponse.ResponseProf>,
                response: Response<ProfStorageResponse.ResponseProf>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()
                    if (response != null) {
                        if (response.isSuccess) {
                            // 성공했을 때
                            Log.d("Retrofit_Get_Success", response.toString())
                        } else {
                            // 실패했을 때
                            Log.d("Retrofit_Get_Failed", response.toString())
                        }

                    }
                } else {
                    Log.d("Retrofit_Get_Failed", response.toString())
                }
            }
            override fun onFailure(
                call: Call<ProfStorageResponse.ResponseProf>,
                t: Throwable
            ) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Get_Error", errorMessage)
            }
        }
        )
    }
}
