package com.example.aboutme.Myprofile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.text.Editable
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.RetrofitMyprofileData.PatchMyprofile
import com.example.aboutme.RetrofitMyprofileData.RequestPatchProfile
import com.example.aboutme.databinding.ActivityEditprofileBinding
import com.example.aboutme.databinding.FragmentEditprofilebackBinding
import com.example.aboutme.databinding.FragmentEditprofilefrontBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    var feature1 : String? = null
    var feature2 : String? = null
    var feature3 : String? = null
    var feature4 : String? = null
    var feature5 : String? = null
    var feature6 : String? = null
    var feature7 : String? = null

    interface TabSelectedListener {
        fun onTabSelected(tabPosition: Int)
    }

    private val viewModel: MyProfileViewModel by viewModels()
    private var editName: String? = null
    private var editNumber: String? = null

    private val binding by lazy {
        ActivityEditprofileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val profileId: String?

        val profileId1 = intent.getStringExtra("profileId")
        val profileId2 = intent.getStringExtra("reProfileId")
        Log.d("reProfileId",profileId2.toString())
        if (profileId1 != null) {
            profileId = profileId1
        } else{
            profileId = profileId2
        }
        Log.d("이것만",profileId.toString())
        lifecycleScope.launch {
            try {
                val response: Response<GetAllProfile> = withContext(Dispatchers.IO) {
                    RetrofitClient.mainProfile.getDataAll(profileId!!.toLong())
                }

                if (response.isSuccessful) {
                    val responseData: GetAllProfile? = response.body()
                    Log.d("GETALL 성공!!!!", "응답 데이터: $responseData")
                    feature1 = responseData!!.result.frontFeatures[0].value
                    feature2 = responseData!!.result.frontFeatures[1].value
                    feature3 = responseData!!.result.backFeatures[0].value
                    feature4 = responseData!!.result.backFeatures[1].value
                    feature5 = responseData!!.result.backFeatures[2].value
                    feature6 = responseData!!.result.backFeatures[3].value
                    feature7 = responseData!!.result.backFeatures[4].value
                    Log.d("frontfeature~~~",feature1.toString())

                    val bundle = Bundle().apply {
                        putString("feature1", feature1)
                        putString("feature2", feature2)
                        val profileId = intent.getStringExtra("profileId")
                        val profileId2 = intent.getStringExtra("reProfileId")

                        if (profileId != null) {
                            putString("profilId1", profileId)
                        } else{
                            putString("profilId1",profileId2)
                        }
                        val dialogName = intent.getStringExtra("dialogName")
                        putString("dialogName",dialogName)
                    }
                    viewModel.setFeatures(feature3.orEmpty(), feature4.orEmpty(),feature5.orEmpty(),feature6.orEmpty(),feature7.orEmpty())

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.tab_layout_container, EditProfileFrontFragment().apply {
                            arguments = bundle
                        })
                        .commit()

                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e("GETALL 요청 실패", "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("GETALL 요청 실패@@", "에러: ${e.message}")
            }
        }

        setTabLayout()
    }





    private fun setTabLayout() {

        binding.storeFragmentTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            // tab이 선택되었을 때
            override fun onTabSelected(tab: TabLayout.Tab?) {

                tab?.position?.let { tabPosition ->
                    // 탭이 선택되었을 때 EditProfileFrontFragment에 정보 전달
                    val fragment1 = supportFragmentManager.findFragmentById(R.id.tab_layout_container)
                    val fragment2 = supportFragmentManager.findFragmentById(R.id.tab_layout_container)
                    if (fragment1 is EditProfileFrontFragment) {
                        fragment1.onTabSelected(tabPosition)
                    }
                    if (fragment2 is EditProfileBackFragment) {
                        fragment2.onTabSelected(tabPosition)
                    }
                }


                when (tab?.position) {
                    0 -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.tab_layout_container, EditProfileFrontFragment().apply {
                                arguments = Bundle().apply {
                                    val profileId1 = intent.getStringExtra("profileId")
                                    val profileId2 = intent.getStringExtra("reProfileId")
                                    Log.d("reProfileId",profileId2.toString())
                                    if (profileId1 != null) {
                                        putString("profilId1", profileId1)
                                    } else{
                                        putString("profilId1", profileId2)

                                    }

                                }

                            })
                            .commit()
                    }
                    1 -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.tab_layout_container, EditProfileBackFragment().apply {
                                arguments = Bundle().apply {
                                    val profileId1 = intent.getStringExtra("profileId")
                                    val profileId2 = intent.getStringExtra("reProfileId")
                                    Log.d("reProfileId",profileId2.toString())
                                    if (profileId1 != null) {
                                        putString("profilId1", profileId1)
                                    } else{
                                        putString("profilId1", profileId2)

                                    }

                                }
                            })
                            .commit()
                    }
                }
            }
            // tab이 선택되지 않았을 때
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            // tab이 다시 선택되었을 때
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

}