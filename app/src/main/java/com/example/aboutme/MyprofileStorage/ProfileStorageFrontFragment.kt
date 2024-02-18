package com.example.aboutme.MyprofileStorage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.aboutme.Myprofile.BackProfileFragment
import com.example.aboutme.MyprofileStorage.api.ProfStorageObj
import com.example.aboutme.MyprofileStorage.api.ProfStorageResponse
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentFrontprofileBinding
import com.example.aboutme.databinding.FragmentProfileStorageFrontBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileStorageFrontFragment : Fragment(){

    lateinit var binding: FragmentProfileStorageFrontBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val profileId: Long = arguments?.getLong("profId") ?: -1
        //Log.d("ProfileStorageDetail", "Front Received profId: $profileId")
        getProfile(profileId)
        binding = FragmentProfileStorageFrontBinding.inflate(inflater, container, false)
        binding.turnBtn.setOnClickListener {
            val ft = parentFragmentManager.beginTransaction()
            val fragment = ProfileStorageBackFragment()
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
                            //Log.d("Retrofit_Get_Success", response.toString())
                            val image = response.result.profile_image
                            val imageResId = when {
                                image.type == "CHARACTER" && image.charcterType in 1..8 -> {
                                    when (image.charcterType) {
                                        1 -> R.drawable.prof_avater1.toString()
                                        2 -> R.drawable.prof_avater2.toString()
                                        3 -> R.drawable.prof_avater3.toString()
                                        4 -> R.drawable.prof_avater4.toString()
                                        5 -> R.drawable.prof_avater5.toString()
                                        6 -> R.drawable.prof_avater6.toString()
                                        7 -> R.drawable.prof_avater7.toString()
                                        8 -> R.drawable.prof_avater8.toString()
                                        else -> R.drawable.prof_avater9.toString()
                                    }
                                }

                                image.type == "USER_IMAGE" -> image.profile_image_url ?: ""
                                else -> R.drawable.avatar_basic.toString()
                            }
                            response.result.front_features.forEach { feature ->
                                if (feature.key == "name") {
                                    binding.profileNameEt.text = feature.value
                                } else if (feature.key == null) {
                                    binding.profileNumEt.text = feature.value ?: ""
                                }
                            }
                            //binding.profileIv.setImageResource(imageResId)
                            if (imageResId.startsWith("http")) {
                                // URL인 경우 Glide를 사용하여 이미지 로드 및 표시
                                Glide.with(requireContext())
                                    .load(imageResId)
                                    .into(binding.profileIv)
                            } else {
                                // 리소스 아이디인 경우 setImageResource() 메서드를 사용하여 이미지 설정
                                binding.profileIv.setImageResource(imageResId.toInt())
                            }
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