package com.example.aboutme.Myprofile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.RetrofitMyprofileData.PatchMyprofile
import com.example.aboutme.RetrofitMyprofileData.RequestPatchProfile
import com.example.aboutme.databinding.FragmentEditprofilebackBinding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class EditProfileBackFragment : Fragment(), EditProfileActivity.TabSelectedListener {

    lateinit var binding: FragmentEditprofilebackBinding
    private val viewModel: MyProfileViewModel by viewModels(ownerProducer = { requireActivity() })

    var frontFeature1 : String? = null
    var frontFeature2 : String? = null

    override fun onTabSelected(tabPosition: Int) {
        // 탭이 선택되었을 때의 동작 구현
        Log.d("탭 선택", tabPosition.toString())

        val feature1 = binding.feature1SchoolEt.text.toString()
        val feature2 = binding.feature2CompanyEt.text.toString()
        val feature3 = binding.feature3MbtiEt.text.toString()
        val feature4 = binding.feature4HobbyEt.text.toString()
        val feature5 = binding.profileTmiEt.text.toString()

        // ViewModel에 값을 전달합니다.
        viewModel.setFeatures(feature1, feature2,feature3,feature4,feature5)
        Log.d("plz3", feature1)

    }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            binding = FragmentEditprofilebackBinding.inflate(inflater, container, false)


            val profileId1 = arguments?.getString("profilId1")
            Log.d("profileId_to_back", profileId1.toString())

           /* viewModel.feature1.observe(this, { feature1 ->
                feature1?.let {
                    binding.feature1SchoolEt.setText(feature1)
                    Log.d("이전",feature1.toString())

                }
            })*/

            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val profileId1 = arguments?.getString("profilId1")
            Log.d("profileId1", profileId1.toString())

            //viewModel.cancelCoroutine()

            val retrofitClient = RetrofitClient.mainProfile


            viewModel.feature1.observe(viewLifecycleOwner) { feature1 ->
                binding.feature1SchoolEt.text = Editable.Factory.getInstance().newEditable(feature1)
            }

            viewModel.feature2.observe(viewLifecycleOwner) { feature2 ->
                binding.feature2CompanyEt.text = Editable.Factory.getInstance().newEditable(feature2)
            }


            viewModel.feature3.observe(viewLifecycleOwner) { feature3 ->
                binding.feature3MbtiEt.text = Editable.Factory.getInstance().newEditable(feature3)
            }

            viewModel.feature4.observe(viewLifecycleOwner) { feature4 ->
                binding.feature4HobbyEt.text = Editable.Factory.getInstance().newEditable(feature4)
            }


            viewModel.feature5.observe(viewLifecycleOwner) { feature5 ->
                binding.profileTmiEt.text = Editable.Factory.getInstance().newEditable(feature5)
            }


            binding.profileEditPreviewBtn.setOnClickListener {
                lifecycleScope.launch {
                    try {
                        val response: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                            retrofitClient.getDataAll(profileId1!!.toLong())
                        }

                        if (response.isSuccessful) {
                            val responseData: GetAllProfile? = response.body()
                            Log.d("GETALL 성공", "응답 데이터: $responseData")

                            val frontFeature1 = responseData!!.result.backFeatures[0].featureId
                            val frontFeature2 = responseData!!.result.backFeatures[1].featureId
                            val frontFeature3 = responseData!!.result.backFeatures[2].featureId
                            val frontFeature4 = responseData!!.result.backFeatures[3].featureId
                            val frontFeature5 = responseData!!.result.backFeatures[4].featureId
                            val frontFeature6 = responseData!!.result.frontFeatures[0].featureId
                            val frontFeature7 = responseData!!.result.frontFeatures[1].featureId

                            patchData(
                                frontFeature1,
                                frontFeature2,
                                frontFeature3,
                                frontFeature4,
                                frontFeature5,
                                frontFeature6,
                                frontFeature7
                            )
                            val intent = Intent(activity, PreviewProfileActivity::class.java)
                            intent.putExtra("profileId_to_preview", profileId1)
                            startActivity(intent)

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

            viewModel.updatedData.observe(viewLifecycleOwner, { updatedData ->
                Log.d("UpdatedData", "Updated data: $updatedData")
                if (updatedData != null) {
                    applyUpdatedDataToUI(updatedData)
                    Log.d("싱행행!!", updatedData.toString())
                } else {
                    Log.e("applyUpdatedDataToUI", "Updated data is null")
                }
            })
        }

        private suspend fun patchData(
            featureId1: Long,
            featureId2: Long,
            featureId3: Long,
            featureId4: Long,
            featureId5: Long,
            featureId6: Long,
            featureId7: Long
        ) {
            val profileId1 = arguments?.getString("profilId1")
            Log.d("profileId1", profileId1.toString())

            val schoolEt = binding.feature1SchoolEt.text.toString()
            val companyEt = binding.feature2CompanyEt.text.toString()
            val mbtiEt = binding.feature3MbtiEt.text.toString()
            val hobbyEt = binding.feature4HobbyEt.text.toString()
            val tmiEt = binding.profileTmiEt.text.toString()

            viewModel.name.observe(viewLifecycleOwner) { viewFeature1 ->
                frontFeature1 = viewFeature1
            }

            Log.d("feature1!!", frontFeature1.toString())

            viewModel.phoneNumber.observe(viewLifecycleOwner) { viewFeature2 ->
                frontFeature2 = viewFeature2
            }

            val patchData1 = RequestPatchProfile(featureId1, "school", schoolEt)
            val patchData2 = RequestPatchProfile(featureId2, "company", companyEt)
            val patchData3 = RequestPatchProfile(featureId3, "mbti", mbtiEt)
            val patchData4 = RequestPatchProfile(featureId4, "hobby", hobbyEt)
            val patchData5 = RequestPatchProfile(featureId5, "tmi", tmiEt)
            val patchFrontData6 = RequestPatchProfile(featureId6, "name", frontFeature1.toString())
            val patchFrontData7 = RequestPatchProfile(featureId7, "phoneNumber", frontFeature2.toString())

            try {
                Log.d("patchData", "patchData1 호출 전")
                val response1: Response<PatchMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.patchProfile(profileId1!!.toLong(), patchData1)
                }
                val response2: Response<PatchMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.patchProfile(profileId1!!.toLong(), patchData2)
                }
                val response3: Response<PatchMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.patchProfile(profileId1!!.toLong(), patchData3)
                }
                val response4: Response<PatchMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.patchProfile(profileId1!!.toLong(), patchData4)
                }
                val response5: Response<PatchMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.patchProfile(profileId1!!.toLong(), patchData5)
                }
                val response6: Response<PatchMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.patchProfile(profileId1!!.toLong(), patchFrontData6)
                }
                val response7: Response<PatchMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.patchProfile(profileId1!!.toLong(), patchFrontData7)
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
                    Log.d(
                        "patch 성공",
                        "응답 데이터1: $responseData1, 응답 데이터2: $responseData2, $responseData3, $responseData4, $responseData5"
                    )
                    Log.d("patchback!",response4.toString())
                    // responseData를 처리하는 로직 작성

                    // PATCH 요청이 성공한 후에 GET 요청을 보내어 최신 데이터를 받아옴
                    val updatedResponse: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                        RetrofitClient.mainProfile.getDataAll(profileId1!!.toLong())
                    }

                    if (updatedResponse.isSuccessful) {
                        // 최신 데이터를 받아옴
                        val updatedData: GetAllProfile? = updatedResponse.body()
                        Log.d("GETALL 성공", "업데이트된 데이터: $updatedData")

                        viewModel.updateData(updatedData)
                        // 업데이트된 데이터를 처리하는 로직 작성
                    } else {
                        val errorBody = updatedResponse.errorBody()?.string() ?: "No error body"
                        Log.e(
                            "GETALL 요청 실패",
                            "응답코드: ${updatedResponse.code()}, 응답메시지: ${updatedResponse.message()}, 오류 내용: $errorBody"
                        )
                    }
                } else {
                    val errorBody1 = response1.errorBody()?.string() ?: "No error body"
                    val errorBody2 = response2.errorBody()?.string() ?: "No error body"
                    Log.e(
                        "patch 요청 실패",
                        "응답코드1: ${response1.code()}, 응답메시지1: ${response1.message()}, 오류 내용1: $errorBody1"
                    )
                    Log.e(
                        "patch 요청 실패",
                        "응답코드2: ${response2.code()}, 응답메시지2: ${response2.message()}, 오류 내용2: $errorBody2"
                    )
                }
            } catch (e: Exception) {
                Log.e("patch 요청 실패", "에러: ${e.message}")
            }
        }

        private fun applyUpdatedDataToUI(updatedData: GetAllProfile) {
            // 변경된 데이터를 UI의 각 요소에 적용
            //binding.profileNameEt.setText(updatedData.result.frontFeatures[0].value)
            binding.feature1SchoolEt.setText(updatedData.result.backFeatures[0].value)
            Log.d("edit", updatedData.result.backFeatures[0].value.toString())
            binding.feature2CompanyEt.setText(updatedData.result.backFeatures[1].value)
            Log.d("edit", updatedData.result.frontFeatures[1].value.toString())
            binding.feature3MbtiEt.setText(updatedData.result.backFeatures[2].value)
            binding.feature4HobbyEt.setText(updatedData.result.backFeatures[3].value)
            binding.profileTmiEt.setText(updatedData.result.backFeatures[4].value)

            // 예시: 변경된 데이터가 로그에 출력되도록 함
            Log.d("UpdatedData", "Updated data applied to UI: $updatedData")
        }

    }
