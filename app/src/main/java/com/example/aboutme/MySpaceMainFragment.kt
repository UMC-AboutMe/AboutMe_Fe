package com.example.aboutme

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aboutme.databinding.FragmentMyspacemainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class MySpaceMainFragment : Fragment() {

    private lateinit var binding: FragmentMyspacemainBinding
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyspacemainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        binding.back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Retrofit을 사용하여 API 호출
        val call = RetrofitClient2.apitest.createMySpaces(MySpaceCreateRequest(nickname = "승승의 스페이스", characterType = 11, roomType = 1))

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
                // TODO: 결과를 화면에 표시하는 작업 수행

                // Smart cast 오류를 해결하기 위해 명시적으로 null 체크
                result.let {
                    // result가 null이 아닌 경우에만 이 블록이 실행됨
                    // TODO: 로그 추가
                    Log.d("MySpaceMainFragment", "API 호출 성공: $it")
                    Log.d("API TEST", "Result: $result")

                    val nickname = result?.nickname
                    val selectedAvatarIndex = result?.characterType
                    val selectedRoomIndex = result?.roomType

//                    val nickname = sharedViewModel.nickname
//                    val selectedAvatarIndex = sharedViewModel.selectedAvatarIndex
//                    val selectedRoomIndex = sharedViewModel.selectedRoomIndex

                    val imageNameavatar = "step2_avatar_${selectedAvatarIndex?.plus(1)}"
                    val imageNameroom = "step3_room_${selectedRoomIndex?.plus(1)}"

                    // 리소스 아이디 가져오기
                    val resourceIdavatar = requireContext().resources.getIdentifier(imageNameavatar, "drawable", requireContext().packageName)
                    val resourceIdroom = requireContext().resources.getIdentifier(imageNameroom, "drawable", requireContext().packageName)

                    binding.step3SelectedAvatar.setImageResource(resourceIdavatar)
                    binding.step3SelectedRoom.setImageResource(resourceIdroom)
                    binding.myspaceTitlebox.text = buildString {
                        append(nickname)
                        append("'s 스페이스")
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
                // TODO: API 오류를 처리하는 작업 수행

                // TODO: 로그 추가
                Log.e("handleApiError", "API 호출 실패: ${response.code()}")
            }

            override fun onFailure(call: Call<MySpaceCreate>, t: Throwable) {
                // API 호출 실패 처리
                handleApiFailure(t)
            }

            private fun handleApiFailure(t: Throwable) {
                // TODO: API 호출 실패를 처리하는 작업 수행

                // TODO: 로그 추가
                Log.e("handleApiFailure", "API 호출 실패: ${t.message}")
            }
        })
    }
}
