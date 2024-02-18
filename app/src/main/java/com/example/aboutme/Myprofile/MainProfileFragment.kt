package com.example.aboutme.Myprofile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofileData.FrontFeature
import com.example.aboutme.RetrofitMyprofileData.MainProfileData
import com.example.aboutme.databinding.FragmentMainprofileBinding
import com.example.aboutme.databinding.ItemMultiprofileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Url
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MainProfileFragment : Fragment() {

    lateinit var binding: FragmentMainprofileBinding
    lateinit var binding2: ItemMultiprofileBinding
    private val multiList = mutableListOf<MultiProfileData>() // 전역 변수로 multiList 선언
    private lateinit var vpadapter : MainProfileVPAdapter

    private var vpCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainprofileBinding.inflate(inflater, container, false)

        binding2 = ItemMultiprofileBinding.inflate(inflater, container, false)

        vpadapter = MainProfileVPAdapter(requireContext())

        val pref = requireContext().getSharedPreferences("pref", 0)
        val token = pref.getString("Gtoken", null) ?: ""
        //RetrofitClient.initialize(token)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initViewPager()

        binding.mainProfileVp.offscreenPageLimit = 1 // 몇 개의 페이지를 미리 로드 해둘것인지
        binding.mainProfileVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        binding.vpRealIndicator.setViewPager2(binding.mainProfileVp) // DotsIndicator와 ViewPager2를 연결


        binding.mainProfileVp.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    // 페이지가 선택될 때마다 바탕화면의 색을 변경합니다.
                    changeBackgroundImage(position)
                }
            })

        }

        private fun changeBackgroundImage(position: Int) {
            val images = arrayOf(
                R.drawable.vp_bg,
                R.drawable.vp_bg2,
                R.drawable.vp_bg,
                R.drawable.vp_bg2,
            )

            // 해당 페이지에 맞는 이미지를 설정합니다.
            val image = images[position % images.size]

            // 이미지를 배경으로 설정합니다.
            binding.root.setBackgroundResource(image)
        }

        private fun initViewPager() {

            //vpadapter = MainProfileVPAdapter()

            binding.mainProfileVp.adapter = vpadapter

            val pageMarginPx =
                resources.getDimensionPixelOffset(R.dimen.viewpager_page_margin) // dimen 파일 안에 크기를 정의해두었다.
            val pagerWidth = resources.getDimensionPixelOffset(R.dimen.pageWidth) // dimen 파일이 없으면 생성해야함
            val screenWidth = resources.displayMetrics.widthPixels // 스마트폰의 너비 길이를 가져옴
            val offsetPx = screenWidth - pageMarginPx - pagerWidth

            val pref = requireContext().getSharedPreferences("pref", 0)
            val token = pref.getString("Gtoken", null) ?: ""


            RetrofitClient.mainProfile.getData(token).enqueue(object : Callback<MainProfileData> {
                // 서버 통신 실패 시의 작업
                override fun onFailure(call: Call<MainProfileData>, t: Throwable) {
                    Log.e("실패", t.toString())
                }

                override fun onResponse(
                    call: Call<MainProfileData>,
                    response: Response<MainProfileData>
                ) {
                    val repos: MainProfileData? = response.body()
                    if (repos != null) {
                        val frontFeatures: List<FrontFeature>? =
                            repos.result.myprofiles?.flatMap { profile ->
                                profile.frontFeatures
                            }
                        val totalMyProfile = repos.getTotalMyProfile()

                        if (totalMyProfile == 1){
                            vpCount = 1
                        }

                        if (frontFeatures != null) {
                            multiList.clear()
                            for (profile in repos.result.myprofiles) {
                                val frontFeatures = profile.frontFeatures
                                if (frontFeatures.size > 1) {

                                    if (profile.profileImage.type == "USER_IMAGE") {
                                        if (profile.profileImage.profileImageUrl != null) {
                                            //getBitmapFromURL(profile.profileImage.profileImageUrl) { bitmap ->
                                            //Log.d("비트맵", bitmap.toString())
                                            multiList.add(
                                                MultiProfileData(
                                                    profile.profileImage.profileImageUrl,
                                                    frontFeatures[0].value,
                                                    frontFeatures[1].value
                                                )
                                            )
                                            //}
                                        }
                                        //}
                                    } else if (profile.profileImage.type == "DEFAULT") {
                                        multiList.add(
                                            MultiProfileData(
                                                R.drawable.defaultimage10.toString(),
                                                frontFeatures[0].value,
                                                frontFeatures[1].value

                                            )
                                        )
                                    } else if (profile.profileImage.type == "CHARACTER") {
                                        if (profile.profileImage.characterType == "1") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater1.toString(),
                                                    frontFeatures[0].value,
                                                    frontFeatures[1].value

                                                )
                                            )
                                        }
                                        if (profile.profileImage.characterType == "2") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater2.toString(),
                                                    frontFeatures[0].value,
                                                    frontFeatures[1].value

                                                )
                                            )
                                        }
                                        if (profile.profileImage.characterType == "3") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater3.toString(),
                                                    frontFeatures[0].value,
                                                    frontFeatures[1].value

                                                )
                                            )
                                        }
                                        if (profile.profileImage.characterType == "4") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater4.toString(),
                                                    frontFeatures[0].value,
                                                    frontFeatures[1].value

                                                )
                                            )
                                        }
                                        if (profile.profileImage.characterType == "5") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater5.toString(),
                                                    frontFeatures[0].value,
                                                    frontFeatures[1].value

                                                )
                                            )
                                        }
                                        if (profile.profileImage.characterType == "6") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater6.toString(),
                                                    frontFeatures[0].value,
                                                    frontFeatures[1].value

                                                )
                                            )
                                        }
                                        if (profile.profileImage.characterType == "7") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater7.toString(),
                                                    frontFeatures[0].value,
                                                    frontFeatures[1].value

                                                )
                                            )
                                        }
                                        if (profile.profileImage.characterType == "8") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater8.toString(),
                                                    frontFeatures[0].value,
                                                    frontFeatures[1].value

                                                )
                                            )
                                        }
                                        if (profile.profileImage.characterType == "9") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater9.toString(),
                                                    frontFeatures[0].value,
                                                    frontFeatures[1].value

                                                )
                                            )
                                        }
                                    } else {
                                        multiList.add(
                                            MultiProfileData(
                                                R.drawable.frontprofile_basic.toString(),
                                                frontFeatures[0].value,
                                                frontFeatures[1].value

                                            )
                                        )
                                    }
                                } else {

                                    if (profile.profileImage.type == "USER_IMAGE") {
                                        if (profile.profileImage.profileImageUrl != null) {

                                            //getBitmapFromURL(profile.profileImage.profileImageUrl) { galleryImage ->

                                            multiList.add(
                                                MultiProfileData(
                                                    profile.profileImage.profileImageUrl,
                                                    //R.drawable.prof_avater2.toString(),
                                                    frontFeatures[0].value,
                                                    ""
                                                )
                                            )
                                        }
                                        //}
                                    } else if (profile.profileImage.type == "DEFAULT") {
                                        multiList.add(
                                            MultiProfileData(
                                                R.drawable.defaultimage10.toString(),
                                                frontFeatures[0].value,
                                                ""

                                            )
                                        )
                                    } else if (profile.profileImage.type == "CHARACTER") {
                                        if (profile.profileImage.characterType == "1") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater1.toString(),
                                                    frontFeatures[0].value,
                                                    ""
                                                )
                                            )
                                        }
                                        if (profile.profileImage.characterType == "2") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater2.toString(),
                                                    frontFeatures[0].value,
                                                    ""

                                                )
                                            )
                                        }
                                        if (profile.profileImage.characterType == "3") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater3.toString(),
                                                    frontFeatures[0].value,
                                                    ""

                                                )
                                            )
                                        }
                                        if (profile.profileImage.characterType == "4") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater4.toString(),
                                                    frontFeatures[0].value,
                                                    ""

                                                )
                                            )
                                        }
                                        if (profile.profileImage.characterType == "5") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater5.toString(),
                                                    frontFeatures[0].value,
                                                    ""

                                                )
                                            )
                                        }
                                        if (profile.profileImage.characterType == "6") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater6.toString(),
                                                    frontFeatures[0].value,
                                                    ""

                                                )
                                            )
                                        }
                                        if (profile.profileImage.characterType == "7") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater7.toString(),
                                                    frontFeatures[0].value,
                                                    ""

                                                )
                                            )
                                        }
                                        if (profile.profileImage.characterType == "8") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater8.toString(),
                                                    frontFeatures[0].value,
                                                    ""

                                                )
                                            )
                                        }
                                        if (profile.profileImage.characterType == "9") {
                                            multiList.add(
                                                MultiProfileData(
                                                    R.drawable.prof_avater9.toString(),
                                                    frontFeatures[0].value,
                                                    ""

                                                )
                                            )
                                        }
                                    } else {
                                        multiList.add(
                                            MultiProfileData(
                                                R.drawable.frontprofile_basic.toString(),
                                                frontFeatures[0].value,
                                                ""

                                            )
                                        )
                                    }
                                }

                            }

                            // 어댑터에 업데이트된 multiList를 제출합니다.
                            vpadapter.submitList(multiList)

                            if (vpCount == 1){
                                Log.d("실행", vpCount.toString())
                            } else{
                                binding.mainProfileVp.setPageTransformer { page, position ->
                                    page.setTranslationX(position * -offsetPx)
                                }
                                Log.d("실행2",vpCount.toString())
                            }

                            Log.d("성공티비", "success")
                            Log.d("FrontFeature List", multiList.toString())
                        } else {
                            Log.e("실패", "front_features 데이터가 null입니다.")
                        }
                    } else {
                        Log.e("실패", "응답 데이터가 null입니다.")
                        Log.e("Response", "${response.code()}")
                    }
                    binding.mainProfileVp.postDelayed({
                        binding.mainProfileVp.setCurrentItem(0, false)
                    }, 50)
                }
            })


        }
}