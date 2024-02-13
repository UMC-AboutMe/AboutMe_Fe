package com.example.aboutme.Myprofile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.FrontFeature
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.RetrofitMyprofileData.PatchMyprofile
import com.example.aboutme.RetrofitMyprofileData.RequestPatchProfile
import com.example.aboutme.databinding.FragmentEditprofilefrontBinding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class EditProfileFrontFragment : Fragment(), EditProfileActivity.TabSelectedListener {

    lateinit var binding: FragmentEditprofilefrontBinding
    private val viewModel: MyProfileViewModel by viewModels(ownerProducer = { requireActivity() })


    var feature1 : String? = null
    var feature2 : String? = null
    var feature3 : String? = null
    var feature4 : String? = null
    var feature5 : String? = null

    //private var job: Job? = null // Nullable Job 변수 선언


    override fun onTabSelected(tabPosition: Int) {
        // 탭이 선택되었을 때의 동작 구현
        Log.d("탭 선택", tabPosition.toString())

        val name = binding.profileNameEt.text.toString()
        val phoneNumber = binding.profileNumberEt.text.toString()

        // ViewModel에 값을 전달합니다.
        viewModel.setNameAndPhoneNumber(name, phoneNumber)
        Log.d("plz", name)
        Log.d("plz2", phoneNumber)
    }

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

        val editName = arguments?.getString("editname1")
        Log.d("받은 editname",editName.toString())

        /*viewModel.updatedData.observe(viewLifecycleOwner, { updatedData ->
            Log.d("UpdatedData", "Updated data: $updatedData")
            if (updatedData != null) {
                applyUpdatedDataToUI(updatedData)
                Log.d("싱행",updatedData.toString())
            } else {
                Log.e("applyUpdatedDataToUI", "Updated data is null")
            }
        })*/



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileId1 = arguments?.getString("profilId1")
        Log.d("profileId1",profileId1.toString())


        val retrofitClient = RetrofitClient.mainProfile


        viewModel.name.observe(viewLifecycleOwner) { name ->
            binding.profileNameEt.text = Editable.Factory.getInstance().newEditable(name)
        }

        viewModel.phoneNumber.observe(viewLifecycleOwner) { phoneNumber ->
            binding.profileNumberEt.text = Editable.Factory.getInstance().newEditable(phoneNumber)
        }

        viewModel.updatedData.observe(viewLifecycleOwner, { updatedData ->
            Log.d("UpdatedData", "Updated data: $updatedData")
            if (updatedData != null) {
                applyUpdatedDataToUI(updatedData)
                Log.d("싱행",updatedData.toString())
            } else {
                Log.e("applyUpdatedDataToUI", "Updated data is null")
            }
        })

        binding.profileEditPreviewBtn.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val response: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                        retrofitClient.getDataAll(profileId1!!.toLong())
                    }

                    if (response.isSuccessful) {
                        val responseData: GetAllProfile? = response.body()
                        Log.d("GETALL 성공", "응답 데이터: $responseData")

                        val frontFeature1 = responseData!!.result.frontFeatures[0].featureId
                        val frontFeature2 = responseData!!.result.frontFeatures[1].featureId
                        val backFeature1 = responseData!!.result.backFeatures[0].featureId
                        val backFeature2 = responseData!!.result.backFeatures[1].featureId
                        val backFeature3 = responseData!!.result.backFeatures[2].featureId
                        val backFeature4 = responseData!!.result.backFeatures[3].featureId
                        val backFeature5 = responseData!!.result.backFeatures[4].featureId
                        patchData(frontFeature1, frontFeature2, backFeature1,backFeature2, backFeature3, backFeature4, backFeature5)
                        //responseData?.let { applyUpdatedDataToUI(it) }

                        // patchData 호출 후에 PreviewProfileActivity 시작
                        val intent = Intent(activity, PreviewProfileActivity::class.java).apply {
                            putExtra("profileId_to_preview", profileId1)
                        }
                        startActivity(intent)
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

    private fun patchData(featureId1: Long, featureId2: Long, featureId3: Long, featureId4: Long, featureId5: Long, featureId6: Long, featureId7: Long) {
        val profileId1 = arguments?.getString("profilId1")
        Log.d("profileId1", profileId1.toString())

        val name = binding.profileNameEt.text.toString()
        val phoneNumber = binding.profileNumberEt.text.toString()

        viewModel.feature1.observe(viewLifecycleOwner) { viewFeature1 ->
            feature1 = viewFeature1
        }

        Log.d("feature1!", feature1.toString())

        viewModel.feature2.observe(viewLifecycleOwner) { viewFeature2 ->
            feature2 = viewFeature2
        }

        viewModel.feature3.observe(viewLifecycleOwner) { viewFeature3 ->
            feature3 = viewFeature3
        }

        viewModel.feature4.observe(viewLifecycleOwner) { viewFeature4 ->
            feature4 = viewFeature4
        }

        viewModel.feature5.observe(viewLifecycleOwner) { viewFeature5 ->
            feature5 = viewFeature5
        }

        val patchData1 = RequestPatchProfile(featureId1, "name", name)
        val patchData2 = RequestPatchProfile(featureId2, "phoneNumber", phoneNumber)
        val patchBackData1 = RequestPatchProfile(featureId3, "school", feature1.toString())
        val patchBackData2 = RequestPatchProfile(featureId4, "company", feature2.toString())
        val patchBackData3 = RequestPatchProfile(featureId5, "mbti", feature3.toString())
        val patchBackData4 = RequestPatchProfile(featureId6, "hobby", feature4.toString())
        val patchBackData5 = RequestPatchProfile(featureId7, "tmi", feature5.toString())

        Log.d("patchData", "Name: $name, PhoneNumber: $phoneNumber")
        Log.d("patchdata2",feature1.toString())

        lifecycleScope.launch {
            try {
                val response1: Response<PatchMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.patchProfile(profileId1!!.toLong(), patchData1)
                }

                val response2: Response<PatchMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.patchProfile(profileId1!!.toLong(), patchData2)
                }
                val response3: Response<PatchMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.patchProfile(profileId1!!.toLong(), patchBackData1)
                }
                val response4: Response<PatchMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.patchProfile(profileId1!!.toLong(), patchBackData2)
                }
                val response5: Response<PatchMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.patchProfile(profileId1!!.toLong(), patchBackData3)
                }
                val response6: Response<PatchMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.patchProfile(profileId1!!.toLong(), patchBackData4)
                }
                val response7: Response<PatchMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.patchProfile(profileId1!!.toLong(), patchBackData5)
                }

                if (response1.isSuccessful && response2.isSuccessful && response3.isSuccessful && response4.isSuccessful && response5.isSuccessful
                    && response6.isSuccessful && response7.isSuccessful) {
                    val responseData1: PatchMyprofile? = response1.body()
                    val responseData2: PatchMyprofile? = response2.body()
                    val responseData3: PatchMyprofile? = response3.body()
                    val responseData4: PatchMyprofile? = response4.body()
                    val responseData5: PatchMyprofile? = response5.body()
                    val responseData6: PatchMyprofile? = response6.body()
                    val responseData7: PatchMyprofile? = response7.body()
                    Log.d("patch 성공", "응답 데이터1: $responseData1, 응답 데이터2: $responseData2")
                    Log.d("patch 성공1", response5.toString())

                    // 수정된 데이터를 받아옴
                    val updatedResponse: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                        RetrofitClient.mainProfile.getDataAll(profileId1!!.toLong())
                    }
                    Log.d("수정", updatedResponse.toString())

                    if (updatedResponse.isSuccessful) {
                        // 최신 데이터를 받아옴
                        val updatedData: GetAllProfile? = updatedResponse.body()
                        Log.d("GETALL 성공", "업데이트된 데이터: $updatedData")

                        // ViewModel에 수정된 데이터를 전달
                        viewModel.updateData(updatedData)
                        if (updatedData != null) {
                            applyUpdatedDataToUI(updatedData)
                        } else {
                            Log.e("applyUpdatedDataToUI", "Updated data is null")
                        }
                    } else {
                        val errorBody = updatedResponse.errorBody()?.string() ?: "No error body"
                        Log.e("GETALL 요청 실패", "응답코드: ${updatedResponse.code()}, 응답메시지: ${updatedResponse.message()}, 오류 내용: $errorBody")
                    }
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



    private fun applyUpdatedDataToUI(updatedData: GetAllProfile) {
        // 변경된 데이터를 UI의 각 요소에 적용
        //binding.profileNameEt.setText(updatedData.result.frontFeatures[0].value)
        binding.profileNameEt.text = Editable.Factory.getInstance().newEditable(updatedData.result.frontFeatures[0].value)
        Log.d("edit2",updatedData.result.frontFeatures[0].value.toString())
        binding.profileNumberEt.setText(updatedData.result.frontFeatures[1].value)
        Log.d("edit2",updatedData.result.frontFeatures[1].value.toString())

        // 예시: 변경된 데이터가 로그에 출력되도록 함
        Log.d("UpdatedData", "Updated data applied to UI: $updatedData")
    }



}