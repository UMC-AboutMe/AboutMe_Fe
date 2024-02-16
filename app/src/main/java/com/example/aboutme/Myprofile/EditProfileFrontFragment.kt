package com.example.aboutme.Myprofile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextUtils.replace
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.FrontFeature
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.RetrofitMyprofileData.PatchMyprofile
import com.example.aboutme.RetrofitMyprofileData.PatchProfileImage
import com.example.aboutme.RetrofitMyprofileData.RequestPatchProfile
import com.example.aboutme.RetrofitMyprofileData.RequestProfileImage
import com.example.aboutme.databinding.FragmentEditprofilefrontBinding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditProfileFrontFragment : Fragment(), EditProfileActivity.TabSelectedListener {


    lateinit var binding: FragmentEditprofilefrontBinding
    private val viewModel: MyProfileViewModel by viewModels(ownerProducer = { requireActivity() })

    lateinit var getResult: ActivityResultLauncher<Intent>

    lateinit var filePath: String

    var feature1: String? = null
    var feature2: String? = null
    var feature3: String? = null
    var feature4: String? = null
    var feature5: String? = null

    var patchImage: RequestProfileImage? = null
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

        val profileId1 = arguments?.getString("profilId1")

        //binding.profileNameEt.addTextChangedListener(MyTextWatcher(profileId1))


        val frontFeature1 = arguments?.getString("feature1")
        Log.d("frontfeature~~", frontFeature1.toString())
        val frontFeature2 = arguments?.getString("feature2")

        val dialogName = arguments?.getString("dialogName")
        Log.d("frontfeature~", dialogName.toString())

        if (frontFeature1 != null) {
            binding.profileNameEt.setText(frontFeature1)
            binding.profileNumberEt.setText(frontFeature2)
        } else {
            binding.profileNameEt.setText(dialogName)
        }

        val editName = arguments?.getString("editname1")
        Log.d("받은 editname", editName.toString())

        binding.imageCharBtn.setOnClickListener {
            patchProfileImage2(profileId1!!.toLong(),"CHARACTER")
        }

        binding.imageLogoBtn.setOnClickListener {
            patchProfileImage2(profileId1!!.toLong(),"DEFAULT")
        }

        binding.imageImageBtn.setOnClickListener {
            goGallery()
        }

        // Initialize getResult here
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                filePath = getRealPathFromURI(it.data?.data!!)
                Log.d("경로", filePath)

                patchProfileImage(profileId1!!.toLong(), "USER_IMAGE", filePath)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileId1 = arguments?.getString("profilId1")
        Log.d("profileId1", profileId1.toString())


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
                Log.d("싱행", updatedData.toString())
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
                        patchData(
                            frontFeature1,
                            frontFeature2,
                            backFeature1,
                            backFeature2,
                            backFeature3,
                            backFeature4,
                            backFeature5
                        )
                        //responseData?.let { applyUpdatedDataToUI(it) }

                        // patchData 호출 후에 PreviewProfileActivity 시작
                        val intent = Intent(activity, PreviewProfileActivity::class.java).apply {
                            putExtra("profileId_to_preview", profileId1)
                        }
                        startActivity(intent)
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "No error body"
                        Log.e(
                            "GETALL 요청 실패",
                            "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody"
                        )
                    }
                } catch (e: Exception) {
                    Log.e("GETALL 요청 실패@", "에러: ${e.message}")
                }
            }
        }


    }


    private fun patchData(
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
        Log.d("patchdata2", feature1.toString())

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
                    && response6.isSuccessful && response7.isSuccessful
                ) {
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
    }


    private fun applyUpdatedDataToUI(updatedData: GetAllProfile) {
        // 변경된 데이터를 UI의 각 요소에 적용
        //binding.profileNameEt.setText(updatedData.result.frontFeatures[0].value)
        binding.profileNameEt.text =
            Editable.Factory.getInstance().newEditable(updatedData.result.frontFeatures[0].value)
        Log.d("edit2", updatedData.result.frontFeatures[0].value.toString())
        binding.profileNumberEt.setText(updatedData.result.frontFeatures[1].value)
        Log.d("edit2", updatedData.result.frontFeatures[1].value.toString())

        // 예시: 변경된 데이터가 로그에 출력되도록 함
        Log.d("UpdatedData", "Updated data applied to UI: $updatedData")
    }


    private fun getRealPathFromURI(uri: Uri): String {
        val buildName = Build.MANUFACTURER
        if (buildName.equals("Xiaomi")) {
            return uri.path.toString()
        }

        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = requireActivity().contentResolver.query(uri, proj, null, null, null)

        if (cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }

        return cursor.getString(columnIndex)
    }


    private fun goGallery() {

        // 현재 기기에 설정된 쓰기 권한을 가져오기 위한 변수
        var writePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

// 현재 기기에 설정된 읽기 권한을 가져오기 위한 변수
        var readPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

// 읽기 권한과 쓰기 권한에 대해서 설정이 되어있지 않다면
        if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
            // 읽기, 쓰기 권한을 요청합니다.
            Log.d("go!!", "su")
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                1
            )
        }
// 위 경우가 아니라면 권한에 대해서 설정이 되어 있으므로
        else {
            var state = Environment.getExternalStorageState()

            // 갤러리를 열어서 파일을 선택할 수 있도록 합니다.
            if (TextUtils.equals(state, Environment.MEDIA_MOUNTED)) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                getResult.launch(intent)
            }
        }
    }


    private fun patchProfileImage(profileId: Long, type: String, filPath : String) {
        val json = JSONObject()
        json.put("profile_image_type", type)

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toString().toRequestBody(mediaType)

        Log.d("경로4",json.toString())
        Log.d("경로5",mediaType.toString())
        Log.d("경로6",requestBody.toString())

        val file = File(filePath)
        val requestFile = "image/*".toMediaTypeOrNull()?.let { RequestBody.create(it, file) }
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", file.name, requestFile!!)
        Log.d("경로1", filePath)
        Log.d("경로2", requestFile.toString())
        Log.d("경로3", body.toString())


        val call: Call<PatchProfileImage> =
            RetrofitClient.mainProfile.patchProfileImage(profileId, requestBody, body)
        call.enqueue(object : Callback<PatchProfileImage> {
            override fun onResponse(
                call: Call<PatchProfileImage>,
                response: Response<PatchProfileImage>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body() // 응답 본문 가져오기
                    if (responseBody != null) {
                        Log.d("서버 응답 본문", responseBody.toString()) // 응답 본문 출력
                    } else {
                        Log.d("서버 응답 본문", "응답 본문이 비어있습니다.")
                    }
                } else {
                    Log.d("서버 응답 오류", "서버 응답이 실패했습니다.")
                }
            }

            override fun onFailure(call: Call<PatchProfileImage>, t: Throwable) {
                // 통신 실패 처리
                Log.e("통신 실패", "요청 실패: ${t.message}", t)
            }
        })
    }

    private fun patchProfileImage2(profileId: Long, type: String) {
        val json = JSONObject()
        json.put("profile_image_type", type)

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toString().toRequestBody(mediaType)

        Log.d("경로4",json.toString())
        Log.d("경로5",mediaType.toString())
        Log.d("경로6",requestBody.toString())


        val call: Call<PatchProfileImage> =
            RetrofitClient.mainProfile.patchProfileImage(profileId, requestBody, null)
        call.enqueue(object : Callback<PatchProfileImage> {
            override fun onResponse(
                call: Call<PatchProfileImage>,
                response: Response<PatchProfileImage>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body() // 응답 본문 가져오기
                    if (responseBody != null) {
                        Log.d("서버 응답 본문", responseBody.toString()) // 응답 본문 출력
                    } else {
                        Log.d("서버 응답 본문", "응답 본문이 비어있습니다.")
                        Log.d("서버", response.message())
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    //Log.d("서버 응답 오류", "서버 응답이 실패했습니다.")
                    Log.d("서버 응답 오류", "서버 응답이 실패했습니다. 오류 메시지: $errorBody")
                    try {
                        val errorMessage = JSONObject(errorBody).getString("message")
                        Log.d("오류 메시지", errorMessage)
                        Toast.makeText(requireContext(),errorMessage,Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        Log.e("JSON 파싱 오류", "오류 메시지를 추출하는 데 실패했습니다: ${e.message}")
                    }
                }
            }

            override fun onFailure(call: Call<PatchProfileImage>, t: Throwable) {
                // 통신 실패 처리
                Log.e("통신 실패", "요청 실패: ${t.message}", t)
            }
        })
    }

}