package com.example.aboutme.MyprofileStorage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aboutme.MyprofileStorage.api.ProfStorageObj
import com.example.aboutme.MyprofileStorage.api.ProfStorageResponse
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentProfileStorageDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileStorageDetailFragment : Fragment() {
    lateinit var binding: FragmentProfileStorageDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileStorageDetailBinding.inflate(inflater)
        setFrag(0)

        binding.trashButton.setOnClickListener {
            deleteProfiles()
        }
        binding.testbutton.setOnClickListener {
            favProfiles()
        }
        return binding.root
    }

    private fun setFrag(fragNum: Int) {
        val ft = childFragmentManager.beginTransaction()
        when (fragNum) {
            0 -> {
                Log.d("MyProfileFragment", "FrontProfileFragment로 교체 중")
                ft.replace(R.id.profileStorage_frame, ProfileStorageFrontFragment()).commit()
            }

            1 -> {
                Log.d("MyProfileFragment", "BackProfileFragment로 교체 중")
                ft.replace(R.id.profileStorage_frame, ProfileStorageBackFragment()).commit()
            }
        }
    }

    //프로필 보관함 삭제 api
    private fun deleteProfiles(){
        Log.d("Retrofit","delete 함수 호출됨")
        val call = ProfStorageObj.getRetrofitService.deleteProfStorage(4,1)

        call.enqueue(object : Callback<ProfStorageResponse.ResponseDeleteProf> {
            override fun onResponse(
                call: Call<ProfStorageResponse.ResponseDeleteProf>,
                response: Response<ProfStorageResponse.ResponseDeleteProf>
            ) {
                Log.d("Retrofit_Delete", response.toString())
                if (response.isSuccessful) {
                    val response = response.body()
                    Log.d("Retrofit_Delete", response.toString())

                    if (response != null) {
                        if (response.isSuccess) {
                            //성공했을 때
                            Log.d("Retrofit_Delete", "처리에 성공함")
                        } else {
                            //실패했을 때
                            Log.d("Retrofit_Delete", "처리에 실패함")
                        }
                    }
                }
            }

            override fun onFailure(
                call: Call<ProfStorageResponse.ResponseDeleteProf>,
                t: Throwable
            ) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Delete", errorMessage)
            }
        }
        )
    }

    //프로필 보관함 즐겨찾기 등록
    private fun favProfiles() {
        Log.d("Retrofit_Fav", "patch 함수 호출됨")
        val call = ProfStorageObj.getRetrofitService.patchProfStorage(4, 1)

        call.enqueue(object : Callback<ProfStorageResponse.ResponseFavProf> {
            override fun onResponse(
                call: Call<ProfStorageResponse.ResponseFavProf>,
                response: Response<ProfStorageResponse.ResponseFavProf>
            ) {
                Log.d("Retrofit_Fav", response.toString())
                if (response.isSuccessful) { // HTTP 응답 코드가 200번대인지 여부 확인
                    val response = response.body()
                    Log.d("Retrofit", response.toString())

                    if (response != null) {
                        if (response.isSuccess) {
                            //성공했을 때
                            Log.d("Retrofit_Fav", "처리에 성공함")
                        } else {
                            //실패했을 때
                            Log.d("Retrofit_Fav", "처리에 실패함")
                        }
                    }
                }
            }

            override fun onFailure(
                call: Call<ProfStorageResponse.ResponseFavProf>,
                t: Throwable
            ) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Fav", errorMessage)
            }
        })
    }
}
