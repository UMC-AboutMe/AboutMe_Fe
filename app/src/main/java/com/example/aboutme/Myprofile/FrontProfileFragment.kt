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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
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



class FrontProfileFragment : Fragment(), BottomSheet.OnImageSelectedListener,
    BottomSheet.OnBasicImageSelectedListener, BottomSheet.OnCharImageSelectedListener {


    lateinit var binding: FragmentFrontprofileBinding


    private lateinit var sharedViewModel: SharedViewModel


    private lateinit var profileEditName: EditText
    private val retrofitClient = RetrofitClient.mainProfile


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentFrontprofileBinding.inflate(inflater,container,false)

        profileEditName = binding.profileNameEt

        binding.turnBtn.setOnClickListener {
            val ft = parentFragmentManager.beginTransaction()

            ft.replace(R.id.profile_frame, BackProfileFragment()).commit()

        }


        //다이얼로그에서 버튼 클릭->프로필사진변경
        binding.profileIv.setOnClickListener {

            val bottomSheet = BottomSheet()

            bottomSheet.setOnImageSelectedListener(this@FrontProfileFragment)
            bottomSheet.setOnBasicImageSelectedListener(this@FrontProfileFragment)
            bottomSheet.setOnCharImageSelectedListener(this@FrontProfileFragment)

            bottomSheet.show(childFragmentManager, bottomSheet.tag)

        }



        val str: String = binding.profileNameEt.text.toString()


        binding.profileNameEt.setOnClickListener {

            if (str.isEmpty()) {
                Toast.makeText(requireContext(), "이름은 필수 입력사항입니다", Toast.LENGTH_LONG).show()
            }

        }

        val profileEdit1  = binding.profileNameEt
        val profileBtn1 : ImageButton = binding.frontProfileEdit1Btn


        var message1 : String = ""

        profileEdit1.addTextChangedListener (object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                message1 = profileEdit1.text.toString()

                if (message1.isNotEmpty()){
                    profileBtn1.isEnabled = false
                    profileBtn1.alpha = 0.1f
                }else{
                    profileBtn1.isEnabled = true
                    profileBtn1.alpha = 1.0f
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        val profileEdit2 : EditText = binding.profileNumEt
        val profileBtn2 : ImageButton = binding.frontProfileEdit2Btn


        var message2 : String = ""

        profileEdit2.addTextChangedListener (object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                message2 = profileEdit2.text.toString()

                if (message2.isNotEmpty()){
                    profileBtn2.isEnabled = false
                    profileBtn2.alpha = 0.1f
                }else{
                    profileBtn2.isEnabled = true
                    profileBtn2.alpha = 1.0f
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        // Retrofit 인스턴스 가져오기


        //val savedName = getSavedName()
        //profileEdit1.setText(savedName)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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



        /*profileEditName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // EditText의 값이 변경될 때마다 데이터 객체를 생성하고 Retrofit을 통해 서버로 전송합니다.
                val name = s.toString()
                sendDataToServer(name)
            }
        })*/

        val saveButton = binding.finishBtn
        saveButton.setOnClickListener {
            // 저장 버튼 클릭 시 EditText의 값을 서버로 전송합니다.
            val name = profileEditName.text.toString()
            sendDataToServer(name)
        }



        val retrofitClient = RetrofitClient.mainProfile



        /*retrofitClient.submitData(postData).enqueue(object : Callback<ResponsePostProfile> {
            override fun onResponse(call: Call<ResponsePostProfile>, response: Response<ResponsePostProfile>) {
                if (response.isSuccessful) {
                    val responseData: ResponsePostProfile? = response.body()
                    Log.d("Post","success")
                    Log.d("Post 성공", "응답 데이터: $responseData")
                    // responseData를 처리하는 로직 작성
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("Post 요청 실패", "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody")

                }
            }

            override fun onFailure(call: Call<ResponsePostProfile>, t: Throwable) {
                Log.e("POST 요청 실패", "통신 에러: ${t.message}")
            }
        })*/


        val patchData = RequestPatchProfile(129, "mbti", "intj")

        lifecycleScope.launch {
            try {
                // withContext를 사용하여 백그라운드 스레드에서 실행하도록 함
                val response: Response<PatchMyprofile> = withContext(Dispatchers.IO) {
                    retrofitClient.patchProfile(19, patchData)
                }

                if (response.isSuccessful) {
                    val responseData: PatchMyprofile? = response.body()
                    Log.d("patch 성공", "응답 데이터: $responseData")
                    // responseData를 처리하는 로직 작성
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("patch 요청 실패", "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody")

                }
            } catch (e: Exception) {
                Log.e("patch 요청 실패", "에러: ${e.message}")
            }
        }


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




    override fun onBasicImageSelected() {
        // drawable에 있는 이미지를 사용하여 프로필 이미지뷰 업데이트
        Glide.with(requireContext()).load(R.drawable.frontprofile_basic).into(binding.profileIv)
    }

    override fun onImageSelected(imageUri: Uri) {
        Glide.with(requireContext()).load(imageUri).into(binding.profileIv)
    }

    override fun onCharImageSelected() {
        Glide.with(requireContext()).load(R.drawable.myprofile_character).into(binding.profileIv)
    }

    private fun saveName(name: String) {
        val sharedPreferences = requireContext().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.commit()
    }

    private fun sendDataToServer(name: String) {
        // Retrofit을 사용하여 서버에 데이터를 전송하는 과정입니다.
        val postData = PostProfile(name)
        retrofitClient.submitData(postData).enqueue(object : Callback<ResponsePostProfile> {
            override fun onResponse(call: Call<ResponsePostProfile>, response: Response<ResponsePostProfile>) {
                if (response.isSuccessful) {
                    val responseData: ResponsePostProfile? = response.body()
                    Log.d("Post", "success")
                    Log.d("Post 성공", "응답 데이터: $responseData")
                    // 성공적으로 서버에 데이터를 전송한 후의 로직을 작성합니다.
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

