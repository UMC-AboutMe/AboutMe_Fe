package com.example.aboutme

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.aboutme.Myspace.MyspaceViewModel
import com.example.aboutme.Search.api.SearchObj
import com.example.aboutme.Search.api.SearchResponse
import com.example.aboutme.Tutorial.TutorialActivity1
import com.example.aboutme.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    private val sharedViewModel: LoginViewModel by viewModels()


    // 앱 처음 시작시 앱 전체 알림 허용 메시지
//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    private val permissionResult =
//        TedPermission.create().setPermissionListener(object: PermissionListener {
//            override fun onPermissionGranted() {
//                Toast.makeText(this@MainActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
//                Toast.makeText(this@MainActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
//            }
//        }).setDeniedMessage("알림을 거부한다면 앱 사용에 지장이 있을 수 있습니다.").setPermissions(android.Manifest.permission.POST_NOTIFICATIONS).check()


    //구글 로그인
    // Google Sign In API와 호출할 구글 로그인 클라이언트
    var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 123
    var signBt: ImageView? = null

    var kakaotoken: OAuthToken? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // 구글 로그인 버튼 클릭시
        binding.googleIv.setOnClickListener {
            Toast.makeText(this, "googleclick", Toast.LENGTH_SHORT).show()
        }

        // 카카오 로그인 버튼 클릭시
        binding.kakaoIv.setOnClickListener {
            // 기기에 카카오톡이 설치되어있는 경우
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (token != null) {
                        Log.i(TAG, "loginWithKakaoTalk ${token.accessToken} $error")

                        // 뷰모델에 액세스토큰을 저장
                        sharedViewModel.accesstoken = token.accessToken
                        Log.d("accesstoken", "${sharedViewModel.accesstoken}")

                        // 발급받은 액세스토큰을 헤더값에 넣어서 서버로 전송한 다음에 jwttoken 발급받기
                        loginKakao("kakao", "${sharedViewModel.accesstoken}")
                    }
                    updateLoginInfo()
                }
            }
            // 기기에 카카오톡이 설치되어있지 않은 경우
            else {
                UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                    if (token != null) {
                        Log.i(TAG, "loginWithKakaoTalk ${token.accessToken} $error")

                        // 뷰모델에 액세스토큰을 저장
                        sharedViewModel.accesstoken = token.accessToken
                        Log.d("accesstoken", "${sharedViewModel.accesstoken}")

                        // 발급받은 액세스토큰을 헤더값에 넣어서 서버로 전송한 다음에 jwttoken 발급받기
                        loginKakao("kakao", "${sharedViewModel.accesstoken}")
                    }
                    updateLoginInfo()
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
    }

    // 카카오 로그인 유저 이메일, 이름, 사진 정보 불러오기
    private fun updateLoginInfo() {
        UserApiClient.instance.me { user, error ->
            user?.let {
                Log.i(
                    TAG,
                    "updateLoginInfo: ${user.id} ${user.kakaoAccount?.email} ${user.kakaoAccount?.profile?.nickname} ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )
                val intent = Intent(this, KakaoLoginProfileActivity::class.java)
                intent.putExtra("email", "${user.kakaoAccount?.email}")
                intent.putExtra("name", "${user.kakaoAccount?.profile?.nickname}")
                intent.putExtra("profile", "${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                startActivity(intent)
            }
            error?.let {

            }
        }
    }

    //구글 로그인
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val acct = completedTask.getResult(ApiException::class.java)
            //로그인 성공시
            if (acct != null) {
                val personEmail = acct.email
                Log.d(
                    TAG,
                    "handleSignInResult:personEmail $personEmail"
                )
                Login("google","$personEmail")
                val intent = Intent(this, TutorialActivity1::class.java)
                startActivity(intent)
/**
                //최초 실행 여부 (최초 실행일시,튜토리얼 화면으로 전환 아닐시 메인화면으로 전환된다.)
                // SharedPreferences 객체 생성
                val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                // "isInit" 값 가져오기 (기본값은 false)
                val isInit = sharedPreferences.getBoolean("isInit", false)
                if (!isInit) {
                    Log.d("test-log", "최초 실행")
                    // "isInit" 값을 true로 설정하여 최초 실행으로 표시
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isInit", true)
                    editor.apply()
                    val intent = Intent(this, TutorialActivity1::class.java)
                    startActivity(intent)
                }
                else {
                    Log.d("test-log", "최초 실행 아님")
                    val intent = Intent(this, bottomNavigationView::class.java)
                    startActivity(intent)
                }
                */
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

    //로그인 - 이메일 방식(구글)
    private fun Login(type: String, email: String) {
        Log.d("Retrofit_Login", "로그인 실행")
        val request = LoginResponse.RequestLogin(email)
        val call = LoginObj.getRetrofitService.postLogin(type, request)

        call.enqueue(object : Callback<LoginResponse.ResponseLogin> {
            override fun onResponse(
                call: Call<LoginResponse.ResponseLogin>,
                response: Response<LoginResponse.ResponseLogin>
            ) {
                Log.d("Retrofit_Login", response.toString())
                if (response.isSuccessful) {
                    val response = response.body()
                    Log.d("Retrofit_Login", response.toString())
                    if (response != null) {
                        if (response.isSuccess) {
                            // 성공했을 때 - 토큰 sharedpreference로 저장
                            val token = response.result.jwtToken
                            val pref = getSharedPreferences("pref", 0)
                            val edit = pref.edit()

                            // 1번째 인자는 키, 2번째 인자는 실제 담아둘 값
                            edit.putString("Gtoken", token)
                            edit.apply() // 저장완료

                            // 토큰 값 로그에 출력
                            Log.d("token", token)
                        } else {
                            // 실패했을 때
                            Log.d("Retrofit_Login", response.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<LoginResponse.ResponseLogin>, t: Throwable) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Login", errorMessage)
            }
        })
    }

    // 로그인 - 액세스 토큰 방식(카카오)
    private fun loginKakao(type: String, token: String) {
        Log.d("Retrofit_Login", "로그인 실행")
        val request = LoginResponse.RequestLoginAT(token)
        val call = LoginObj.getRetrofitService.postLoginKakao(type, request)

        call.enqueue(object : Callback<LoginResponse.ResponseLogin> {
            override fun onResponse(
                call: Call<LoginResponse.ResponseLogin>,
                response: Response<LoginResponse.ResponseLogin>
            ) {
                Log.d("Retrofit_Login", response.toString())
                if (response.isSuccessful) {
                    val response = response.body()
                    Log.d("Retrofit_Login", response.toString())
                    if (response != null) {
                        if (response.isSuccess) {
                            // 성공했을 때 - jwttoken을 sharedpreference로 저장
                            val token = response.result.jwtToken
                            val pref = getSharedPreferences("pref", 0)
                            val edit = pref.edit()

                            // 1번째 인자는 키, 2번째 인자는 실제 담아둘 값
                            edit.putString("Gtoken", token)
                            edit.apply() // 저장완료

                            // 토큰 값 로그에 출력
                            Log.d("token", token)
                        } else {
                            // 실패했을 때
                            Log.d("Retrofit_Login", response.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<LoginResponse.ResponseLogin>, t: Throwable) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Login", errorMessage)
            }
        })
    }

}