package com.example.aboutme.Myprofile

import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofileData.FrontFeature
import com.example.aboutme.RetrofitMyprofileData.MainProfileData
import com.example.aboutme.databinding.FragmentMainprofileBinding
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
        binding.mainProfileVp.setCurrentItem(0, false)


        initViewPager()
    }

    private fun initViewPager() {
        //val multiList = mutableListOf<MultiProfileData>()

        /*multiList.add(MultiProfileData(R.drawable.myprofile_character, "1", "010-1234-5678"))
        multiList.add(MultiProfileData(R.drawable.myprofile_character, "2", "010-1234-5678"))
        multiList.add(MultiProfileData(R.drawable.myprofile_character, "3", "010-1234-5678"))*/

        /*val sharedPreferences = requireContext().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "")*/

        //initViewPager()

        vpadapter = MainProfileVPAdapter()

        binding.mainProfileVp.adapter = vpadapter

        //binding.mainProfileVp.setCurrentItem(0, false)


        RetrofitClient.mainProfile.getData().enqueue(object : Callback<MainProfileData> {
            // 서버 통신 실패 시의 작업
            override fun onFailure(call: Call<MainProfileData>, t: Throwable) {
                Log.e("실패", t.toString())
            }


            override fun onResponse(call: Call<MainProfileData>, response: Response<MainProfileData>) {
                val repos: MainProfileData? = response.body()
                if (repos != null) {
                    val frontFeatures: List<FrontFeature>? = repos.result.myprofiles?.flatMap { profile ->
                        profile.frontFeatures
                    }
                    val totalMyProfile = repos.getTotalMyProfile()

                    if (frontFeatures != null) {
                        multiList.clear()
                        for (profile in repos.result.myprofiles) {
                            val frontFeatures = profile.frontFeatures
                            //profile?.forEach {  ->
                            //for (frontFeature in frontFeatures) {
                                if (frontFeatures.size > 1) {
                                    multiList.add(
                                        MultiProfileData(
                                            R.drawable.myprofile_character,
                                            frontFeatures[0].value,
                                            frontFeatures[1].value

                                        )
                                    )
                                }else{
                                    multiList.add(
                                        MultiProfileData(
                                            R.drawable.myprofile_character,
                                            frontFeatures[0].value,
                                            ""

                                        )
                                    )
                                }
                            }



                        // 어댑터에 업데이트된 multiList를 제출합니다.
                        vpadapter.submitList(multiList)
                        Log.d("성공티비","success")
                        Log.d("FrontFeature List", multiList.toString())
                    } else {
                        Log.e("실패", "front_features 데이터가 null입니다.")
                    }
                } else {
                    Log.e("실패", "응답 데이터가 null입니다.")
                    Log.e("Response", "${response.code()}")
                }
                binding.mainProfileVp.setCurrentItem(0, false)
            }
        })

    }


}