package com.example.aboutme.Myprofile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.RetrofitMyprofileData.MainProfileData
import com.example.aboutme.databinding.FragmentBackprofileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BackProfileFragment : Fragment()
{

    companion object {
        // newInstance 메서드 추가
        fun newInstance(positionId: Int): BackProfileFragment {
            val fragment = BackProfileFragment()
            val args = Bundle().apply {
                putInt("positionId", positionId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var binding : FragmentBackprofileBinding

    private var selectedButtonId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = FragmentBackprofileBinding.inflate(inflater, container, false)

        val positionId = arguments?.getInt("positionId", -1)
        Log.d("FrontProfileFragment!!", "Profile ID: $positionId")



        binding.turnBtn2.setOnClickListener {
            // 프로필 ID 가져오기
            val positionId = arguments?.getInt("positionId", -1)

            // BackProfileFragment로 전환하기 위해 프로필 ID를 번들에 담아서 생성
            val frontProfileFragment = FrontProfileFragment.newInstance(positionId ?: -1)

            // 프로필 ID를 담은 번들을 BackProfileFragment로 전달
            frontProfileFragment.arguments = arguments

            // BackProfileFragment로 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.profile_frame, frontProfileFragment)
                .commit()
        }


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val positionId = arguments?.getInt("positionId", -1)
        Log.d("FrontProfileFragment!!", "Profile ID: $positionId")

        profilePosion(positionId!!) { realProfileId ->
            Log.d("realprofileID", realProfileId.toString())
            // 여기서 realProfileId를 사용할 수 있습니다.
            refreshData(realProfileId.toString())
        }
    }

    private fun profilePosion(positionId: Int, callback: (Int) -> Unit) {
        var realProfileId = -1 // 기본값 설정
        RetrofitClient.mainProfile.getData().enqueue(object : Callback<MainProfileData> {
            // 서버 통신 실패 시의 작업
            override fun onFailure(call: Call<MainProfileData>, t: Throwable) {
                Log.e("실패", t.toString())
                callback(realProfileId) // 실패 시에도 콜백 호출
            }

            override fun onResponse(
                call: Call<MainProfileData>,
                response: Response<MainProfileData>
            ) {
                val repos: MainProfileData? = response.body()
                if (repos != null) {
                    val totalMyProfile = repos.getTotalMyProfile()
                    Log.d("get!!", "응답 데이터: $repos")

                    if (totalMyProfile == 1) {
                        realProfileId = repos.result.myprofiles[0].profileId
                    }
                    if (totalMyProfile == 2) {
                        val minProfileId = repos.result.myprofiles[0].profileId
                        val maxProfileId = repos.result.myprofiles[1].profileId

                        realProfileId = if (positionId == 0) {
                            minProfileId
                        } else {
                            maxProfileId
                        }
                    }
                    if (totalMyProfile == 3) {
                        val minProfileId = repos.result.myprofiles[0].profileId
                        val mediumProfileId = repos.result.myprofiles[1].profileId
                        val maxProfileId = repos.result.myprofiles[2].profileId

                        realProfileId = when {
                            positionId == 0 -> minProfileId
                            positionId == 1 -> mediumProfileId
                            else -> maxProfileId
                        }
                    }
                } else {
                    Log.e("실패", "front_features 데이터가 null입니다.")
                }
                callback(realProfileId) // 응답 처리 후에 콜백 호출
            }
        })
    }




    private fun refreshData(profileId: String?) {
        lifecycleScope.launch {
            try {
                val response: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.getDataAll(profileId!!.toLong())
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