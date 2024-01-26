package com.example.aboutme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.aboutme.databinding.ActivityMyspacestep2roomBinding

class MySpaceStep2RoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyspacestep2roomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyspacestep2roomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nextButton = binding.nextIbStep3

        // 체크박스에 해당하는 이미지뷰들을 리스트에 추가
        val checkBoxList = listOf(
            binding.checkbox1,
            binding.checkbox2,
            binding.checkbox3,
            binding.checkbox4,
        )

        val checkmarkList = listOf(
            binding.checkmark1,
            binding.checkmark2,
            binding.checkmark3,
            binding.checkmark4,
        )

        val selectedCheckBoxIndexavatar = intent.getIntExtra("index_step2_avatar", -1)

        Log.d("Avatar Index", "$selectedCheckBoxIndexavatar")

        // 선택된 체크박스의 인덱스를 저장할 변수
        var selectedCheckBoxIndexroom: Int? = null

        // 각 체크박스에 대한 클릭 이벤트 처리
        checkBoxList.forEachIndexed { index, checkBox ->
            checkBox.setOnClickListener {
                // 이전에 선택된 체크박스의 체크마크 숨기기
                selectedCheckBoxIndexroom?.let {
                    checkmarkList[it].visibility = View.INVISIBLE
                }

                // 현재 체크마크의 가시성 상태 확인
                val isCheckmarkVisible = checkmarkList[index].visibility == View.VISIBLE

                // 체크마크의 가시성을 토글
                checkmarkList[index].visibility = if (isCheckmarkVisible) View.INVISIBLE else View.VISIBLE

                // 선택된 체크박스의 인덱스 저장
                selectedCheckBoxIndexroom = if (isCheckmarkVisible) null else index

                // 다음 버튼의 가시성 설정
                nextButton.visibility = if (selectedCheckBoxIndexroom != null) View.VISIBLE else View.INVISIBLE
            }
        }

        binding.nextIbStep3.setOnClickListener {
            // 다음 액티비티로 넘길 때 선택된 체크박스의 인덱스 정보를 넘김
            val intent = Intent(this, MySpaceStep3Activity::class.java)
            selectedCheckBoxIndexroom?.let {
                intent.putExtra("index_step2_room", it)
                intent.putExtra("index_step2_avatar", selectedCheckBoxIndexavatar)
            }
            startActivity(intent)
            Log.d("Myspacestep2room", "step3로 넘어갑니다.")
            Log.d("Avatar Index", "$selectedCheckBoxIndexavatar")
            Log.d("Room Index", "$selectedCheckBoxIndexroom")
        }

        binding.back.setOnClickListener {
            finish()
        }
    }
}