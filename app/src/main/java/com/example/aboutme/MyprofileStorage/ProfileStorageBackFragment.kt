package com.example.aboutme.MyprofileStorage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aboutme.Myprofile.FrontProfileFragment
import com.example.aboutme.MyprofileStorage.api.ProfStorageObj
import com.example.aboutme.MyprofileStorage.api.ProfStorageResponse
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentBackprofileBinding
import com.example.aboutme.databinding.FragmentProfileStorageBackBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileStorageBackFragment : Fragment() {

    lateinit var binding: FragmentProfileStorageBackBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val profileId: Long = arguments?.getLong("profId") ?: -1
        //Log.d("ProfileStorageDetail", "Back Received profId: $profileId")
        getProfile(profileId)
        binding = FragmentProfileStorageBackBinding.inflate(inflater, container, false)
        binding.turnBtn2.setOnClickListener {
            val ft = parentFragmentManager.beginTransaction()
            val fragment = ProfileStorageFrontFragment()
            val bundle = Bundle()
            bundle.putLong("profId", profileId)
            fragment.arguments = bundle
            ft.replace(R.id.profileStorage_frame, fragment).commit()
        }
        return binding.root
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
                            binding.backProfileEt1.text = response.result.back_features[0].key
                            binding.backProfileEt2.text = response.result.back_features[1].key
                            binding.backProfileEt3.text = response.result.back_features[2].key
                            binding.backProfileEt4.text = response.result.back_features[3].key
                            binding.backProfileEt5.text = response.result.back_features[4].key

                        } else {
                            // 실패했을 때
                            Log.d("Retrofit_Get_Failed", response.toString())
                        }
                    }
                } else {
                    //Log.d("Retrofit_Get_Failed", response.toString())
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e(
                        "Retrofit_Get_Failed",
                        "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody"
                    )
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