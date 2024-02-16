package com.example.aboutme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.aboutme.Agit.AgitFragment
import com.example.aboutme.Myprofile.MainProfileFragment
import com.example.aboutme.MyprofileStorage.ProfileStorageFragment
import com.example.aboutme.Myspace.LottieAnimationActivity
import com.example.aboutme.Myspace.MySpaceMainFragment
import com.example.aboutme.Myspace.MySpaceStep1Activity
import com.example.aboutme.Myspace.MyspaceViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class bottomNavigationView : AppCompatActivity() {

    private val frame: ConstraintLayout by lazy { // activity_main의 화면 부분
        findViewById(R.id.layout_main)
    }
    private val bottomNagivationView: BottomNavigationView by lazy { // 하단 네비게이션 바
        findViewById(R.id.bottomNavigationView)
    }

    private val sharedViewModel: MyspaceViewModel by viewModels()

    // 프래그먼트 인덱싱을 위한 선언
    private val fragments = listOf(
        MainProfileFragment(),
        ProfileStorageFragment(),
        HomeFragment(),
        MySpaceMainFragment(),
        AgitFragment()
    )

    // animation 방향 계산을 위해 가장 마지막 위치 값 저장
    private var recentPosition = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation_view)

        // 애플리케이션 실행 후 첫 화면 설정
        supportFragmentManager.beginTransaction().replace(frame.id, fragments[2]).commit()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val initialItemId = R.id.nav_home
        bottomNavigationView.selectedItemId = initialItemId

        // 아이콘 색상 활성화
        bottomNavigationView.itemIconTintList = null

        // 네비게이션 바 클릭 이벤트 설정 - 각자 만든 fragment 이름 넣어주세요~!
        bottomNagivationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_myprof -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.bottomnavigationview_anim_left, R.anim.bottomnavigationview_fadeout)
                        .replace(frame.id, fragments[0])
                        .commit()
                    recentPosition = 0
                    return@setOnItemSelectedListener true
                }

                R.id.nav_saveprof -> {
                    // 두번째 보다 작은 왼쪽에서 이동해 올 경우 오른쪽에서 화면이 나타남
                    // 반대의 경우 왼쪽에서 나타남
                    if (recentPosition < 1) {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.bottomnavigationview_anim_right, R.anim.bottomnavigationview_fadeout)
                            .replace(frame.id, fragments[1])
                            .commit()
                    } else {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.bottomnavigationview_anim_left, R.anim.bottomnavigationview_fadeout)
                            .replace(frame.id, fragments[1])
                            .commit()
                    }
                    recentPosition = 1
                    return@setOnItemSelectedListener true
                }

                R.id.nav_home -> {
                    // 세번째 보다 작은 왼쪽에서 이동해 올 경우 오른쪽에서 화면이 나타남
                    // 반대의 경우 왼쪽에서 나타남
                    if (recentPosition < 2) {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.bottomnavigationview_anim_right, R.anim.bottomnavigationview_fadeout)
                            .replace(frame.id, fragments[2])
                            .commit()
                    } else {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.bottomnavigationview_anim_left, R.anim.bottomnavigationview_fadeout)
                            .replace(frame.id, fragments[2])
                            .commit()
                    }
                    recentPosition = 2
                    return@setOnItemSelectedListener true
                }

                R.id.nav_myspace -> {
                    // 네번째 보다 작은 왼쪽에서 이동해 올 경우 오른쪽에서 화면이 나타남
                    // 반대의 경우 왼쪽에서 나타남
                    if (recentPosition < 3) {
                        // 뷰모델의 isCreated로 사용자의 스페이스 생성 여부 판단
                        sharedViewModel.isCreated.observe(this) {isCreated ->
                            if (isCreated) {
                                supportFragmentManager.beginTransaction()
                                    .setCustomAnimations(R.anim.bottomnavigationview_anim_right, R.anim.bottomnavigationview_fadeout)
                                    .replace(frame.id, MySpaceMainFragment())
                                    .commit()
                            } else {
                                Log.d("isCreated", "${sharedViewModel.isCreated}")
                                val intent = Intent(this, MySpaceStep1Activity::class.java)
                                startActivity(intent)
                            }
                        }
                    } else {
                        // 뷰모델의 isCreated로 사용자의 스페이스 생성 여부 판단
                        sharedViewModel.isCreated.observe(this) {isCreated ->
                            if (isCreated) {
                                supportFragmentManager.beginTransaction()
                                    .setCustomAnimations(R.anim.bottomnavigationview_anim_left, R.anim.bottomnavigationview_fadeout)
                                    .replace(frame.id, MySpaceMainFragment())
                                    .commit()
                            } else {
                                Log.d("isCreated", "${sharedViewModel.isCreated}")
                                val intent = Intent(this, MySpaceStep1Activity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                    recentPosition = 3
                    return@setOnItemSelectedListener true
                }

                R.id.nav_agit -> {
                    // 다섯번째 보다 작은 왼쪽에서 이동해 올 경우 오른쪽에서 화면이 나타남
                    // 반대의 경우 왼쪽에서 나타남
                    if (recentPosition < 4) {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.bottomnavigationview_anim_right, R.anim.bottomnavigationview_fadeout)
                            .replace(frame.id, fragments[4])
                            .commit()
                    } else {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.bottomnavigationview_anim_left, R.anim.bottomnavigationview_fadeout)
                            .replace(frame.id, fragments[4])
                            .commit()
                    }
                    recentPosition = 4
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // step 진행이 모두 완료되었다는 신호를 전달받기
        val signal = intent.getStringExtra("stepCompleted")

        // step3에서 step 진행이 모두 완료되었다는 신호를 받았을 때
        if (signal == "stepCompleted") {
            val intent = Intent(this, LottieAnimationActivity::class.java)
            intent.putExtra("Lottie", "Lottie")
            startActivity(intent)
        }
        // Lottie 애니메이션 액티비티에서 마이스페이스메인으로 넘어가라는 신호를 받았을 때
        else if (signal == "myspace") {
            CoroutineScope(Dispatchers.Main).launch {
                bottomNagivationView.selectedItemId = R.id.nav_myspace
            }
        }
    }
}