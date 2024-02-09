package com.example.aboutme.Myprofile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.aboutme.R
import com.example.aboutme.databinding.ActivityEditprofileBinding
import com.example.aboutme.databinding.ActivityPreviewprofileBinding

class PreviewProfileActivity : AppCompatActivity() {


    private val binding by lazy {
        ActivityPreviewprofileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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

    private fun setFrag(fragNum : Int){
        val ft = supportFragmentManager.beginTransaction()
        when(fragNum)
        {
            0 -> {
                Log.d("MyProfileFragment", "FrontProfileFragment로 교체 중")
                ft.replace(R.id.profile_frame2, FrontProfilePreviewFragment()).commit()
            }

            1 -> {
                Log.d("MyProfileFragment", "BackProfileFragment로 교체 중")
                ft.replace(R.id.profile_frame2, BackProfilePreviewFragment()).commit()
            }
        }
    }

}