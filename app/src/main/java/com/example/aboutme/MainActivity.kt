package com.example.aboutme

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.aboutme.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permissionResult =
        TedPermission.create().setPermissionListener(object: PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(this@MainActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@MainActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }).setDeniedMessage("알림을 거부한다면 앱 사용에 지장이 있을 수 있습니다.").setPermissions(android.Manifest.permission.POST_NOTIFICATIONS).check()

    //구글 로그인
    // Google Sign In API와 호출할 구글 로그인 클라이언트
    var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 123
    var signBt: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

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

        //구글 로그인
        signBt = findViewById(R.id.google_iv)
        signBt?.setOnClickListener(this)

        // 앱에 필요한 사용자 데이터를 요청하도록 로그인 옵션을 설정한다.
        // DEFAULT_SIGN_IN parameter는 유저의 ID와 기본적인 프로필 정보를 요청하는데 사용된다.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail() // email addresses도 요청함
            .build()

        // 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체를 만듬
        mGoogleSignInClient = GoogleSignIn.getClient(this@MainActivity, gso)

        // 기존에 로그인 했던 계정을 확인한다.
        val gsa = GoogleSignIn.getLastSignedInAccount(this@MainActivity)

        // 로그인 되있는 경우 (토큰으로 로그인 처리)
        if (gsa != null && gsa.id != null) {
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

    //구글 로그인
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val acct = completedTask.getResult(ApiException::class.java)
            if (acct != null) {
                val personName = acct.displayName
                val personGivenName = acct.givenName
                val personFamilyName = acct.familyName
                val personEmail = acct.email
                val personId = acct.id
                val personPhoto = acct.photoUrl
                Log.d(
                    TAG,
                    "handleSignInResult:personName $personName"
                )
                Log.d(
                    TAG,
                    "handleSignInResult:personGivenName $personGivenName"
                )
                Log.d(
                    TAG,
                    "handleSignInResult:personEmail $personEmail"
                )
                Log.d(
                    TAG,
                    "handleSignInResult:personId $personId"
                )
                Log.d(
                    TAG,
                    "handleSignInResult:personFamilyName $personFamilyName"
                )
                Log.d(
                    TAG,
                    "handleSignInResult:personPhoto $personPhoto"
                )
            }
        } catch (e: ApiException) {
            Log.e(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.google_iv -> signIn()
            //R.id.logoutBt -> mGoogleSignInClient!!.signOut()
            //    .addOnCompleteListener(this) { task: Task<Void?>? ->
            //        Log.d(TAG, "onClick:logout success ")
            //        mGoogleSignInClient!!.revokeAccess()
            //            .addOnCompleteListener(
            //                this
            //            ) { task1: Task<Void?>? ->
            //                Log.d(
            //                    TAG,
            //                    "onClick:revokeAccess success "
            //                )
            //            }
            //    }
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

}