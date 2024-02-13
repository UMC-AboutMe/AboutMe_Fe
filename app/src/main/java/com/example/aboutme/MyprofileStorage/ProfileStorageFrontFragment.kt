package com.example.aboutme.MyprofileStorage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
                                image.type == "USER_IMAGE" -> R.drawable.prof_avater1
                                else -> R.drawable.avatar_basic
                            }
                            response.result.front_features.forEach { feature ->
                                if (feature.key == "name") {
                                    binding.profileNameEt.text = feature.value
                                } else if (feature.key == null) {
                                    binding.profileNumEt.text = feature.value ?: ""
                                }
                            }
                            binding.profileIv.setImageResource(imageResId)
                            }
                        } else {
                            // 실패했을 때
                            Log.d("Retrofit_Get_Failed", response.toString())
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