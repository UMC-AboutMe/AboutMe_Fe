package com.example.aboutme.Myprofile

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.PostProfile
import com.example.aboutme.RetrofitMyprofileData.ResponsePostProfile
import com.example.aboutme.RetrofitMyprofileData.ResultX
import com.example.aboutme.databinding.ActivityNameDialogBinding
import com.example.aboutme.databinding.FragmentFrontprofileBinding
import com.example.aboutme.databinding.FragmentMainprofileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NameDialogFragment():DialogFragment(){

    lateinit var binding: ActivityNameDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = ActivityNameDialogBinding.inflate(inflater, container, false)


        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //dialog?.window?.setBackgroundDrawableResource(R.drawable.nameedit_box)
        dialog?.window?.setGravity(Gravity.CENTER)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var profileId: String? = null // profileId를 선언

        binding.saveNameBtn.setOnClickListener {

            // 저장 버튼 클릭 시 EditText의 값을 서버로 전송
            val name = binding.nameDialogEdit.text.toString()
            sendDataToServer(name) { responseData ->
                responseData?.result?.let { result ->
                    profileId = result.profileId.toString() // onResponse 콜백 내에서 profileId 값을 설정
                }
                val intent = Intent(activity, EditProfileActivity::class.java)
                Log.d("다이얼로그", "success")
                intent.putExtra("dialogName",name)

                //intent.putExtra("dialogName", name)


                profileId?.let {
                    intent.putExtra("profileId", profileId)
                    Log.d("!!", profileId.toString())
                }
                // 액티비티2로 이동
                startActivity(intent)

                // 다이얼로그 종료
                dismiss()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceHeight = size.y
        val deviceWidth = size . x

        // 다이얼로그의 높이를 디바이스 높이의 25%로 설정
        params?.height = (deviceHeight * 0.25).toInt()
        params?.width = (deviceWidth * 0.85).toInt()

        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    private fun sendDataToServer(name: String, onResponse: (ResponsePostProfile?) -> Unit) {
        // Retrofit을 사용하여 서버에 데이터를 전송
        val postData = PostProfile(name)
        RetrofitClient.mainProfile.submitData(postData).enqueue(object : Callback<ResponsePostProfile> {
            override fun onResponse(call: Call<ResponsePostProfile>, response: Response<ResponsePostProfile>) {
                if (response.isSuccessful) {
                    val responseData: ResponsePostProfile? = response.body()
                    Log.d("Post", "success")
                    Log.d("Post 성공", "응답 데이터: $responseData")
                    // 성공적으로 서버에 데이터를 전송한 후의 로직을 작성합니다.
                    onResponse(responseData)

                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("Post 요청 실패", "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody")
                    // 서버에 데이터 전송에 실패한 경우의 예외 처리를 작성합니다.
                }
            }

            override fun onFailure(call: Call<ResponsePostProfile>, t: Throwable) {
                Log.e("POST 요청 실패", "통신 에러: ${t.message}")
                // 통신 과정에서 예외가 발생한 경우의 예외 처리를 작성합니다.
            }
        })
    }


}