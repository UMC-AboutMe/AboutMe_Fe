package com.example.aboutme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.aboutme.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class bottomNavigationView : AppCompatActivity() {

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
                        .replace(frame.id, Fragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }

                R.id.nav_saveprof -> {
                    supportFragmentManager.beginTransaction()
                        .replace(frame.id, Fragment())
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
                    supportFragmentManager.beginTransaction()
                        .replace(frame.id, Fragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }

                R.id.nav_agit -> {
                    supportFragmentManager.beginTransaction()
                        .replace(frame.id, Fragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }

                else -> false
            }
        }
    }
}