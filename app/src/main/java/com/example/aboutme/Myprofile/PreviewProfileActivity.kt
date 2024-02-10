package com.example.aboutme.Myprofile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.databinding.ActivityEditprofileBinding
import com.example.aboutme.databinding.ActivityPreviewprofileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class PreviewProfileActivity : AppCompatActivity() {


    private val binding by lazy {
        ActivityPreviewprofileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setFrag(0)


        binding.editProfileBackBtn.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            //intent.putExtra("positionId",0)
            startActivity(intent)
        }

        binding.finihEditBtn.setOnClickListener {
            val noEditDialog = NoEditDialogFragment()

            Log.d("!!!!","success")
            noEditDialog.show(supportFragmentManager, noEditDialog.tag)
        }

        /*lifecycleScope.launch {
            try {
                // 백그라운드 스레드에서 Retrofit의 patchProfile 메서드를 호출하고, 결과를 받아옴
                // withContext를 사용하여 백그라운드 스레드에서 실행하도록 함
                val response: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.getDataAll(profileId1!!.toLong())
                }

                if (response.isSuccessful) {

                    // 성공한 응답 데이터를 받아옴
                    val responseData: GetAllProfile? = response.body()
                    Log.d("GETALL 성공", "응답 데이터: $responseData")
                    // responseData를 처리하는 로직 작성


                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("GETALL 요청 실패", "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody")

                }
            } catch (e: Exception) {
                Log.e("GETALL 요청 실패", "에러: ${e.message}")
            }
        }*/
    }

    private fun setFrag(fragNum : Int){
        val ft = supportFragmentManager.beginTransaction()
        when(fragNum)
        {
            0 -> {
                Log.d("MyProfileFragment", "FrontProfileFragment로 교체 중")
                ft.replace(R.id.profile_frame2, FrontProfilePreviewFragment()).commit()
            }

            1 -> {
                Log.d("MyProfileFragment", "BackProfileFragment로 교체 중")
                ft.replace(R.id.profile_frame2, BackProfilePreviewFragment()).commit()
            }
        }
    }

}