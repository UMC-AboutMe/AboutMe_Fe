package com.example.aboutme.Myspace

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.aboutme.R
import com.example.aboutme.databinding.ActivityMyspacestep2Binding

class MySpaceStep2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityMyspacestep2Binding

    private val sharedViewModel: MyspaceViewModel by viewModels()

    private val handler = Handler(Looper.getMainLooper())
    private val animationInterval = 400L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyspacestep2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val nextButton = binding.nextIbStep2

        binding.progressBar.progress = 50

        // progress bar의 애니메이션 리스너 생성
        val animatorListener = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                // 애니메이션이 시작될 때 필요한 동작 수행
                // 캐릭터의 위치를 업데이트
                val layoutParams = binding.animationView.layoutParams as ConstraintLayout.LayoutParams
                val marginStartInPixels = (90 * resources.displayMetrics.density).toInt() // 20dp를 픽셀 값으로 변환
                layoutParams.marginStart = marginStartInPixels
                binding.animationView.layoutParams = layoutParams
            }

            override fun onAnimationEnd(animation: Animator) {
                // 애니메이션이 끝날 때 필요한 동작 수행
            }

            override fun onAnimationCancel(animation: Animator) {
                // 애니메이션이 취소될 때 필요한 동작 수행
            }

            override fun onAnimationRepeat(animation: Animator) {
                // 애니메이션이 반복될 때 필요한 동작 수행
            }
        }

        // 애니메이션 효과 추가
        val animation = ObjectAnimator.ofInt(binding.progressBar, "progress", 25, 50)
        animation.duration = 1500 // 애니메이션 지속 시간 (밀리초)
        animation.interpolator = AccelerateDecelerateInterpolator() // 가속 및 감속 인터폴레이터 사용
        animation.addListener(animatorListener) // 애니메이션 리스너 추가
        animation.start()

        handler.postDelayed(object : Runnable {
            override fun run() {
                // 애니메이션 재생
                binding.animationView.playAnimation()

                // 다음 애니메이션을 1초 뒤에 실행
                handler.postDelayed(this, animationInterval)
            }
        }, animationInterval) // 1초 뒤에 첫 번째 애니메이션 실행

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

                // 애니메이션 설정
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            binding.back.setOnClickListener {
                onBackPressed()
            }
        }
    }
}
