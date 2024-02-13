package com.example.aboutme.Myspace

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.aboutme.R
import com.example.aboutme.bottomNavigationView
import com.example.aboutme.databinding.ActivityMyspacestep3Binding

class MySpaceStep3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityMyspacestep3Binding

    private val sharedViewModel: MyspaceViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyspacestep3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val nextButton = binding.nextIbStep3

        // 체크박스에 해당하는 이미지뷰들을 리스트에 추가
        val checkBoxList = listOf(
            binding.room1,
            binding.room2,
            binding.room3,
            binding.room4
        )

        val checkmarkList = listOf(
            binding.checkmark1,
            binding.checkmark2,
            binding.checkmark3,
            binding.checkmark4,
        )

        sharedViewModel.nickname?.let {
            // 선택된 체크박스의 인덱스를 저장할 변수
            var selectedCheckBoxIndex: Int? = null

            // 각 체크박스에 대한 클릭 이벤트 처리
            checkBoxList.forEachIndexed { index, checkBox ->
                checkBox.setOnClickListener {
                    // 이전에 선택된 체크박스의 체크마크 숨기기
                    selectedCheckBoxIndex?.let {
                        checkmarkList[it].visibility = View.INVISIBLE
                    }

                    // 현재 체크마크의 가시성 상태 확인
                    val isCheckmarkVisible = checkmarkList[index].visibility == View.VISIBLE

                    // 체크마크의 가시성을 토글
                    checkmarkList[index].visibility = if (isCheckmarkVisible) View.INVISIBLE else View.VISIBLE

                    // 선택된 체크박스의 인덱스 저장
                    selectedCheckBoxIndex = if (isCheckmarkVisible) null else index

                    // 다음 버튼의 가시성 설정
                    nextButton.visibility = if (selectedCheckBoxIndex != null) View.VISIBLE else View.INVISIBLE
                }
            }

            binding.nextIbStep3.setOnClickListener {
                // 선택된 체크박스의 인덱스 정보를 ViewModel에 저장
                selectedCheckBoxIndex?.let {
                    sharedViewModel.setSelectedRoomIndex(it)
                }

                // isCreated 값 변경
                sharedViewModel.setSelectedIsCreated(true)

                Log.d("isCreated", "${sharedViewModel.isCreated}")

                startActivity(Intent(this, bottomNavigationView::class.java))

                // 애니메이션 설정
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
            }

            binding.back.setOnClickListener {
                finish()
            }
        }
    }
}
