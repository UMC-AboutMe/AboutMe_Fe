package com.example.aboutme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.aboutme.Agit.AgitFragment
import com.example.aboutme.Myprofile.MainProfileFragment
import com.example.aboutme.Myprofile.SharedViewModel
import com.example.aboutme.MyprofileStorage.ProfileStorageFragment
import com.example.aboutme.Myspace.MySpaceMainFragment
import com.example.aboutme.Myspace.MySpaceStep1Activity
import com.google.android.material.bottomnavigation.BottomNavigationView

class bottomNavigationView : AppCompatActivity() {

    private val isCreatedViewModel: SharedViewModel by viewModels()

    private val frame: ConstraintLayout by lazy { // activity_main의 화면 부분
        findViewById(R.id.layout_main)
    }
    private val bottomNagivationView: BottomNavigationView by lazy { // 하단 네비게이션 바
        findViewById(R.id.bottomNavigationView)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation_view)

        // 애플리케이션 실행 후 첫 화면 설정
        supportFragmentManager.beginTransaction().add(frame.id, HomeFragment()).commit()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val initialItemId = R.id.nav_home
        bottomNavigationView.selectedItemId = initialItemId

        //아이콘 색상 활성화
        bottomNavigationView.itemIconTintList = null

        // 네비게이션 바 클릭 이벤트 설정 - 각자 만든 fragment 이름 넣어주세요~!
        bottomNagivationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_myprof -> {
                    supportFragmentManager.beginTransaction()
                        .replace(frame.id, MainProfileFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }

                R.id.nav_saveprof -> {
                    supportFragmentManager.beginTransaction()
                        .replace(frame.id, ProfileStorageFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }

                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(frame.id, HomeFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }

                R.id.nav_myspace -> {
                    // 뷰모델의 isCreated로 사용자의 스페이스 생성 여부 판단
                    if (isCreatedViewModel.isCreated) {
                        supportFragmentManager.beginTransaction()
                            .replace(frame.id, MySpaceMainFragment())
                            .commit()
                    } else {
                        val intent = Intent(this, MySpaceStep1Activity::class.java)
                        startActivity(intent)
                    }
                    return@setOnItemSelectedListener true
                }

                R.id.nav_agit -> {
                    supportFragmentManager.beginTransaction()
                        .replace(frame.id, AgitFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }

                else -> false
            }
        }
    }

}