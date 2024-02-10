package com.example.aboutme.Myspace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aboutme.Myprofile.SharedViewModel
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentMyspacestep1Binding

class MySpaceStep1Fragment : Fragment() {

    private lateinit var binding: FragmentMyspacestep1Binding
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyspacestep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel 초기화
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        binding.nextIbStep1.setOnClickListener {
            // EditText에서 텍스트 가져오기
            val inputText = binding.nickname.text.toString()

            // ViewModel에 데이터 저장
            sharedViewModel.nickname = inputText

            // 데이터는 ViewModel에 저장되어 있으므로 Bundle 사용할 필요 없음
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.frame, MySpaceStep2Fragment())
                .commit()
        }
    }
}

