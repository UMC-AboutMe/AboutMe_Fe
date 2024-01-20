package com.example.aboutme

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.aboutme.databinding.FragmentFrontprofileBinding
import com.example.aboutme.databinding.FragmentMyprofileBinding
import com.example.aboutme.databinding.FragmentSharebottomsheet2Binding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class BottomSheet2 : DialogFragment() {

    lateinit var binding : FragmentSharebottomsheet2Binding

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentSharebottomsheet2Binding.inflate(inflater,container,false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.window?.setBackgroundDrawableResource(R.drawable.bottomsheetbox)
        dialog?.window?.setGravity(Gravity.TOP)


        return inflater.inflate(R.layout.fragment_sharebottomsheet2, container, false)
    }

    //
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        //이미지 저장 버튼 클릭시 발생하는 이벤트
        binding.shareBottomSheet2ImageBtn.setOnClickListener {
            sharedViewModel.profileLayoutLiveData.value?.let {
                profileLayout -> viewSave(profileLayout)
            }
        }
    }


    private fun viewSave(view: View) {
        val bitmap = getViewBitmap(view)
        val filePath = getSaveFilePathName()
        bitmapFileSave(bitmap, filePath)
    }

    private fun getViewBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun getSaveFilePathName(): String {
        val folder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
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




    override fun onResume() {
        super.onResume()

        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceHeight = size.y

        // 다이얼로그의 높이를 디바이스 높이의 30%로 설정
        params?.height = (deviceHeight * 0.25).toInt()

        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }



}