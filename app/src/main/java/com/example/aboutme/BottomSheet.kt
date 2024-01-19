package com.example.aboutme

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.findFragment
import com.bumptech.glide.Glide
import com.example.aboutme.databinding.FragmentFrontprofileBinding
import com.example.aboutme.databinding.FragmentSharebottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.concurrent.fixedRateTimer


class BottomSheet() : DialogFragment() {

    lateinit var binding : FragmentFrontprofileBinding




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentFrontprofileBinding.inflate(inflater, container, false)


        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.window?.setBackgroundDrawableResource(R.drawable.bottomsheetbox)
        dialog?.window?.setGravity(Gravity.BOTTOM)


        return inflater.inflate(R.layout.fragment_sharebottomsheet, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        

        view?.findViewById<ImageButton>(R.id.shareBottomSheet_image_btn)?.setOnClickListener {
            initImageViewProfile()
        }

        view?.findViewById<ImageButton>(R.id.shareBottomSheet_logo_btn)?.setOnClickListener {
            initBasicImage()
            Log.d("basic", "success")
        }

        view?.findViewById<ImageButton>(R.id.shareBottomSheet_char_btn)?.setOnClickListener {
            initCharImage()
        }

    }

    private fun initImageViewProfile() {

            when {
                // 갤러리 접근 권한이 있는 경우
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                -> {
                    navigateGallery()
                }

                // 갤러리 접근 권한이 없는 경우 & 교육용 팝업을 보여줘야 하는 경우
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                -> {
                    showPermissionContextPopup()
                }

                // 권한 요청 하기(requestPermissions) -> 갤러리 접근(onRequestPermissionResult)
                else -> requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1000
                )
            }
        dismiss()

        }

    private fun initBasicImage(){
        basicImageSelectedListener?.onBasicImageSelected()
    }

    private fun initCharImage(){
        charImageSelectedListener?.onCharImageSelected()
        dismiss()
    }

    interface OnImageSelectedListener {
        fun onImageSelected(imageUri: Uri)
    }

    private var imageSelectedListener: OnImageSelectedListener? = null

    fun setOnImageSelectedListener(listener: OnImageSelectedListener) {
        this.imageSelectedListener = listener
    }

    interface OnBasicImageSelectedListener{
        fun onBasicImageSelected()
    }

    private var basicImageSelectedListener : OnBasicImageSelectedListener? = null

    fun setOnBasicImageSelectedListener(listener: OnBasicImageSelectedListener){
        this.basicImageSelectedListener = listener
    }

    interface OnCharImageSelectedListener{
        fun onCharImageSelected()
    }

    private var charImageSelectedListener : OnCharImageSelectedListener? = null

    fun setOnCharImageSelectedListener(listener: OnCharImageSelectedListener){
        this.charImageSelectedListener = listener
    }

    // 권한 요청 승인 이후 실행되는 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    navigateGallery()
                else
                    Toast.makeText(context, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(context, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()

            }


        }

    }

    private fun navigateGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        // 가져올 컨텐츠들 중에서 Image 만을 가져온다.
        intent.type = "image/*"
        // 갤러리에서 이미지를 선택한 후, 프로필 이미지뷰를 수정하기 위해 갤러리에서 수행한 값을 받아오는 startActivityForeResult를 사용한다.
        startActivityForResult(intent, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 예외처리
        if (resultCode != Activity.RESULT_OK)
            return

        when (requestCode) {
            // 2000: 이미지 컨텐츠를 가져오는 액티비티를 수행한 후 실행되는 Activity 일 때만 수행하기 위해서
            2000 -> {
               /* val frontProfileFragment = parentFragmentManager.findFragmentByTag("FrontProfileFragmentTag") as? FrontProfileFragment
                val ivProfile = frontProfileFragment?.getProfileImageView()*/

                val selectedImageUri: Uri? = data?.data
                Log.d("PutImage1", "success")
                if (selectedImageUri != null) {
                    imageSelectedListener?.onImageSelected(selectedImageUri)
                    Glide.with(requireContext()).load(selectedImageUri).into(binding.profileIv)
                    Log.d("PutImage!", "success")
                } else {
                    Log.d("PutImage", "selectedImageUri is null")
                    Toast.makeText(context, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            else -> {
                Toast.makeText(requireContext(), "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(context)
            .setTitle("권한이 필요합니다.")
            .setMessage("프로필 이미지를 바꾸기 위해서는 갤러리 접근 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }



    override fun onResume() {
        super.onResume()

        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceHeight = size.y

        // 다이얼로그의 높이를 디바이스 높이의 25%로 설정
        params?.height = (deviceHeight * 0.25).toInt()

        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }



}
