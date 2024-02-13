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

        //viewModel = ViewModelProvider(this).get(MyProfileViewModel::class.java)



        val profileId = intent.getStringExtra("profileId")
        Log.d("intent데이터",profileId.toString())

        viewModel.updatedData.observe(this, { updatedData ->
            updatedData?.let {
                // 데이터 업데이트 시 필요한 작업 수행
                Log.d("gd",updatedData.toString())
                editName = updatedData.result.frontFeatures[0].value
                editNumber = updatedData.result.frontFeatures[1].value

                Log.d("editname~", editName.toString())

                var fragment1 = EditProfileFrontFragment()

                var bundle = Bundle()


                bundle.putString("editname1",editName)

//fragment1의 arguments에 데이터를 담은 bundle을 넘겨줌
                fragment1.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.tab_layout_container, fragment1)
                    .commit()

                Log.d("1에 넘겨주는 이름",editName.toString())

            }
        })


        supportFragmentManager.beginTransaction()
            .replace(R.id.tab_layout_container, EditProfileFrontFragment().apply {
                arguments = Bundle().apply {

                    // profileId를 가져와 Bundle에 추가
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
            })
            .commit()


            //val profileId = intent.getStringExtra("reProfileId")
        setTabLayout()
    }


    /*override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // EditText의 텍스트를 저장합니다.
        outState.putString("editName", editName)
        Log.d("제발", editName.toString())
        outState.putString("editNumber", editNumber)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // 저장된 EditText의 텍스트를 복원합니다.
        editName = savedInstanceState.getString("editName")
        editNumber = savedInstanceState.getString("editNumber")
    }*/


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