package com.example.aboutme.Myprofile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.databinding.FragmentEditprofilefrontBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class EditProfileFrontFragment : Fragment() {

    lateinit var binding: FragmentEditprofilefrontBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditprofilefrontBinding.inflate(inflater, container, false)

        //요청 바디에 들어갈 데이터
        val profileId1 = arguments?.getString("profilId1")

//CoroutineScope의 lifecycleScope를 사용하여 백그라운드에서 실행될 코루틴 블록을 정의
        lifecycleScope.launch {
            try {
                // 백그라운드 스레드에서 Retrofit의 patchProfile 메서드를 호출하고, 결과를 받아옴
                // withContext를 사용하여 백그라운드 스레드에서 실행하도록 함
                val response: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.getDataAll(profileId1!!.toLong())
                }

                if (response.isSuccessful) {

                    // 성공한 응답 데이터를 받아옴
                    val responseData: GetAllProfile? = response.body()
                    Log.d("GETALL 성공", "응답 데이터: $responseData")
                    // responseData를 처리하는 로직 작성
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("GETALL 요청 실패", "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody")

                }
            } catch (e: Exception) {
                Log.e("GETALL 요청 실패", "에러: ${e.message}")
            }
        }


        return binding.root
    }
}