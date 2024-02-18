package com.example.aboutme.Search

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.aboutme.R
import com.example.aboutme.Search.api.SearchObj
import com.example.aboutme.Search.api.SearchResponse
import com.example.aboutme.databinding.ActivityCustomDialogProfBinding
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomDialogProf(private val serial : Int) : DialogFragment() {
    private var _binding: ActivityCustomDialogProfBinding? = null
    private val binding get() = _binding!!
    lateinit var profileAdapter: DialogProfAdapter
    private val datas = mutableListOf<DialogProfData>()
    lateinit var token: String // token 변수를 추가

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ActivityCustomDialogProfBinding.inflate(inflater, container, false)
        // token을 SharedPreferences에서 가져와서 초기화
        val pref = requireContext().getSharedPreferences("pref", 0)
        token = pref.getString("Gtoken", null) ?: ""
        Log.d("token", token)

        val view = binding.root
        // 레이아웃 배경을 투명하게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes?.windowAnimations = R.style.AnimationPopupStyle
        // 다이얼로그를 하단으로 조정
        dialog?.window?.setGravity(Gravity.BOTTOM)

        //다이얼로그 외부 클릭 시 종료 x
        dialog?.setCanceledOnTouchOutside(false)

        getProfiles()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialog?.window?.attributes?.windowAnimations = R.style.AnimationPopupStyle
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout( WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT )
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
    }
    private fun initRecycler() {
        profileAdapter = DialogProfAdapter(requireContext())
        binding.DialogProfRc.adapter = profileAdapter
        }
    //마이프로필 목록 조회
    private fun getProfiles() {
        Log.d("Retrofit_Add", "프로필 공유 실행")
        val call = SearchObj.getRetrofitService.getProfileList(token)

        call.enqueue(object : Callback<SearchResponse.ResponseGetProfiles> {
            override fun onResponse(
                call: Call<SearchResponse.ResponseGetProfiles>,
                response: Response<SearchResponse.ResponseGetProfiles>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()
                    Log.d("Retrofit_Add", response.toString())
                    if (response != null) {
                        if (response.isSuccess) {
                            // 성공했을 때
                            profileAdapter.datas.clear()
                            response.result.myprofiles.forEach { profile ->
                                var profileName = ""
                                profile.front_features.forEach { feature ->
                                    if (feature.key == "name") {
                                        profileName = feature.value
                                    }
                                }
                                // 프로필 이미지 가져오기
                                val imageResId = when {
                                    profile.profile_image.type == "CHARACTER" && profile.profile_image.characterType in 1..8 -> {
                                        when (profile.profile_image.characterType) {
                                            1 -> R.drawable.prof_avater1.toString()
                                            2 -> R.drawable.prof_avater2.toString()
                                            3 -> R.drawable.prof_avater3.toString()
                                            4 -> R.drawable.prof_avater4.toString()
                                            5 -> R.drawable.prof_avater5.toString()
                                            6 -> R.drawable.prof_avater6.toString()
                                            7 -> R.drawable.prof_avater7.toString()
                                            8 -> R.drawable.prof_avater8.toString()
                                            else -> R.drawable.prof_avater9.toString()
                                        }
                                    }
                                    profile.profile_image.type == "USER_IMAGE" -> profile.profile_image.profile_image_url ?: ""
                                    else -> R.drawable.avatar_basic.toString()
                                }
                                profileAdapter.datas.add(
                                    DialogProfData(
                                        profile_img = imageResId,
                                        profile_name = profileName,
                                        profile_num = " ", //일단 임의값 - 전화번호 부분
                                        serial_number = profile.serial_number,
                                        isChecked = false
                                    )
                                )
                            }
                            // 어댑터의 데이터가 변경되었음을 알리고 화면을 업데이트합니다.
                            profileAdapter.notifyDataSetChanged()
                            // 확인 버튼
//                            binding.yesBtn.setOnClickListener {
//                                postShareProf()
//                                dismiss()
                            // 확인 버튼 클릭 시
                            binding.yesBtn.setOnClickListener {
                                // 선택된 아이템의 serial_number 목록 가져오기
                                val selectedSerials = profileAdapter.checkedSerials
                                // 선택된 아이템이 없는 경우
                                if (selectedSerials.isEmpty()) {
                                    // 처리할 로직 작성 (예: Toast 메시지 출력 등)
                                    Toast.makeText(requireContext(), "선택된 프로필이 없습니다.", Toast.LENGTH_SHORT).show()
                                } else {
                                    // 선택된 아이템이 있는 경우 postShareProf() 호출
                                    postShareProf(requireContext(),selectedSerials)
                                    Log.d("retro","$selectedSerials")
                                    dismiss()
                                }
                            }
                        }
                    }
                }
                val errorBody = response.errorBody()?.string() ?: "No error body"
                Log.e(
                    "Retrofit_Get_Failed",
                    "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody"
                )
            }
            override fun onFailure(call: Call<SearchResponse.ResponseGetProfiles>, t: Throwable) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Add", errorMessage)
            }
        })
    }
    //마이프로필 상대방에게 공유
    private fun postShareProf(contest: Context, selectedSerials: List<Int>) {
        Log.d("Retrofit_Add", "프로필 공유 실행")
        //상대방의 마이프로필 시리얼 번호(이전 화면에서 가져옴) , 공유할 마이프로필 시리얼 번호
        val requestShareProf = SearchResponse.RequestShareProf(listOf(serial), selectedSerials)
        val call = SearchObj.getRetrofitService.postShareProf(token, requestShareProf)

        call.enqueue(object : Callback<SearchResponse.ResponseShareProf> {
            override fun onResponse(
                call: Call<SearchResponse.ResponseShareProf>,
                response: Response<SearchResponse.ResponseShareProf>
            ) {
                Log.d("Retrofit_Add", response.toString())
                if (response.isSuccessful) {
                    val response = response.body()
                    Log.d("Retrofit_Add", response.toString())

                    if (response != null) {
                        if (response.isSuccess) {
                            //성공했을 때
                            Log.d("Retrofit_Share","내 프로필 공유 성공")
                            Log.d("Retrofit_Share", response.message)
                            //Toast.makeText(requireContext(), "선택한 마이프로필이 공유되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e(
                        "Retrofit_Storage_Failed",
                        "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody"
                    )
                    try {
                        val jsonObject = JSONObject(errorBody)
                        val errorMessage = jsonObject.getString("message")
                        //Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<SearchResponse.ResponseShareProf>, t: Throwable) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Share", errorMessage)
            }
        })
    }
}