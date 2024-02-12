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



        setFrag(0)


        binding.editProfileBackBtn.setOnClickListener {

            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra("reProfileId",profileId)
            Log.d("reProfileId1",profileId.toString())
            startActivity(intent)
        }

        binding.finihEditBtn.setOnClickListener {
            val noEditDialog = NoEditDialogFragment()

            Log.d("!!!!","success")
            noEditDialog.show(supportFragmentManager, noEditDialog.tag)
        }

    }
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
            }
        } else {
            Log.e("MyProfileFragment", "positionId가 null입니다.")
        }
    }

}