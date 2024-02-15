package com.example.aboutme.MyprofileStorage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.aboutme.MyprofileStorage.api.ProfStorageObj
import com.example.aboutme.MyprofileStorage.api.ProfStorageResponse
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentProfileStorageDetailBinding
import com.example.aboutme.databinding.FragmentProfileStorageFrontBinding
import com.kakao.sdk.user.model.Profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileStorageDetailFragment : Fragment() {
    lateinit var binding: FragmentProfileStorageDetailBinding
    private var items: MutableList<ProfileData>? = null
    lateinit var bindingFront : FragmentProfileStorageFrontBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //새로 추가
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

        val profileId: Long = arguments?.getLong("profId") ?: -1

        binding = FragmentProfileStorageDetailBinding.inflate(inflater)
        bindingFront = FragmentProfileStorageFrontBinding.inflate(inflater)

        setFrag(0,profileId)

        binding.trashButton.setOnClickListener {
            //Log.d("retro", "$profileId")
            deleteProfiles(profileId!!.toLong(), 6)
            parentFragmentManager.popBackStack()
        }
        return binding.root
    }
    private fun setFrag(fragNum: Int, profId: Long) {
        val ft = childFragmentManager.beginTransaction()
        when (fragNum) {
            0 -> {
                //Log.d("MyProfileFragment", "FrontProfileFragment로 교체 중")
                val fragment = ProfileStorageFrontFragment()
                val bundle = Bundle()
                bundle.putLong("profId", profId)
                getProfile(profId)
                fragment.arguments = bundle
                ft.replace(R.id.profileStorage_frame, fragment).commit()
                //Log.d("ProfileStorageDetail2", "Received profId: $profId")
            }

            1 -> {
                //Log.d("MyProfileFragment", "BackProfileFragment로 교체 중")
                val fragment = ProfileStorageBackFragment()
                val bundle = Bundle()
                bundle.putLong("profId", profId)
                getProfile(profId)
                fragment.arguments = bundle
                ft.replace(R.id.profileStorage_frame, fragment).commit()
                //Log.d("ProfileStorageDetail3", "Received profId: $profId")
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
                            //새로 추가
                            response.result.front_features.forEach { feature ->
                                if (feature.key == "name") {
                                    //작업하던 부분
                                    //bindingFront.profileNameEt.text = feature.value
                                   //Log.d("Retrofit_Get_Success", feature.value.toString())
                                }
                            }
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
        })
    }
}
