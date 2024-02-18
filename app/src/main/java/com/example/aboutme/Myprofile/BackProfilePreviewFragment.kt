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
import com.example.aboutme.RetrofitMyprofileData.PatchMyprofile
import com.example.aboutme.databinding.FragmentBackprofileBinding
import com.example.aboutme.databinding.FragmentFrontprofileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class BackProfilePreviewFragment: Fragment() {

    lateinit var binding: FragmentBackprofileBinding

    companion object {
        // newInstance 메서드 추가
        fun newInstance(positionId: Int): BackProfilePreviewFragment {
            val fragment = BackProfilePreviewFragment()
            val args = Bundle().apply {
                putInt("positionId", positionId)
            }
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBackprofileBinding.inflate(inflater, container, false)

        val pref = requireContext().getSharedPreferences("pref", 0)
        val token = pref.getString("Gtoken", null) ?: ""

        // RetrofitClient 초기화
        //RetrofitClient.initialize(token)


        binding.turnBtn2.setOnClickListener {
            // 프로필 ID 가져오기
            val positionId = arguments?.getInt("positionId", -1)

            // BackProfileFragment로 전환하기 위해 프로필 ID를 번들에 담아서 생성
            val frontProfilePreviewFragment = FrontProfilePreviewFragment.newInstance(positionId ?: -1)

            // 프로필 ID를 담은 번들을 BackProfileFragment로 전달
            frontProfilePreviewFragment.arguments = arguments

            // BackProfileFragment로 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.profile_frame2, frontProfilePreviewFragment)
                .commit()
        }

        val profileId = arguments?.getInt("positionId", -1)
        Log.d("preview_id_in_fragment!!", profileId.toString())

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileId1 = arguments?.getInt("positionId", -1)
        Log.d("프리뷰", profileId1.toString())

        viewLifecycleOwner.lifecycleScope.launch {
            delay(300) // 0.5초 지연
            refreshData(profileId1.toString())
        }
    }

    private fun refreshData(profileId: String?) {
        val pref = requireContext().getSharedPreferences("pref", 0)
        val token = pref.getString("Gtoken", null) ?: ""

        lifecycleScope.launch {
            try {
                val response: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.getDataAll(token, profileId!!.toLong())
                }

                if (response.isSuccessful) {
                    val responseData: GetAllProfile? = response.body()
                    Log.d("GETALL 성공!!!!", "응답 데이터: $responseData")
                    responseData?.let { applyUpdatedDataToUI(it) }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("GETALL 요청 실패", "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("GETALL 요청 실패", "에러: ${e.message}")
            }
        }
    }

    private fun applyUpdatedDataToUI(updatedData: GetAllProfile) {
        // 변경된 데이터를 UI의 각 요소에 적용
        binding.backProfileEt1.setText(updatedData.result.backFeatures[0].value)
        binding.backProfileEt2.setText(updatedData.result.backFeatures[1].value)
        binding.backProfileEt3.setText(updatedData.result.backFeatures[2].value)
        binding.backProfileEt4.setText(updatedData.result.backFeatures[3].value)
        binding.backProfileEt5.setText(updatedData.result.backFeatures[4].value)


        // 예시: 변경된 데이터가 로그에 출력되도록 함
        Log.d("UpdatedData", "Updated data applied to UI: $updatedData")
    }

}