package com.example.aboutme.Myprofile

import RetrofitClient
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
        val multiList = mutableListOf<FrontFeature>()

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

        //initViewPager()

        vpadapter = MainProfileVPAdapter()

        vpadapter.submitList(multiList)

        binding.mainProfileVp.adapter = vpadapter

        binding.mainProfileVp.setCurrentItem(0, false)




        RetrofitClient.mainProfile.getData().enqueue(object : Callback<FrontFeature> {
            // 서버 통신 실패 시의 작업
            override fun onFailure(call: Call<FrontFeature>, t: Throwable) {
                Log.e("실패", t.toString())
            }

            // 서버 통신 성공 시의 작업
            // 매개변수 Response에 서버에서 받은 응답 데이터가 들어있음.
            override fun onResponse(
                call: Call<FrontFeature>,
                response: Response<FrontFeature>
            ) {
                // 응답받은 데이터를 가지고 처리할 코드 작성
                val repos: FrontFeature? = response.body()
                if (repos != null) {
                    Log.d("성공", repos.toString())
                } else {
                    Log.e("실패", "응답 데이터가 null입니다.")
                }
            }
        })
    }

}