package com.example.aboutme.Myprofile

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.RetrofitMyprofileData.MainProfileData
import com.example.aboutme.RetrofitMyprofileData.PatchMyprofile
import com.example.aboutme.RetrofitMyprofileData.PostProfile
import com.example.aboutme.RetrofitMyprofileData.RequestPatchProfile
import com.example.aboutme.RetrofitMyprofileData.ResponsePostProfile
import com.example.aboutme.databinding.FragmentFrontprofileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date



class FrontProfileFragment : Fragment() {

    companion object {
        fun newInstance(positionId: Int): FrontProfileFragment {
            val fragment = FrontProfileFragment()
            val args = Bundle().apply {
                putInt("positionId", positionId)
            }
            fragment.arguments = args
            return fragment
        }
    }


    lateinit var binding: FragmentFrontprofileBinding


    private lateinit var sharedViewModel: SharedViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentFrontprofileBinding.inflate(inflater,container,false)


        binding.turnBtn.setOnClickListener {
            // 프로필 ID 가져오기
            val positionId = arguments?.getInt("positionId", -1)

            // BackProfileFragment로 전환하기 위해 프로필 ID를 번들에 담아서 생성
            val backProfileFragment = BackProfileFragment.newInstance(positionId ?: -1)

            // 프로필 ID를 담은 번들을 BackProfileFragment로 전달
            backProfileFragment.arguments = arguments

            // BackProfileFragment로 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.profile_frame, backProfileFragment)
                .commit()
        }


        val positionId = arguments?.getInt("positionId", -1)
        Log.d("FrontProfileFragment!", "Profile ID: $positionId")

        val pref = requireContext().getSharedPreferences("pref", 0)
        val token = pref.getString("Gtoken", null) ?: ""
        //RetrofitClient.initialize(token)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val positionId = arguments?.getInt("positionId", -1)
        Log.d("FrontProfileFragment!", "Profile ID: $positionId")

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        sharedViewModel.storeProfileLayout(binding.profileLayout) // someView는 실제로 전달해야 할 View입니다.
        sharedViewModel.profileLayoutLiveData.observe(viewLifecycleOwner) {
            if (sharedViewModel.storeBitmap.value == true) {
                // 프로필 레이아웃을 비트맵으로 저장하는 로직 수행
                val bitmap = getViewBitmap(it)
                val filePath = getSaveFilePathName()
                bitmapFileSave(bitmap, filePath)
                Log.d("bitmap", "success")
            }
        }

        profilePosion(positionId!!) { realProfileId ->
            Log.d("realprofileID", realProfileId.toString())
            // 여기서 realProfileId를 사용할 수 있습니다.
            refreshData(realProfileId.toString())


        }
    }


    private fun profilePosion(positionId: Int, callback: (Int) -> Unit) {
        val pref = requireContext().getSharedPreferences("pref", 0)
        val token = pref.getString("Gtoken", null) ?: ""

        var realProfileId = -1 // 기본값 설정
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




    private fun refreshData(profileId: String?) {
        val pref = requireContext().getSharedPreferences("pref", 0)
        val token = pref.getString("Gtoken", null) ?: ""

        lifecycleScope.launch {
            try {
                val response: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.getDataAll(token,profileId!!.toLong())
                }

                if (response.isSuccessful) {
                    val responseData: GetAllProfile? = response.body()
                    Log.d("GETALL 성공!!!!", "응답 데이터: $responseData")
                    responseData?.let { applyUpdatedDataToUI(it) }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("GETALL 요청 실패", "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("GETALL 요청 실패", "에러: ${e.message}")
            }
        }
    }

    private fun applyUpdatedDataToUI(updatedData: GetAllProfile) {
        // 변경된 데이터를 UI의 각 요소에 적용
            binding.profileNameEt.setText(updatedData.result.frontFeatures[0].value.toString())

        if (updatedData.result.frontFeatures[1].value != null) {
            binding.profileNumEt.setText(updatedData.result.frontFeatures[1].value.toString())
        } else{
            binding.profileNumEt.setText("")
        }

            if (updatedData.result.profileImage.type == "USER_IMAGE"){
                if (updatedData.result.profileImage.profileImageUrl != null) {
                    Glide.with(this)
                        .load(updatedData.result.profileImage.profileImageUrl)
                        .into(binding.profileIv)
                }
            } else if(updatedData.result.profileImage.type == "DEFAULT"){
                Glide.with(requireContext()).load(R.drawable.profiledefault).into(binding.profileIv)
            } else if (updatedData.result.profileImage.type == "CHARACTER"){
                if (updatedData.result.profileImage.characterType == "2") {
                    Glide.with(requireContext()).load(R.drawable.prof_avater2).into(binding.profileIv)
                }
                if (updatedData.result.profileImage.characterType == "3") {
                    Glide.with(requireContext()).load(R.drawable.prof_avater3).into(binding.profileIv)
                }
                if (updatedData.result.profileImage.characterType == "4") {
                    Glide.with(requireContext()).load(R.drawable.prof_avater4).into(binding.profileIv)
                }
                if (updatedData.result.profileImage.characterType == "5") {
                    Glide.with(requireContext()).load(R.drawable.prof_avater5).into(binding.profileIv)
                }
                if (updatedData.result.profileImage.characterType == "6") {
                    Glide.with(requireContext()).load(R.drawable.prof_avater6).into(binding.profileIv)
                }
                if (updatedData.result.profileImage.characterType == "7") {
                    Glide.with(requireContext()).load(R.drawable.prof_avater7).into(binding.profileIv)
                }
                if (updatedData.result.profileImage.characterType == "8") {
                    Glide.with(requireContext()).load(R.drawable.prof_avater8).into(binding.profileIv)
                }
                if (updatedData.result.profileImage.characterType == "9") {
                    Glide.with(requireContext()).load(R.drawable.prof_avater9).into(binding.profileIv)
                }
            }



        // 예시: 변경된 데이터가 로그에 출력되도록 함
        Log.d("UpdatedData", "Updated data applied to UI: $updatedData")
    }



    private fun getViewBitmap(view: View): Bitmap {
        // View의 크기를 측정
        view.measure(
            View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(view.height, View.MeasureSpec.EXACTLY)
        )

        val width = view.measuredWidth
        val height = view.measuredHeight


        // 측정된 폭과 높이가 0일 경우 처리
        if (width <= 0 || height <= 0) {
            Log.e("FrontProfileFragment", "Invalid view size: width=$width, height=$height")
            // 너비와 높이가 0일 경우에 대한 예외 처리를 추가하거나 기본값으로 처리
            // 예: width = defaultWidth, height = defaultHeight
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        Log.d("FrontProfileFragment", "Bitmap created successfully: width=$width, height=$height")


        return bitmap
    }

    private fun getSaveFilePathName() : String{
        val folder =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString()
        val fileName = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        return "$folder/$fileName.jpg"
    }

    private fun bitmapFileSave(bitmap: Bitmap, path: String) {
        val fos: FileOutputStream
        try {
            fos = FileOutputStream(File(path))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}

