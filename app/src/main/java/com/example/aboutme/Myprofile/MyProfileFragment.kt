package com.example.aboutme.Myprofile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.DeleteMyprofile
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.RetrofitMyprofileData.MainProfileData
import com.example.aboutme.RetrofitMyprofileData.PostProfile
import com.example.aboutme.RetrofitMyprofileData.ResponsePostProfile
import com.example.aboutme.bottomNavigationView
import com.example.aboutme.databinding.FragmentMyprofileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyProfileFragment : Fragment(), BottomSheet2.OnBottomSheetListener {

    lateinit var binding: FragmentMyprofileBinding

    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var mActivity: MainActivity2


    private var mListener: BottomSheet2.OnBottomSheetListener? = null

    companion object {
        // newInstance 메서드 추가
        fun newInstance(positionId: Int): MyProfileFragment {
            val fragment = MyProfileFragment()
            val args = Bundle().apply {
                putInt("positionId", positionId)
            }
            fragment.arguments = args
            return fragment
        }
    }


    fun setOnBottomSheetListener(listener: BottomSheet2.OnBottomSheetListener) {
        mListener = listener
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding=FragmentMyprofileBinding.inflate(inflater,container,false)


        setFrag(0)

        val positionId = arguments?.getInt("positionId", -1)
        Log.d("MyProfileFragment", "Position ID: $positionId")



        binding.myprofileShareBtn.setOnClickListener {
            val bottomSheet2 = BottomSheet2()
            mListener?.let {
                bottomSheet2.setOnBottomSheetListener(it)
            }

            bottomSheet2.show(childFragmentManager, bottomSheet2.tag)
        }

        val pref = requireContext().getSharedPreferences("pref", 0)
        val token = pref.getString("Gtoken", null) ?: ""


        return binding.root
    }



    fun setActivity(activity: MainActivity2) {
        mActivity = activity
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val positionId = arguments?.getInt("positionId", -1)
        Log.d("MyProfileFragment", "Position ID: $positionId")

        //공유 버튼 클릭 시 이벤트 발생
        /*binding.myprofileShareBtn.setOnClickListener {

            val bottomSheet2 = BottomSheet2()

            bottomSheet2.show(childFragmentManager, bottomSheet2.tag)
        }*/

        profilePosion(positionId!!) { realProfileId ->
            Log.d("realprofileID", realProfileId.toString())

            binding.deleteButtonIv.setOnClickListener{
                Log.d("delete!!","success")
                deleteProfile(realProfileId)

                val intent = Intent(activity,bottomNavigationView::class.java)
                startActivity(intent)

            }

            binding.backBtn.setOnClickListener {
                val intent = Intent(activity,bottomNavigationView::class.java)
                startActivity(intent)
            }

            binding.myprofileShareBtn.setOnClickListener {

                val bottomSheet2 = BottomSheet2()

                val bundle = Bundle().apply {
                    putInt("realProfileId", realProfileId)
                }
                // 다이얼로그 프래그먼트를 호출하여 번들을 전달
                bottomSheet2.arguments = bundle
                bottomSheet2.show(childFragmentManager, bottomSheet2.tag)
            }

        }

    }

    private fun setFrag(fragNum: Int) {
        val positionId = arguments?.getInt("positionId", -1)
        if (positionId != null) { // positionId가 null이 아닌 경우에만 처리
            val ft = childFragmentManager.beginTransaction()
            when (fragNum) {
                0 -> {
                    Log.d("MyProfileFragment", "FrontProfileFragment로 교체 중")
                    val frontProfileFragment = FrontProfileFragment.newInstance(positionId)
                    ft.replace(R.id.profile_frame, frontProfileFragment).commit()
                }
                /*1 -> {
                    Log.d("MyProfileFragment", "BackProfileFragment로 교체 중")
                    val backProfileFragment = BackProfileFragment.newInstance(positionId)
                    ft.replace(R.id.profile_frame, backProfileFragment).commit()
                }*/
            }
        } else {
            Log.e("MyProfileFragment", "positionId가 null입니다.")
        }
    }



    private fun profilePosion(positionId: Int, callback: (Int) -> Unit) {
        var realProfileId = -1 // 기본값 설정
        val pref = requireContext().getSharedPreferences("pref", 0)
        val token = pref.getString("Gtoken", null) ?: ""

        RetrofitClient.mainProfile.getData(token).enqueue(object : Callback<MainProfileData> {
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

    private fun deleteProfile(profileId : Int){
        val pref = requireContext().getSharedPreferences("pref", 0)
        val token = pref.getString("Gtoken", null) ?: ""
        Log.d("딜리트 토큰",token)

// Coroutine을 사용하여 비동기 호출 수행
        lifecycleScope.launch {
            try {
                // withContext를 사용하여 백그라운드 스레드에서 실행하도록 함
                val response: Response<DeleteMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.deleteData(token,profileId.toLong())
                }

                if (response.isSuccessful) {
                    val responseData: DeleteMyprofile? = response.body()
                    Log.d("Delete 성공", "응답 데이터: $responseData")
                    Toast.makeText(requireContext(),"프로필이 삭제되었습니다.",Toast.LENGTH_SHORT).show()
                    // responseData를 처리하는 로직 작성
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("Delete 요청 실패", "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody")

                }
            } catch (e: Exception) {
                Log.e("Delete 요청 실패", "에러: ${e.message}")
            }
        }

    }


    override fun onBottomSheetAction() {

    }


}