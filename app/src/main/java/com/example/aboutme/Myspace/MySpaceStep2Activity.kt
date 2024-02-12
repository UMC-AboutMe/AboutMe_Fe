package com.example.aboutme.Myspace

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.aboutme.databinding.ActivityMyspacestep2Binding

class MySpaceStep2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityMyspacestep2Binding

    private val sharedViewModel: MyspaceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyspacestep2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val nextButton = binding.nextIbStep2

        // 체크박스에 해당하는 이미지뷰들을 리스트에 추가
        val checkBoxList = listOf(
            binding.avatar1,
            binding.avatar2,
            binding.avatar3,
            binding.avatar4,
            binding.avatar5,
            binding.avatar6,
            binding.avatar7,
            binding.avatar8,
            binding.avatar9,
        )

        val checkmarkList = listOf(
            binding.checkmark1,
            binding.checkmark2,
            binding.checkmark3,
            binding.checkmark4,
            binding.checkmark5,
            binding.checkmark6,
            binding.checkmark7,
            binding.checkmark8,
            binding.checkmark9
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

            binding.nextIbStep2.setOnClickListener {
                // 선택된 체크박스의 인덱스 정보를 ViewModel에 저장
                selectedCheckBoxIndex?.let {
                    sharedViewModel.setSelectedAvatarIndex(it)
                }

                // Step 3 액티비티로 이동
                val intent = Intent(this, MySpaceStep3Activity::class.java)
                startActivity(intent)
            }

            binding.back.setOnClickListener {
                onBackPressed()
            }
        }
    }
}
