package com.example.aboutme.Myprofile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.aboutme.R
import com.example.aboutme.databinding.ActivityPreviewprofileBinding

class PreviewProfileActivity : AppCompatActivity() {


    private val binding by lazy {
        ActivityPreviewprofileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val profileId = intent.getStringExtra("profileId_to_preview")
        Log.d("preview_id",profileId.toString())



        setFrag(0, profileId.toString())




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

    private fun setFrag(fragNum: Int, profileId: String) {
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

            1 -> {
                Log.d("MyProfileFragment", "BackProfileFragment로 교체 중")
                val backProfilePreviewFragment = BackProfilePreviewFragment()
                backProfilePreviewFragment.arguments = bundle
                ft.replace(R.id.profile_frame2, backProfilePreviewFragment).commit()
            }
        }
    }

}