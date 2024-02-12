package com.example.aboutme.Myspace

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.aboutme.HomeFragment
import com.example.aboutme.RetrofitMyspaceAgit.MySpaceCreate
import com.example.aboutme.RetrofitMyspaceAgit.MySpaceCreateRequest
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyspaceAgit.MyspaceCheckResponse
import com.example.aboutme.RetrofitMyspaceAgit.MyspaceCheckResult
import com.example.aboutme.RetrofitMyspaceAgit.ResultModelmsc
import com.example.aboutme.RetrofitMyspaceAgit.RetrofitClient2
import com.example.aboutme.RetrofitMyspaceAgit.RetrofitClientMyspace
import com.example.aboutme.databinding.FragmentMyspacemainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MySpaceMainFragment : Fragment() {

    private lateinit var binding: FragmentMyspacemainBinding
    private val sharedViewModel: MyspaceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyspacemainBinding.inflate(inflater, container, false)

        binding.logo.setOnClickListener {
            // 데이터는 ViewModel에 저장되어 있으므로 Bundle 사용할 필요 없음
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, HomeFragment())
                .commit()
            return@setOnClickListener
        }

        return binding.root
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

                // Edittext와 Ok 버튼을 포함한 레이아웃에 대한 리스트
                val layoutsWithEditTextAndOkButton = listOf(
                    Pair(binding.step3CommentEt, binding.step3CommentOk),
                    Pair(binding.step3MusicEt, binding.step3MusicOk),
                    Pair(binding.step3StoryEt, binding.step3StoryOk),
                    Pair(binding.step3FeelingEt, binding.step3FeelingOk)
                )

                val editTextToVariableMap = mapOf(
                    R.id.step3_comment_et to "commentText",
                    R.id.step3_music_et to "musicText",
                    R.id.step3_story_et to "storyText",
                    R.id.step3_feeling_et to "feelingText"
                )

                val okButtons = listOf(
                    binding.step3CommentOk,
                    binding.step3MusicOk,
                    binding.step3StoryOk,
                    binding.step3FeelingOk
                )

                // Ok 버튼에 대한 클릭 이벤트 처리
                okButtons.forEachIndexed { index, okButton ->
                    okButton.setOnClickListener {
                        val editText = layoutsWithEditTextAndOkButton[index].first
                        val inputText = editText.text.toString()

                        // ViewModel에 저장
                        val variableName = editTextToVariableMap[editText.id]
                        variableName?.let {
                            sharedViewModel.saveText(it, inputText)
                        }

                        // Ok 버튼 숨기기
                        okButton.visibility = View.GONE

                        // 키보드 숨기기
                        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
                    }
                }

                // EditText를 클릭할 때 Ok 버튼 보이도록 설정
                layoutsWithEditTextAndOkButton.forEach { (editText, okButton) ->
                    editText.setOnClickListener {
                        okButton.visibility = View.VISIBLE
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
