package com.example.aboutme.Myprofile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aboutme.MainProfile
import com.example.aboutme.R
import com.example.aboutme.RetrofitClient
import com.example.aboutme.RetrofitClient.mainProfile
import com.example.aboutme.databinding.FragmentMainprofileBinding
import com.google.gson.Gson
import com.kakao.sdk.user.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainProfileFragment : Fragment() {

    lateinit var binding: FragmentMainprofileBinding
    private val multiList = mutableListOf<MultiProfileData>() // 전역 변수로 multiList 선언
    private lateinit var vpadapter : MainProfileVPAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainprofileBinding.inflate(inflater, container, false)




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SharedPreferences에서 데이터를 읽어옴

        initViewPager()
    }

    private fun initViewPager() {
        val multiList = mutableListOf<MultiProfileData>()

        /*multiList.add(MultiProfileData(R.drawable.myprofile_character, "1", "010-1234-5678"))
        multiList.add(MultiProfileData(R.drawable.myprofile_character, "2", "010-1234-5678"))
        multiList.add(MultiProfileData(R.drawable.myprofile_character, "3", "010-1234-5678"))*/

        /*val sharedPreferences = requireContext().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "")

        //initViewPager()

        if (name != null) {
            multiList.add(MultiProfileData(R.id.char_iv, name, "010-1234-5678" ))
            Log.d("profile!!", "success")
        } else {
            Log.e("MainProfileFragment", "Name is null")
        }*/


        vpadapter = MainProfileVPAdapter()

        vpadapter.submitList(multiList)

        binding.mainProfileVp.adapter = vpadapter

        binding.mainProfileVp.setCurrentItem(0, false)

    }

    private fun fetchProfileData() {
        // Retrofit을 사용하여 API 서비스 인스턴스 생성
        val apiService = RetrofitClient.mainProfile

        // API 호출하여 데이터 가져오기 (비동기 방식)
        apiService.getData().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()?.string()

                    responseData?.let {
                        val myProfilesResponse = Gson().fromJson(it, MyProfilesResponse::class.java)

                        val myProfiles = myProfilesResponse.result.myProfiles

                        multiList.addAll(myProfiles.map { profile ->
                            val name = profile.frontFeatures.find { it.key == "name" }?.value ?: ""
                            val phoneNumber = profile.frontFeatures.find { it.key == "phonenumber" }?.value ?: ""
                            val profileImageUrl = profile.profileImageUrl

                            MultiProfileData(name, phoneNumber, profileImageUrl)
                        })

                        vpadapter.submitList(multiList)
                        binding.mainProfileVp.adapter = vpadapter
                    }
                } else {
                    Log.e("MainProfileFragment", "API call failed")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("MainProfileFragment", "API call failed: ${t.message}")
            }
        })
    }

}