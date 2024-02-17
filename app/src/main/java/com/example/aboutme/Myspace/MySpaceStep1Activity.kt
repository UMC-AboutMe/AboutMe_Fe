package com.example.aboutme.Myspace

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.aboutme.R
import com.example.aboutme.bottomNavigationView
import com.example.aboutme.databinding.ActivityMyspacestep1Binding

class MySpaceStep1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityMyspacestep1Binding

    private val sharedViewModel: MyspaceViewModel by viewModels()

    private val handler = Handler(Looper.getMainLooper())
    private val animationInterval = 400L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyspacestep1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("isCreated", "${sharedViewModel.isCreated}")

        binding.progressBar.progress = 0

        // progress bar의 애니메이션 리스너 생성
        val animatorListener = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                // 애니메이션이 시작될 때 필요한 동작 수행
                // 캐릭터의 위치를 업데이트
                val layoutParams = binding.animationView.layoutParams as ConstraintLayout.LayoutParams
                val marginStartInPixels = (30 * resources.displayMetrics.density).toInt() // 20dp를 픽셀 값으로 변환
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
        val animation = ObjectAnimator.ofInt(binding.progressBar, "progress", 0, 25)
        animation.duration = 1000 // 애니메이션 지속 시간 (밀리초)
        animation.interpolator = AccelerateDecelerateInterpolator() // 가속 및 감속 인터폴레이터 사용
        animation.addListener(animatorListener) // 애니메이션 리스너 추가
        animation.start()

        // 이스터에그!!!
        binding.animationView.setOnClickListener {
            binding.animationView.playAnimation()

            // 이미지뷰를 나타나게 하는 애니메이션을 적용합니다.
            val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            binding.easteregg.startAnimation(fadeInAnimation)
            binding.easteregg.visibility = View.VISIBLE

            // 2초 뒤에 이미지뷰를 숨기는 작업을 수행합니다.
            handler.postDelayed({
                val fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
                binding.easteregg.startAnimation(fadeOutAnimation)
                binding.easteregg.visibility = View.INVISIBLE
            }, 2000)
        }

        // progress bar 위의 캐릭터 실시간 애니메이션 효과 주기
        handler.postDelayed(object : Runnable {
            override fun run() {
                // 애니메이션 재생
                binding.animationView.playAnimation()

                // 다음 애니메이션을 1초 뒤에 실행
                handler.postDelayed(this, animationInterval)
            }
        }, animationInterval) // 1초 뒤에 첫 번째 애니메이션 실행

        // 화면 빈공간 클릭시 키보드 숨기기
        binding.fragmentMyspacestep1.setOnClickListener {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.fragmentMyspacestep1.windowToken, 0)
        }

        // 닉네임 edittext에서 텍스트 입력 후 다음 버튼 클릭시 키보드 숨기기
        binding.nickname.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.nickname.windowToken, 0)

                return@setOnEditorActionListener true
            }
            false
        }

        // 다음 버튼 클릭시
        binding.nextIbStep1ClickArea.setOnClickListener {
            // EditText에서 텍스트 가져오기
            val inputText = binding.nickname.text.toString()

            // ViewModel에 데이터 저장
            sharedViewModel.nickname = inputText

            val intent = Intent(this, MySpaceStep2Activity::class.java)
            startActivity(intent)

            // 애니메이션 설정
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // 홈로고 클릭시
        binding.memberLogo.setOnClickListener {
            val intent = Intent(this, bottomNavigationView::class.java)
            startActivity(intent)

            // 애니메이션 설정
            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
        }
    }

    override fun onBackPressed() {
        // 뒤로 가기 버튼을 눌렀을 때 bottomNavigationView로 이동
        val intent = Intent(this, bottomNavigationView::class.java)
        startActivity(intent)
        super.finish()
    }
}
