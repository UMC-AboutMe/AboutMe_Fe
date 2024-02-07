package com.example.aboutme.Myspace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.aboutme.Myprofile.SharedViewModel
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentMyspacestep3Binding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MySpaceStep3Fragment : Fragment() {

    private lateinit var binding: FragmentMyspacestep3Binding
    private lateinit var sharedViewModel: SharedViewModel
    private val isCreatedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyspacestep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

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

                // Step 3 프래그먼트로 이동
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.frame, MySpaceMainFragment())
                    .commit()

                val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNavigationView.visibility = View.VISIBLE

                isCreatedViewModel.isCreated = true

            }

            binding.back.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }
}
