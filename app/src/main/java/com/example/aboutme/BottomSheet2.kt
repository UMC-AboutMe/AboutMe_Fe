package com.example.aboutme

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.aboutme.databinding.FragmentSharebottomsheet2Binding
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date

class BottomSheet2 : DialogFragment() {

    lateinit var binding: FragmentSharebottomsheet2Binding
    private lateinit var sharedViewModel: SharedViewModel
    var savedImageUri: Uri? = null
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSharebottomsheet2Binding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bottomsheetbox)
        dialog?.window?.setGravity(Gravity.TOP)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        binding.shareBottomSheet2ImageBtn.setOnClickListener {
            sharedViewModel.profileLayoutLiveData.value?.let { profileLayout ->
                savedImageUri = viewSave(profileLayout)
                Log.d("bottomclick", "Image saved successfully")
            } ?: run {
                Log.d("bottomclick", "profileLayout is null")
            }
            dismiss()
        }

        binding.shareBottomSheet2InstargramBtn.setOnClickListener {
            sharedViewModel.profileLayoutLiveData.value?.let { profileLayout ->
                val viewBitmap = getViewBitmap(profileLayout)
                val viewUri = saveImageOnAboveAndroidQ(viewBitmap)

                instaShare(viewUri)
                Log.d("insta!!","success")
            }
            dismiss()
        }
    }

    private fun viewSave(view: View): Uri {
        val bitmap = getViewBitmap(view)
        val filePath = getSaveFilePathName()
        bitmapFileSave(bitmap, filePath)
        return Uri.fromFile(File(filePath))
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
        params?.height = (deviceHeight * 0.25).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageOnAboveAndroidQ(bitmap: Bitmap) : Uri? {
        val fileName = System.currentTimeMillis().toString() + ".Png"

        val contentValues = ContentValues()
        contentValues.apply {
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/ImageSave")
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val uri = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        try {
            if (uri != null) {
                val image = requireContext().contentResolver.openFileDescriptor(uri, "w", null)

                if (image != null) {
                    val fos = FileOutputStream(image.fileDescriptor)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)

                    fos.close()

                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    requireContext().contentResolver.update(uri, contentValues, null, null)
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return uri
    }

    fun instaShare(viewUri: Uri?){

        val stickerAssetUri = Uri.parse(viewUri.toString())
        val sourceApplication = "com.example.aboutme"

        val intent = Intent("com.instagram.share.ADD_TO_STORY")
        intent.putExtra("source_application", sourceApplication)

        intent.type = "image/png"
        intent.setDataAndType(viewUri, "image/png")
        intent.putExtra("interactive_asset_uri", stickerAssetUri)

        requireActivity().grantUriPermission(
            "com.instagram.android", stickerAssetUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        requireActivity().grantUriPermission(
            "com.instagram.android", viewUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        try {
            this.startActivity(intent)
        } catch (e : ActivityNotFoundException) {
            Toast.makeText(requireContext().applicationContext, "인스타그램 앱이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()

            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/"))
            startActivity(webIntent)
        }
        try{
            //저장해놓고 삭제한다.
            Thread.sleep(1000)
            viewUri?.let { uri -> requireContext().contentResolver.delete(uri, null, null) }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

}
