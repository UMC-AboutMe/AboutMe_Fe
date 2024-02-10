package com.example.aboutme.Myprofile

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.aboutme.R

class MainActivity2 : AppCompatActivity(), BottomSheet2.OnBottomSheetListener{

    private val viewModel by lazy { ViewModelProvider(this).get(FrontProfileViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        if (savedInstanceState == null) {
            // 기존 코드: val myProfileFragment = MyProfileFragment()
            val positionId = intent.getIntExtra("positionId", -1)
            // 수정된 코드: 새로운 인스턴스 생성 및 번들 설정
            val myProfileFragment = MyProfileFragment.newInstance(positionId)

            // 번들에 positionId 추가
            val bundle = Bundle().apply {
                putInt("positionId", positionId)
            }
            myProfileFragment.arguments = bundle

            supportFragmentManager.beginTransaction().apply {
                setReorderingAllowed(true)
                add(R.id.frame_container, myProfileFragment)
                commit()
            }

        }

        /*if (savedInstanceState == null) {
            val myProfileFragment = MyProfileFragment()

            supportFragmentManager.beginTransaction().apply {
                setReorderingAllowed(true)
                add(R.id.frame_container, myProfileFragment)
                commit()
            }

        }

        val positionId = intent.getIntExtra("positionId", -1)

        var myProfileFragment = MyProfileFragment()
        var bundle = Bundle()
        bundle.putInt("positionId",positionId)
        myProfileFragment.arguments = bundle
        Log.d("positionId!!",positionId.toString())*/
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
    fun getViewModel2(): FrontProfileViewModel {
        return viewModel
    }



}
