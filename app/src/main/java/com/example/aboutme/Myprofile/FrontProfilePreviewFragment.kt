package com.example.aboutme.Myprofile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.databinding.FragmentFrontprofileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class FrontProfilePreviewFragment : Fragment(){

    lateinit var binding: FragmentFrontprofileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFrontprofileBinding.inflate(inflater, container, false)


        binding.turnBtn.setOnClickListener {
            val ft = requireActivity().supportFragmentManager.beginTransaction()

            ft.replace(R.id.profile_frame2, BackProfilePreviewFragment()).commit()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileId = arguments?.getString("profileId")
        Log.d("preview_id_in_fragment", profileId.toString())

        lifecycleScope.launch {
            try {
                // 백그라운드 스레드에서 Retrofit의 patchProfile 메서드를 호출하고, 결과를 받아옴
                // withContext를 사용하여 백그라운드 스레드에서 실행하도록 함
                val response: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.getDataAll(profileId!!.toLong())
                }

                if (response.isSuccessful) {

                    // 성공한 응답 데이터를 받아옴
                    val responseData: GetAllProfile? = response.body()
                    Log.d("GETALL 성공", "응답 데이터: $responseData")
                    // responseData를 처리하는 로직 작성

                    binding.profileNameEt.text = responseData!!.result.frontFeatures[0].value
                    //Log.d("preview_value0",responseData.result.frontFeatures[0].value)



                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("GETALL 요청 실패", "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody")

                }
            } catch (e: Exception) {
                Log.e("GETALL 요청 실패", "에러: ${e.message}")
            }
        }
    }


}