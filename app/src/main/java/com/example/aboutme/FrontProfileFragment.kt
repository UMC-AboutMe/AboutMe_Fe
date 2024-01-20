package com.example.aboutme

import android.R.id.edit
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.aboutme.databinding.FragmentFrontprofileBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


class FrontProfileFragment : Fragment(),BottomSheet.OnImageSelectedListener, BottomSheet.OnBasicImageSelectedListener, BottomSheet.OnCharImageSelectedListener{

    lateinit var binding: FragmentFrontprofileBinding


    private lateinit var sharedViewModel: SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentFrontprofileBinding.inflate(inflater,container,false)

        binding.turnBtn.setOnClickListener {
            val ft = parentFragmentManager.beginTransaction()

            ft.replace(R.id.profile_frame, BackProfileFragment()).commit()

        }

        //비트맵으로 뷰 저장




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


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        sharedViewModel.storeProfileLayout(binding.profileLayout) // someView는 실제로 전달해야 할 View입니다.
        sharedViewModel.profileLayoutLiveData.observe(viewLifecycleOwner) {
            if (sharedViewModel.storeBitmap.value == true) {
                // 프로필 레이아웃을 비트맵으로 저장하는 로직 수행
                viewSave(it)
                Log.d("bitmap", "success")
            }
        }
    }



    private fun getViewBitmap(view: View): Bitmap {
        val width = view.measuredWidth
        val height = view.measuredHeight

        // 크기가 0 또는 음수인 경우 예외 처리
        if (width <= 0 || height <= 0) {
            Log.e("FrontProfileFragment", "Invalid view size: width=$width, height=$height")
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

    private fun viewSave(view: View) {
        val bitmap = getViewBitmap(view)
        val filePath = getSaveFilePathName()
        bitmapFileSave(bitmap, filePath)
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

}

