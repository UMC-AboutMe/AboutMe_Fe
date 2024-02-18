package com.example.aboutme.Myspace

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyspaceAgit.MySpaceCreate
import com.example.aboutme.RetrofitMyspaceAgit.MySpaceCreateRequest
import com.example.aboutme.RetrofitMyspaceAgit.ResultModelmsc
import com.example.aboutme.RetrofitMyspaceAgit.RetrofitClient2
import com.example.aboutme.bottomNavigationView
import com.example.aboutme.databinding.ActivityMyspacestep3Binding
import com.gun0912.tedpermission.provider.TedPermissionProvider.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MySpaceStep3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityMyspacestep3Binding

    private val sharedViewModel: MyspaceViewModel by viewModels()

    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyspacestep3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val nextButton = binding.nextIbStep3

        binding.progressBar.progress = 75

        // SharedPreferences에서 토큰을 가져오는 함수
        fun getToken(context: Context): String? {
            val pref = context.getSharedPreferences("pref", 0)
            return pref.getString("Gtoken", null)
        }

        token = context?.let { getToken(it) }.toString() // SharedPreferences에서 토큰을 가져오는 함수를 호출하여 토큰 값을 가져옵니다.

        // progress bar의 애니메이션 리스너 생성
        val animatorListener = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
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
        val animation = ObjectAnimator.ofInt(binding.progressBar, "progress", 50, 75)
        animation.duration = 1500 // 애니메이션 지속 시간 (밀리초)
        animation.interpolator = AccelerateDecelerateInterpolator() // 가속 및 감속 인터폴레이터 사용
        animation.addListener(animatorListener) // 애니메이션 리스너 추가
        animation.start()

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

            binding.nextIbStep3ClickArea.setOnClickListener {
                // 선택된 체크박스의 인덱스 정보를 ViewModel에 저장
                selectedCheckBoxIndex?.let {
                    sharedViewModel.setSelectedRoomIndex(it)
                }

                // isCreated 값 변경
                sharedViewModel.setSelectedIsCreated(true)

                // 이전 step 데이터들을 서버에 저장하고 로컬 뷰모델에 저장되어있는 정보 추출
                val nickname = sharedViewModel.nickname
                val selectedAvatarIndex = sharedViewModel.selectedAvatarIndex
                val selectedRoomIndex = sharedViewModel.selectedRoomIndex

                // Retrofit을 사용하여 API 호출
                val call = token.let { RetrofitClient2.apitest.createMySpaces(token = it, MySpaceCreateRequest(nickname = "$nickname", characterType = selectedAvatarIndex!!, roomType = selectedRoomIndex!!)) }

                // API 호출로 서버에 저장되어있는 사용자들의 스페이스 정보 추출
                call.enqueue(object : Callback<MySpaceCreate> {
                    override fun onResponse(call: Call<MySpaceCreate>, response: Response<MySpaceCreate>) {
                        if (response.isSuccessful) {
                            val result = response.body()?.result

                            // API 응답 결과를 처리하는 작업 수행
                            result?.let {
                            }
                        } else {
                            // API 오류 처리
                            handleApiError(response)
                        }
                    }

                    private fun handleApiError(response: Response<MySpaceCreate>) {
                        Log.e("handleApiError", "API 호출 실패: ${response.code()}")
                    }

                    override fun onFailure(call: Call<MySpaceCreate>, t: Throwable) {
                        // API 호출 실패 처리
                        handleApiFailure(t)
                    }

                    private fun handleApiFailure(t: Throwable) {
                        Log.e("handleApiFailure", "API 호출 실패: ${t.message}")
                    }
                })

                // step 진행이 모두 완료되었음을 바텀네비게이션뷰에 전달
                val intent = Intent(this, bottomNavigationView::class.java)
                intent.putExtra("stepCompleted", "stepCompleted")
                startActivity(intent)

                // 애니메이션 설정
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
            }

            binding.back.setOnClickListener {
                finish()
            }
        }
    }
}
