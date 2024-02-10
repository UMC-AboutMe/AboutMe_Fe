package com.example.aboutme.Myprofile

import android.os.Bundle
import android.text.TextUtils.replace
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
import com.example.aboutme.RetrofitMyprofileData.RequestPatchProfile
import com.example.aboutme.databinding.FragmentEditprofilefrontBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class EditProfileFrontFragment : Fragment() {

    lateinit var binding: FragmentEditprofilefrontBinding

    private val frontProfile = mutableListOf<FrontEditData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditprofilefrontBinding.inflate(inflater, container, false)

        //요청 바디에 들어갈 데이터
        val profileId1 = arguments?.getString("profilId1")
        Log.d("profileId1",profileId1.toString())
        val dialogName = arguments?.getString("dialogName")

        binding.profileNameEt.setText(dialogName)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileId1 = arguments?.getString("profilId1")
        Log.d("profileId1",profileId1.toString())


        val retrofitClient = RetrofitClient.mainProfile

        binding.profileEditPreviewBtn.setOnClickListener {

            lifecycleScope.launch {
                try {
                    // 백그라운드 스레드에서 Retrofit의 patchProfile 메서드를 호출하고, 결과를 받아옴
                    // withContext를 사용하여 백그라운드 스레드에서 실행하도록 함
                    val response: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                        retrofitClient.getDataAll(profileId1!!.toLong())
                    }

                    if (response.isSuccessful) {

                        // 성공한 응답 데이터를 받아옴
                        val responseData: GetAllProfile? = response.body()
                        Log.d("GETALL 성공", "응답 데이터: $responseData")
                        // responseData를 처리하는 로직 작성

                        val frontFeature1 = responseData!!.result.frontFeatures[0].feature_id
                        Log.d("id!", frontFeature1.toString())
                        val frontFeature2 = responseData!!.result.frontFeatures[1].feature_id
                        Log.d("id!", frontFeature2.toString())

                        patchData(frontFeature1,frontFeature2)


                    } else {
                        val errorBody = response.errorBody()?.string() ?: "No error body"
                        Log.e(
                            "GETALL 요청 실패",
                            "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody"
                        )

                    }
                } catch (e: Exception) {
                    Log.e("GETALL 요청 실패", "에러: ${e.message}")
                }
            }
        }

    }

    private fun patchData(featureId1: Long, featureId2: Long) {
        val profileId1 = arguments?.getString("profilId1")
        Log.d("profileId1", profileId1.toString())

        val name = binding.profileNameEt.text.toString()
        val phoneNumber = binding.profileNumberEt.text.toString()

        val patchData1 = RequestPatchProfile(featureId1, "name", name)
        val patchData2 = RequestPatchProfile(featureId2, "phoneNumber", phoneNumber)

        Log.d("patchData", "Name: $name, PhoneNumber: $phoneNumber")

        lifecycleScope.launch {
            try {
                Log.d("patchData", "patchData1 호출 전")
                // withContext를 사용하여 백그라운드 스레드에서 실행하도록 함
                val response1: Response<PatchMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.patchProfile(profileId1!!.toLong(), patchData1)
                }
                Log.d("patchData", "patchData1 호출 후")

                Log.d("patchData", "patchData2 호출 전")
                val response2: Response<PatchMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.patchProfile(profileId1!!.toLong(), patchData2)
                }
                Log.d("patchData", "patchData2 호출 후")

                if (response1.isSuccessful && response2.isSuccessful) {
                    val responseData1: PatchMyprofile? = response1.body()
                    val responseData2: PatchMyprofile? = response2.body()
                    Log.d("patch 성공", "응답 데이터1: $responseData1, 응답 데이터2: $responseData2")
                    // responseData를 처리하는 로직 작성
                } else {
                    val errorBody1 = response1.errorBody()?.string() ?: "No error body"
                    val errorBody2 = response2.errorBody()?.string() ?: "No error body"
                    Log.e("patch 요청 실패", "응답코드1: ${response1.code()}, 응답메시지1: ${response1.message()}, 오류 내용1: $errorBody1")
                    Log.e("patch 요청 실패", "응답코드2: ${response2.code()}, 응답메시지2: ${response2.message()}, 오류 내용2: $errorBody2")
                }
            } catch (e: Exception) {
                Log.e("patch 요청 실패", "에러: ${e.message}")
            }
        }
    }
}