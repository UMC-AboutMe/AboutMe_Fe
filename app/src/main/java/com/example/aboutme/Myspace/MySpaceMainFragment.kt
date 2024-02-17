package com.example.aboutme.Myspace

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.aboutme.RetrofitMyspaceAgit.MySpaceCreate
import com.example.aboutme.RetrofitMyspaceAgit.MySpaceCreateRequest
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyspaceAgit.MyspaceCheckResponse
import com.example.aboutme.RetrofitMyspaceAgit.MyspaceCheckResult
import com.example.aboutme.RetrofitMyspaceAgit.ResultModelmsc
import com.example.aboutme.RetrofitMyspaceAgit.RetrofitClient2
import com.example.aboutme.RetrofitMyspaceAgit.RetrofitClientMyspace
import com.example.aboutme.bottomNavigationView
import com.example.aboutme.databinding.FragmentMyspacemainBinding
import com.github.matteobattilana.weather.PrecipType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MySpaceMainFragment : Fragment() {

    private lateinit var binding: FragmentMyspacemainBinding
    private val sharedViewModel: MyspaceViewModel by viewModels()

    private lateinit var weather: PrecipType
    private var number = 1

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyspacemainBinding.inflate(inflater, container, false)

        binding.logo.setOnClickListener {
            // 홈화면 이동시 애니메이션 효과
            val intent = Intent(activity, bottomNavigationView::class.java)
            val options = ActivityOptions.makeCustomAnimation(requireContext(), R.anim.zoom_in, R.anim.zoom_out)
            requireActivity().startActivity(intent, options.toBundle())
        }

        binding.myspaceTitle.setOnClickListener {
            changeWeather()
        }

        binding.myspaceTitleName.setOnClickListener {
            cancelWeather()
        }

        return binding.root
    }

    private fun changeWeather() {
        binding.weatherView.visibility = View.VISIBLE

        var weatherSpeed = 0
        var weatherParticles = 0f

        // 사이클 돌리기
        if (number < 2) {
            ++number
        } else {
            number = 0
        }

        // number 상수값으로 날씨 결정
        when (number) {
            0 -> {
                weather = PrecipType.CLEAR
            }
            1 -> {
                weather = PrecipType.SNOW
                weatherParticles = 50f
                weatherSpeed = 200
            }
            2 -> {
                weather = PrecipType.RAIN
                weatherParticles = 100f
                weatherSpeed = 600
            }
        }

        //Update animation UI for weather
        binding.weatherView.apply {
            setWeatherData(weather)
            speed = weatherSpeed  // 입자가 내리는 속도
            emissionRate = weatherParticles  // 입자수
            angle = 0 // 입자가 내리는 각도
            fadeOutPercent = .8f  // 입자가 사라지는 임계값 (0.0f-1.0f) -> 수치가 낮을수록 빨리 사라짐
            // 입자의 크기는 조정불가
        }

        // 입자가 하얀색이므로 프래그먼트 및 기존 검정 텍스트의 색깔 변경
        binding.fragmentMyspacemain.apply {
            // 배경색 변경
            binding.fragmentMyspacemain.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))

            // 특정 이미지뷰의 텍스트 색상 변경
            binding.myspaceTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.myspaceTitleName.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    private fun cancelWeather() {
        binding.weatherView.visibility = View.GONE

        binding.fragmentMyspacemain.apply {
            // 배경색 변경
            binding.fragmentMyspacemain.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

            // 특정 이미지뷰의 텍스트 색상 변경
            binding.myspaceTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.myspaceTitleName.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
    }

    private fun fetchData() {
        // retrofitclient에서 통신 방법 설정(GET, POST, DELETE, PATCH)
        val call = RetrofitClientMyspace.apitest.checkMySpace("2")

        call.enqueue(object : Callback<MyspaceCheckResponse> { // API 호출(call, response 데이터 클래스 명시)
            override fun onResponse(call: Call<MyspaceCheckResponse>, response: Response<MyspaceCheckResponse>) {
                if (response.isSuccessful) { // API 호출 성공시
                    val result = response.body()?.result
                    result?.let {
                        updateUI(it)
                    }
                } else { // API 호출 실패시
                    handleApiError(response)
                }
            }

            // API 호출 실패시
            override fun onFailure(call: Call<MyspaceCheckResponse>, t: Throwable) {
                handleApiFailure(t)
            }
        })
    }

    private fun updateUI(result: MyspaceCheckResult) {
        // 서버에 저장되어있는 유저의 스페이스 정보 추출
        // 소셜 로그인 구현 후 추가 작성
        val nickname = result.nickname
        val selectedAvatarIndex = result.characterType
        val selectedRoomIndex = result.roomType

        val imageNameavatar = "step2_avatar_${selectedAvatarIndex}"
        val imageNameroom = "step3_room_${selectedRoomIndex}"

        // 리소스 아이디 가져오기
        val resourceIdavatar = requireContext().resources.getIdentifier(imageNameavatar, "drawable", requireContext().packageName)
        val resourceIdroom = requireContext().resources.getIdentifier(imageNameroom, "drawable", requireContext().packageName)

        binding.step3SelectedAvatar.setImageResource(resourceIdavatar)
        binding.step3SelectedRoom.setImageResource(resourceIdroom)
        binding.myspaceTitle.text = buildString {
            append("$nickname's")
        }

        // mood, musicUrl, statusMessage 설정
        // spaceImageList, planList 설정

        // API TEST
        Log.d("API TEST", "Space ID: ${result.spaceImageList}")
        Log.d("API TEST", "Space ID: ${result.planList}")
    }

    // API ERROR 표시
    private fun handleApiError(response: Response<MyspaceCheckResponse>) {
        Log.e("handleApiError", "API 호출 실패: ${response.code()}")
    }

    // API ERROR 표시
    private fun handleApiFailure(t: Throwable) {
        Log.e("handleApiFailure", "API 호출 실패: ${t.message}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.isCreated.observe(this) {isCreated ->
            if (isCreated) {
                fetchData()

                // 아이콘 클릭시 등장하는 입력란 전체를 감싸고 있는 레이아웃
                val layouts = listOf(
                    binding.step3FeelingLayout,
                    binding.step3CommentLayout,
                    binding.step3StoryLayout,
                    binding.step3MusicLayout,
                    binding.step3ScheduleLayout,
                    binding.step3PhotoLayout
                )

                // 마이스페이스 아이콘 목록
                val buttons = listOf(
                    binding.step3Feeling,
                    binding.step3Comment,
                    binding.step3Story,
                    binding.step3Music,
                    binding.step3Schedule,
                    binding.step3Photo
                )

                // 아이콘 클릭시 등장하는 입력란 표시
                buttons.forEachIndexed { index, button ->
                    button.setOnClickListener {
                        // 키보드 숨기기
                        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(button.windowToken, 0)

                        // 기존에 선택된 레이아웃들을 모두 숨김
                        layouts.forEach { layout -> layout.visibility = View.GONE }

                        // 현재 클릭된 버튼에 해당하는 레이아웃을 보임
                        layouts[index].visibility = View.VISIBLE
                    }
                }

                // *문자를 입력해야하는 아이콘들에 대한 정의*
                // Edittext와 Ok 버튼이 존재하는 레이아웃에 대한 리스트
                val layoutsWithEditTextAndOkButton = listOf(
                    Pair(binding.step3CommentEt, binding.step3CommentOk),
                    Pair(binding.step3MusicEt, binding.step3MusicOk),
                    Pair(binding.step3StoryEt, binding.step3StoryOk),
                )

                // *문자를 입력해야하는 아이콘들에 대한 정의*
                // Edittext 주소 매핑
                val editTextToVariableMap = mapOf(
                    R.id.step3_comment_et to "commentText",
                    R.id.step3_music_et to "musicText",
                    R.id.step3_story_et to "storyText",
                )

                // *문자를 입력해야하는 아이콘들에 대한 정의*
                // Ok 버튼 주소 매핑
                val okButtons = listOf(
                    binding.step3CommentOk,
                    binding.step3MusicOk,
                    binding.step3StoryOk,
                )

                // *문자를 입력해야하는 아이콘들에 대한 정의*
                // Ok 버튼에 대한 클릭 이벤트 처리
                okButtons.forEachIndexed { index, okButton ->
                    okButton.setOnClickListener {
                        val editText = layoutsWithEditTextAndOkButton[index].first
                        val inputText = editText.text.toString()

                        // 확인버튼 누를시 edittext는 읽기 전용으로 바뀌고 다시 수정을 하려면 연필버튼을 누르도록 유도
                        editText.isEnabled = false

                        // ViewModel에 저장
                        val variableName = editTextToVariableMap[editText.id]
                        variableName?.let {
                            sharedViewModel.saveText(it, inputText)
                        }

                        // Ok 버튼 숨기기
                        okButton.visibility = View.GONE

                        // 음악 아이콘의 경우 ok버튼을 눌러서 링크를 저장한 후에 하단에 안내메시지 등장
                        if (index == 1) {
                            // 이미지뷰를 나타나게 하는 애니메이션을 적용합니다.
                            val fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
                            binding.youtubeLinkTv.startAnimation(fadeInAnimation)
                            binding.youtubeLinkTv.visibility = View.VISIBLE

                            // 2초 뒤에 이미지뷰를 숨기는 작업을 수행합니다.
                            handler.postDelayed({
                                val fadeOutAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
                                binding.youtubeLinkTv.startAnimation(fadeOutAnimation)
                                binding.youtubeLinkTv.visibility = View.INVISIBLE
                            }, 2000)

                            binding.step3MusicPencil.visibility = View.VISIBLE
                            binding.step3MusicTrash.visibility = View.VISIBLE
                        }

                        // 키보드 숨기기
                        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
                    }
                }

                binding.step3MusicGuide.setOnClickListener {
                    // EditText에서 텍스트 가져오기
                    val youtubeLink = binding.step3MusicEt.text.toString()

                    // 기기에 설치되어있는 유튜브로 리다이렉트
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setPackage("com.google.android.youtube")
                    startActivity(intent)
                }

                // *문자를 입력해야하는 아이콘들에 대한 정의*
                // EditText를 클릭할 때 Ok 버튼 보이도록 설정
                layoutsWithEditTextAndOkButton.forEach { (editText, okButton) ->
                    editText.setOnClickListener {
                        okButton.visibility = View.VISIBLE
                    }
                }

                // *문자를 입력하지 않는 feeling 아이콘에 대한 정의*
                // 클릭한 이미지뷰를 저장할 변수 선언
                var selectedImageView: ImageView? = null

                // *문자를 입력하지 않는 feeling 아이콘에 대한 정의*
                // feeling 아이콘 주소 매핑
                val feelingIcons = listOf(
                    binding.feeling1,
                    binding.feeling2,
                    binding.feeling3,
                    binding.feeling4,
                    binding.feeling5,
                )

                // *문자를 입력하지 않는 feeling 아이콘에 대한 정의*
                // Ok 버튼에 대한 클릭 이벤트 처리
                feelingIcons.forEach { imageView ->
                    imageView.setOnClickListener {
                        // 클릭한 이미지뷰를 제외한 나머지 이미지뷰들을 숨김
                        feelingIcons.filter { it != imageView }.forEach { it.visibility = View.GONE }

                        // 클릭한 이미지뷰를 feeling_3 아이콘 자리로 이동
                        val layoutParams = imageView.layoutParams as RelativeLayout.LayoutParams
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
                        layoutParams.addRule(RelativeLayout.BELOW, R.id.step3_feeling_guide)
                        imageView.layoutParams = layoutParams

                        // 클릭한 이미지뷰를 selectedImageView에 저장
                        selectedImageView = imageView

                        // 클릭한 이미지뷰를 뷰모델에 저장
                        sharedViewModel.setSelectedFeeling(imageView.id)
                    }
                }


            } else {
                // 이전 step 데이터들을 서버에 저장하고 로컬 뷰모델에 저장되어있는 정보 추출
                val nickname = sharedViewModel.nickname
                val selectedAvatarIndex = sharedViewModel.selectedAvatarIndex
                val selectedRoomIndex = sharedViewModel.selectedRoomIndex

                // Retrofit을 사용하여 API 호출
                val call = RetrofitClient2.apitest.createMySpaces(memberId = "1", MySpaceCreateRequest(nickname = "$nickname", characterType = selectedAvatarIndex!!, roomType = selectedRoomIndex!!))

                // API 호출로 서버에 저장되어있는 사용자들의 스페이스 정보 추출
                call.enqueue(object : Callback<MySpaceCreate> {
                    override fun onResponse(call: Call<MySpaceCreate>, response: Response<MySpaceCreate>) {
                        if (response.isSuccessful) {
                            val result = response.body()?.result

                            // API 응답 결과를 처리하는 작업 수행
                            result?.let {
                                updateUI(it)
                            }
                        } else {
                            // API 오류 처리
                            handleApiError(response)
                        }
                    }

                    private fun updateUI(result: ResultModelmsc?) {

                        // Smart cast 오류를 해결하기 위해 명시적으로 null 체크
                        result.let {
                            // result가 null이 아닌 경우에만 이 블록이 실행됨
                            Log.d("MySpaceMainFragment", "API 호출 성공: $it")
                            Log.d("API TEST", "Result: $result")

                            val imageNameavatar = "step2_avatar_${selectedAvatarIndex.plus(1)}"
                            val imageNameroom = "step3_room_${selectedRoomIndex.plus(1)}"

                            // 리소스 아이디 가져오기
                            val resourceIdavatar = requireContext().resources.getIdentifier(imageNameavatar, "drawable", requireContext().packageName)
                            val resourceIdroom = requireContext().resources.getIdentifier(imageNameroom, "drawable", requireContext().packageName)

                            binding.step3SelectedAvatar.setImageResource(resourceIdavatar)
                            binding.step3SelectedRoom.setImageResource(resourceIdroom)
                            binding.myspaceTitle.text = buildString {
                                append(nickname)
                            }

                            val layouts = listOf(
                                binding.step3FeelingLayout,
                                binding.step3CommentLayout,
                                binding.step3StoryLayout,
                                binding.step3MusicLayout,
                                binding.step3ScheduleLayout,
                                binding.step3PhotoLayout
                            )

                            val buttons = listOf(
                                binding.step3Feeling,
                                binding.step3Comment,
                                binding.step3Story,
                                binding.step3Music,
                                binding.step3Schedule,
                                binding.step3Photo
                            )

                            buttons.forEachIndexed { index, button ->
                                button.setOnClickListener {
                                    // 기존에 선택된 레이아웃들을 모두 숨김
                                    layouts.forEach { layout -> layout.visibility = View.GONE }

                                    // 현재 클릭된 버튼에 해당하는 레이아웃을 보임
                                    layouts[index].visibility = View.VISIBLE
                                }
                            }
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
            }
        }
    }
}
