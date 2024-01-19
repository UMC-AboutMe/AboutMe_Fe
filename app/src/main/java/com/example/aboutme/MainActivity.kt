package com.example.aboutme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.aboutme.databinding.ActivityMainBinding
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.googleIv.setOnClickListener {
            Toast.makeText(this, "googleclick", Toast.LENGTH_SHORT).show()
        }

        binding.kakaoIv.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    Log.i(TAG, "loginWithKakaoTalk $token $error")
                    updateLoginInfo()
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                    Log.i(TAG, "loginWithKakaoAccount $token $error")
                    updateLoginInfo()
                    Toast.makeText(this, "kakaoclick", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, TutorialActivity1::class.java)
                    startActivity(intent)
                }
            }
        }

        updateLoginInfo();
    }

    private fun updateLoginInfo() {
        UserApiClient.instance.me { user, error ->
            user?.let {
                Log.i(TAG, "updateLoginInfo: ${user.id} ${user.kakaoAccount?.email} ${user.kakaoAccount?.profile?.nickname} ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
            }
            error?.let {

            }
        }
    }
}