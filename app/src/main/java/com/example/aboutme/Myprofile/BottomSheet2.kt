package com.example.aboutme.Myprofile

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.databinding.FragmentSharebottomsheet2Binding
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.ItemContent
import com.kakao.sdk.template.model.Link
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


class BottomSheet2 : DialogFragment() {

    lateinit var binding: FragmentSharebottomsheet2Binding
    private lateinit var sharedViewModel: SharedViewModel

    private var serialNumber: Int? = null
    private var profileName : String? = null


    private val TAG = "BottomSheet2"

    var savedImageUri: Uri? = null
        private set

    interface OnBottomSheetListener {
        fun onBottomSheetAction()
    }

    private var listener: OnBottomSheetListener? = null

    fun setOnBottomSheetListener(listener: OnBottomSheetListener) {
        this.listener = listener
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.d(TAG, "Fragment isAdded: $isAdded")

        binding.shareBottomSheet2KakaoBtn.setOnClickListener {
            listener?.onBottomSheetAction()

                context?.let { nonNullContext ->
                    // 카카오톡 설치여부 확인
                    if (isAdded) { // Fragment가 Activity에 추가되었는지 확인
                        ShareClient.instance.shareDefault(
                            nonNullContext,
                            defaultFeed
                        ) { sharingResult, error ->
                            if (error != null) {
                                Toast.makeText(
                                    nonNullContext,
                                    "카카오톡이 설치되어있지 않습니다",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultFeed)
                                Log.e(TAG, "카카오톡 공유 실패", error)
                                // 카카오톡 공유 실패 시 웹으로 공유 시도
                                try {
                                    KakaoCustomTabsClient.openWithDefault(nonNullContext, sharerUrl)
                                } catch (e: UnsupportedOperationException) {
                                    // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
                                }

                                // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
                                try {
                                    KakaoCustomTabsClient.open(nonNullContext, sharerUrl)
                                } catch (e: ActivityNotFoundException) {
                                    // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
                                }

                                dismiss()

                            } else if (sharingResult != null) {
                                Log.d(TAG, "카카오톡 공유 성공 ${sharingResult.intent}")
                                startActivity(sharingResult.intent)

                                // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                                Log.w(TAG, "Warning Msg: ${sharingResult.warningMsg}")
                                Log.w(TAG, "Argument Msg: ${sharingResult.argumentMsg}")

                                dismiss()
                            }
                        }
                    }
                }
            }

        }

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

        initDefaultFeed()

        val realProfileId = arguments?.getInt("realProfileId", -1)
        Log.d("다이얼로그id",realProfileId.toString())

        binding.shareBottomSheet2ImageBtn.setOnClickListener {
            sharedViewModel.profileLayoutLiveData.value?.let { profileLayout ->
                savedImageUri = viewSave(profileLayout)
                Toast.makeText(requireContext(),"이미지가 파일에 저장되었습니다.", Toast.LENGTH_SHORT ).show()
                Log.d("bottomclick", "Image saved successfully")
            } ?: run {
                Log.d("bottomclick", "profileLayout is null")
            }
            dismiss()
        }


        binding.shareBottomSheet2InstargramBtn.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                sharedViewModel.profileLayoutLiveData.value?.let { profileLayout ->
                    val viewBitmap = getViewBitmap(profileLayout)
                    val viewUri = saveImageOnAboveAndroidQ(viewBitmap)

                    val bgBitmap = drawBackgroundBitmap()
                    val bgUri = saveImageOnAboveAndroidQ(bgBitmap)

                    instaShare(bgUri, viewUri)
                    Log.d("insta!!", "success")
                }

            } else {
                // 외부 저장소 읽기 권한 체크
                val readPermission = ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )

                if (readPermission != PackageManager.PERMISSION_GRANTED) {
                    // 권한이 부여되지 않은 경우 권한 요청
                    val requestReadExternalStorageCode = 2

                    val permissionStorage = arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )

                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        permissionStorage,
                        requestReadExternalStorageCode
                    )
                } else {
                    // 이미 권한이 있는 경우에 수행할 동작
                    sharedViewModel.profileLayoutLiveData.value?.let { profileLayout ->
                        val viewBitmap = getViewBitmap(profileLayout)
                        val viewUri = saveImageOnUnderAndroidQ(viewBitmap)

                        val bgBitmap = drawBackgroundBitmap()
                        val bgUri = saveImageOnUnderAndroidQ(bgBitmap)

                        instaShare(bgUri,viewUri)
                    }
                }
            }
            dismiss()
        }

        binding.numberPasteBtn.setOnClickListener {
            val clip = ClipData.newPlainText("클립보드 복사", serialNumber.toString())
            val clipboard: ClipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()

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
    private fun saveImageOnAboveAndroidQ(bitmap: Bitmap): Uri? {
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


    fun instaShare(bgUri: Uri?, viewUri: Uri?) {

        val stickerAssetUri = Uri.parse(viewUri.toString())
        val sourceApplication = "com.example.aboutme"

        val intent = Intent("com.instagram.share.ADD_TO_STORY")
        intent.putExtra("source_application", sourceApplication)

        intent.type = "image/png"
        intent.setDataAndType(bgUri, "image/png")
        intent.putExtra("interactive_asset_uri", stickerAssetUri)


        requireActivity().grantUriPermission(
            "com.instagram.android", stickerAssetUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        /*requireActivity().grantUriPermission(
            "com.instagram.android", viewUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
        )*/

        requireActivity().grantUriPermission(
            "com.instagram.android", bgUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        try {
            this.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext().applicationContext,
                "인스타그램 앱이 존재하지 않습니다.",
                Toast.LENGTH_SHORT
            ).show()

            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/"))
            startActivity(webIntent)
        }
        try {
            //저장해놓고 삭제한다.
            Thread.sleep(1000)
            viewUri?.let { uri -> requireContext().contentResolver.delete(uri, null, null) }
            bgUri?.let { uri -> requireContext().contentResolver.delete(uri, null, null) }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun saveImageOnUnderAndroidQ(bitmap: Bitmap): Uri? {
        val fileName = System.currentTimeMillis().toString() + ".png"
        val externalStorage = Environment.getExternalStorageDirectory().absolutePath
        val path = "$externalStorage/DCIM/imageSave"
        val dir = File(path)

        if (dir.exists().not()) {
            dir.mkdirs() //폴더 없는 경우 폴더 생성
        }

        val fileItem = File("$dir/$fileName")

        try {
            fileItem.createNewFile() // 0kB 파일 생성

            val fos = FileOutputStream(fileItem)

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos) //비트맵 압축

            fos.close()

            MediaScannerConnection.scanFile(
                requireContext().applicationContext,
                arrayOf(fileItem.toString()),
                null,
                null
            )
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()

        }

        return FileProvider.getUriForFile(
            requireContext().applicationContext,
            "com.example.aboutme.fileprovider",
            fileItem
        )
    }

    private fun drawBackgroundBitmap(): Bitmap {
        // 기기 해상도를 가져옴.
        val backgroundWidth = resources.displayMetrics.widthPixels
        val backgroundHeight = resources.displayMetrics.heightPixels

        val backgroundBitmap = Bitmap.createBitmap(backgroundWidth, backgroundHeight, Bitmap.Config.ARGB_8888) // 비트맵 생성
        val canvas = Canvas(backgroundBitmap) // 캔버스에 비트맵을 Mapping.

        // 배경색을 흰색으로 지정
        val bgColor = ContextCompat.getColor(requireContext(), android.R.color.white)
        canvas.drawColor(bgColor) // 캔버스에 현재 설정된 배경화면색으로 칠한다.

        return backgroundBitmap
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            2 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 부여된 경우에 수행할 동작
                    sharedViewModel.profileLayoutLiveData.value?.let { profileLayout ->
                        val viewBitmap = getViewBitmap(profileLayout)
                        val viewUri = saveImageOnUnderAndroidQ(viewBitmap)

                        val bgBitmap = drawBackgroundBitmap()
                        val bgUri = saveImageOnUnderAndroidQ(bgBitmap)

                        instaShare(bgUri,viewUri)
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "외부 저장소 읽기 권한이 거부되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private val defaultFeed: FeedTemplate by lazy {
        val pref = requireContext().getSharedPreferences("pref", 0)
        val token = pref.getString("Gtoken", null) ?: ""

        lifecycleScope.launch {

            val realProfileId = arguments?.getInt("realProfileId", -1)
            try {
                val response: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.getDataAll(token,realProfileId!!.toLong())
                }

                if (response.isSuccessful) {
                    val responseData: GetAllProfile? = response.body()
                    Log.d("GETALL 성공~~", "응답 데이터: $responseData")
                    // API 응답에서 serialNumber 값을 받아와서 초기화합니다.
                    serialNumber = responseData?.result?.serialNumber
                    Log.d("씨리얼", serialNumber.toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("GETALL 요청 실패", "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("GETALL 요청 실패", "에러: ${e.message}")
            }
        }

        // serialNumber 값을 사용하여 defaultFeed를 초기화합니다.
        FeedTemplate(
            content = Content(
                title = "$profileName 님의 AboutMe 프로필을 확인해보세요.",
                description = "홈 화면에 있는 프로필 찾기에 일련번호를 입력하면 프로필을 찾을 수 있어요.",
                imageUrl = "https://i.imgur.com/PsidRUL.jpg",
                link = Link(
                    webUrl = "https://play.google.com",
                    mobileWebUrl = "https://play.google.com"
                )
            ),
            itemContent = ItemContent(
                profileText = "일련번호: $serialNumber" // serialNumber 값을 사용합니다.
            ),
            buttons = listOf(
                Button(
                    "앱 다운로드",
                    Link(
                        webUrl = "https://play.google.com",
                        mobileWebUrl = "https://play.google.com"
                    )
                ),
                Button(
                    "앱으로 이동",
                    Link(
                        //이 부분을 사용해서 어떤 상세페이지를 띄울지 결정
                        androidExecutionParams = mapOf("key1" to "value1")
                    )
                )
            )
        )
    }




    private fun initDefaultFeed() {
        val pref = requireContext().getSharedPreferences("pref", 0)
        val token = pref.getString("Gtoken", null) ?: ""

        lifecycleScope.launch {
            val realProfileId = arguments?.getInt("realProfileId", -1)
            try {
                val response: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.getDataAll(token,realProfileId!!.toLong())
                }

                if (response.isSuccessful) {
                    val responseData: GetAllProfile? = response.body()
                    Log.d("GETALL 성공~~", "응답 데이터: $responseData")
                    // API 응답에서 serialNumber 값을 받아와서 초기화합니다.
                    serialNumber = responseData?.result?.serialNumber
                    profileName = responseData!!.result.frontFeatures[0]!!.value
                    Log.d("이름~",profileName.toString())
                    Log.d("씨리얼", serialNumber.toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e(
                        "GETALL 요청 실패",
                        "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody"
                    )
                }
            } catch (e: Exception) {
                Log.e("GETALL 요청 실패", "에러: ${e.message}")
            }
        }
    }
}
