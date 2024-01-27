package com.example.aboutme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow

class MainActivity2 : AppCompatActivity(), BottomSheet2.OnBottomSheetListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        if (savedInstanceState == null) {
            val myProfileFragment = MyProfileFragment()

            supportFragmentManager.beginTransaction().apply {
                setReorderingAllowed(true)
                add(R.id.frame_container, myProfileFragment)
                commit()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // 프래그먼트가 완전히 활성화된 후에 초기화 코드 실행
        val myProfileFragment =
            supportFragmentManager.findFragmentById(R.id.frame_container) as? MyProfileFragment
        myProfileFragment?.setActivity(this)
        myProfileFragment?.setOnBottomSheetListener(this)
    }

    override fun onBottomSheetAction() {
        // BottomSheet2에서 발생한 액션에 대한 동작
        // ...
    }


}
