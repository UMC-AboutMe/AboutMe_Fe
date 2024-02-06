package com.example.aboutme.Myprofile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.DeleteMyprofile
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.RetrofitMyprofileData.PostProfile
import com.example.aboutme.RetrofitMyprofileData.ResponsePostProfile
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


// Coroutine을 사용하여 비동기 호출 수행
        lifecycleScope.launch {
            try {
                // withContext를 사용하여 백그라운드 스레드에서 실행하도록 함
                val response: Response<DeleteMyprofile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.deleteData(9)
                }

                if (response.isSuccessful) {
                    val responseData: DeleteMyprofile? = response.body()
                    Log.d("Delete 성공", "응답 데이터: $responseData")
                    // responseData를 처리하는 로직 작성
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("Delete 요청 실패", "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody")

                }
            } catch (e: Exception) {
                Log.e("Delete 요청 실패", "에러: ${e.message}")
            }
        }

        return binding.root
    }



    fun setActivity(activity: MainActivity2) {
        mActivity = activity
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        //공유 버튼 클릭 시 이벤트 발생
        binding.myprofileShareBtn.setOnClickListener {

            val bottomSheet2 = BottomSheet2()

            bottomSheet2.show(childFragmentManager, bottomSheet2.tag)
        }

    }

    private fun setFrag(fragNum : Int){
        val ft = childFragmentManager.beginTransaction()
        when(fragNum)
        {
            0 -> {
                Log.d("MyProfileFragment", "FrontProfileFragment로 교체 중")
                ft.replace(R.id.profile_frame, FrontProfileFragment()).commit()
            }

            1 -> {
                Log.d("MyProfileFragment", "BackProfileFragment로 교체 중")
                ft.replace(R.id.profile_frame, BackProfileFragment()).commit()
            }
        }
    }

    override fun onBottomSheetAction() {

    }




}