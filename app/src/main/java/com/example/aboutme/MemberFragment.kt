package com.example.aboutme.Myspace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.aboutme.Myprofile.SharedViewModel
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentMemberBinding
import com.example.aboutme.databinding.FragmentMyspacemainBinding

class MemberFragment : Fragment() {

    private lateinit var binding2 : FragmentMemberBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding2 = FragmentMemberBinding.inflate(inflater, container, false)

        return binding2.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달받은 위치 정보를 가져오기
        val position = arguments?.getInt("position") ?: -1
        val drawableResId = when (position) {
            0, 3, 6 -> R.drawable.step3_room_1
            1, 4, 7 -> R.drawable.step3_room_2
            2, 5, 8 -> R.drawable.step3_room_4
            else -> R.drawable.step3_room_1 // 기본값 설정
        }
        val drawableAvId = when (position) {
        0-> R.drawable.step2_avatar_2
        1-> R.drawable.step2_avatar_6
        2-> R.drawable.step2_avatar_1
        3-> R.drawable.step2_avatar_7
        4-> R.drawable.step2_avatar_4
        5-> R.drawable.step2_avatar_9
        6-> R.drawable.step2_avatar_8
        7-> R.drawable.step2_avatar_5
        8-> R.drawable.step2_avatar_3
            else-> R.drawable.step2_avatar_2
        }

        val AvTv = when(position) {
            0-> "테디 | 유태연's\n스페이스"
            1-> "파스텔 | 정서연's\n스페이스"
            2-> "윤 | 오유은's\n스페이스"
            3-> "하나 | 최가나's\n스페이스"
            4-> "렉스 | 변성호's\n스페이스"
            5-> "혬 | 송혜음's\n스페이스"
            6-> "모아 | 조희수's\n스페이스"
            7-> "쩡 | 차현정's\n스페이스"
            else-> "다에몬 | 정승원's\n스페이스"
        }
        // 이미지뷰에 이미지 설정
        binding2.memberStep3SelectedRoom.setImageResource(drawableResId)
        // 텍스트뷰에 텍스트 설정
        binding2.memberStep3SelectedAvatar.setImageResource(drawableAvId)
        // 텍스트뷰에 텍스트 설정
        binding2.memberMyspaceTitle.text = AvTv
    }
}
