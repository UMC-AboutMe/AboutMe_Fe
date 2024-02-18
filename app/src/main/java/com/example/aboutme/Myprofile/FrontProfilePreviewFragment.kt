package com.example.aboutme.Myprofile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.databinding.FragmentFrontprofileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class FrontProfilePreviewFragment : Fragment(){

    lateinit var binding: FragmentFrontprofileBinding

    companion object {
        fun newInstance(positionId: Int): FrontProfilePreviewFragment {
            val fragment = FrontProfilePreviewFragment()
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

        binding = FragmentFrontprofileBinding.inflate(inflater, container, false)


        binding.turnBtn.setOnClickListener {
            // 프로필 ID 가져오기
            val positionId = arguments?.getInt("positionId", -1)

            // BackProfileFragment로 전환하기 위해 프로필 ID를 번들에 담아서 생성
            val backProfileFragment = BackProfilePreviewFragment.newInstance(positionId ?: -1)

            // 프로필 ID를 담은 번들을 BackProfileFragment로 전달
            backProfileFragment.arguments = arguments

            // BackProfileFragment로 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.profile_frame2, backProfileFragment)
                .commit()
        }

        val pref = requireContext().getSharedPreferences("pref", 0)
        val token = pref.getString("Gtoken", null) ?: ""
        //RetrofitClient.initialize(token)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileId1 = arguments?.getInt("positionId", -1)
        Log.d("프리뷰", profileId1.toString())

        viewLifecycleOwner.lifecycleScope.launch {
            delay(200) // 0.2초 지연
            refreshData(profileId1.toString())
        }


    }

    private fun refreshData(profileId: String?) {
        val pref = requireContext().getSharedPreferences("pref", 0)
        val token = pref.getString("Gtoken", null) ?: ""

        lifecycleScope.launch {
            try {
                val response: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.getDataAll(token,profileId!!.toLong())
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
        binding.profileNameEt.setText(updatedData.result.frontFeatures[0].value.toString())
        binding.profileNumEt.setText(updatedData.result.frontFeatures[1].value.toString())

        if (updatedData.result.profileImage.type == "USER_IMAGE"){
            if (updatedData.result.profileImage.profileImageUrl != null) {
                Glide.with(this)
                    .load(updatedData.result.profileImage.profileImageUrl)
                    .into(binding.profileIv)
            }
        } else if(updatedData.result.profileImage.type == "DEFAULT"){
            Glide.with(requireContext()).load(R.drawable.profiledefault).into(binding.profileIv)
        } else if (updatedData.result.profileImage.type == "CHARACTER"){
            if (updatedData.result.profileImage.characterType == "2") {
                Glide.with(requireContext()).load(R.drawable.prof_avater2).into(binding.profileIv)
            }
            if (updatedData.result.profileImage.characterType == "3") {
                Glide.with(requireContext()).load(R.drawable.prof_avater3).into(binding.profileIv)
            }
            if (updatedData.result.profileImage.characterType == "4") {
                Glide.with(requireContext()).load(R.drawable.prof_avater4).into(binding.profileIv)
            }
            if (updatedData.result.profileImage.characterType == "5") {
                Glide.with(requireContext()).load(R.drawable.prof_avater5).into(binding.profileIv)
            }
            if (updatedData.result.profileImage.characterType == "6") {
                Glide.with(requireContext()).load(R.drawable.prof_avater6).into(binding.profileIv)
            }
            if (updatedData.result.profileImage.characterType == "7") {
                Glide.with(requireContext()).load(R.drawable.prof_avater7).into(binding.profileIv)
            }
            if (updatedData.result.profileImage.characterType == "8") {
                Glide.with(requireContext()).load(R.drawable.prof_avater8).into(binding.profileIv)
            }
            if (updatedData.result.profileImage.characterType == "9") {
                Glide.with(requireContext()).load(R.drawable.prof_avater9).into(binding.profileIv)
            }
        }

        // 예시: 변경된 데이터가 로그에 출력되도록 함
        Log.d("UpdatedData", "Updated data applied to UI: $updatedData")
    }

}