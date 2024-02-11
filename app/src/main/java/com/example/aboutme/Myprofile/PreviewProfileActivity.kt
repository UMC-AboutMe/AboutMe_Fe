package com.example.aboutme.Myprofile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.aboutme.R
import com.example.aboutme.databinding.ActivityPreviewprofileBinding

class PreviewProfileActivity : AppCompatActivity() {

    /*companion object {
        // newInstance 메서드 추가
        fun newInstance(positionId: Int): PreviewProfileActivity {
            val activity = PreviewProfileActivity()
            val args = Bundle().apply {
                putInt("positionId", positionId)
            }
            activity.arguments = args
            return fragment
        }
    }*/


    private val binding by lazy {
        ActivityPreviewprofileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val profileId = intent.getStringExtra("profileId_to_preview")
        Log.d("preview_id",profileId.toString())



        setFrag(0)




        binding.editProfileBackBtn.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            //intent.putExtra("positionId",0)
            startActivity(intent)
        }

        binding.finihEditBtn.setOnClickListener {
            val noEditDialog = NoEditDialogFragment()

            Log.d("!!!!","success")
            noEditDialog.show(supportFragmentManager, noEditDialog.tag)
        }

    }

    /*private fun setFrag(fragNum: Int, profileId: String) {
        val ft = supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putString("profileId", profileId)

        when (fragNum) {
            0 -> {
                Log.d("MyProfileFragment", "FrontProfileFragment로 교체 중")
                val frontProfilePreviewFragment = FrontProfilePreviewFragment()
                frontProfilePreviewFragment.arguments = bundle
                ft.replace(R.id.profile_frame2, frontProfilePreviewFragment).commit()
            }

            *//*1 -> {
                Log.d("MyProfileFragment", "BackProfileFragment로 교체 중")
                val backProfilePreviewFragment = BackProfilePreviewFragment()
                backProfilePreviewFragment.arguments = bundle
                ft.replace(R.id.profile_frame2, backProfilePreviewFragment).commit()
            }*//*
        }
    }*/

    private fun setFrag(fragNum: Int) {
        val positionId = intent.getStringExtra("profileId_to_preview")
        Log.d("preview_id",positionId.toString())
        if (positionId != null) { // positionId가 null이 아닌 경우에만 처리
            val ft = supportFragmentManager.beginTransaction()
            when (fragNum) {
                0 -> {
                    Log.d("MyProfileFragment", "FrontProfileFragment로 교체 중")
                    val frontProfilePreviewFragment = FrontProfilePreviewFragment.newInstance(positionId.toInt())
                    ft.replace(R.id.profile_frame2, frontProfilePreviewFragment).commit()
                }
                /*1 -> {
                    Log.d("MyProfileFragment", "BackProfileFragment로 교체 중")
                    val backProfileFragment = BackProfileFragment.newInstance(positionId)
                    ft.replace(R.id.profile_frame, backProfileFragment).commit()
                }*/
            }
        } else {
            Log.e("MyProfileFragment", "positionId가 null입니다.")
        }
    }

}