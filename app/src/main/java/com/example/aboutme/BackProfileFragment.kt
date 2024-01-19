package com.example.aboutme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.aboutme.databinding.FragmentBackprofileBinding
import com.example.aboutme.databinding.FragmentFrontprofileBinding

class BackProfileFragment : Fragment(), RecommendBottomSheet.OnMbtiSelectedListener, RecommendBottomSheet.OnSchoolSelectedListener,
    RecommendBottomSheet.OnCompanySelectedListener, RecommendBottomSheet.OnHobbySelectedListener, RecommendBottomSheet.OnJobSelectedListener {

    lateinit var binding : FragmentBackprofileBinding

    private var selectedButtonId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = FragmentBackprofileBinding.inflate(inflater,container, false)

        binding.turnBtn2.setOnClickListener {
            val ft = parentFragmentManager.beginTransaction()

            ft.replace(R.id.profile_frame, FrontProfileFragment()).commit()
        }

        binding.backProfileEt1.setOnClickListener {

            val bottomSheet = RecommendBottomSheet()
            selectedButtonId = 1

            bottomSheet.setOnMbtiSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnSchoolSelectedListener(this@BackProfileFragment)

            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        binding.backProfileEt2.setOnClickListener {

            val bottomSheet = RecommendBottomSheet()
            selectedButtonId = 2
            bottomSheet.setOnMbtiSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnSchoolSelectedListener(this@BackProfileFragment)

            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        binding.backProfileEt3.setOnClickListener {

            val bottomSheet = RecommendBottomSheet()
            selectedButtonId = 3

            bottomSheet.setOnMbtiSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnSchoolSelectedListener(this@BackProfileFragment)
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        binding.backProfileEt4.setOnClickListener {

            val bottomSheet = RecommendBottomSheet()
            selectedButtonId = 4

            bottomSheet.setOnMbtiSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnSchoolSelectedListener(this@BackProfileFragment)
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        binding.backProfileEt5.setOnClickListener {

            val bottomSheet = RecommendBottomSheet()
            selectedButtonId = 5

            bottomSheet.setOnMbtiSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnSchoolSelectedListener(this@BackProfileFragment)
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }



        return binding.root
    }

    override fun onMbtiSelected() {
        when(selectedButtonId){
            1 -> {
                Glide.with(requireContext()).load(R.drawable.mbti).into(binding.recommendEt1Iv)
            }
            2 -> {
                Glide.with(requireContext()).load(R.drawable.mbti).into(binding.recommendEt2Iv)
            }
            3 -> {
                Glide.with(requireContext()).load(R.drawable.mbti).into(binding.recommendEt3Iv)
            }
            4 -> {
                Glide.with(requireContext()).load(R.drawable.mbti).into(binding.recommendEt4Iv)
            }
            5 -> {
                Glide.with(requireContext()).load(R.drawable.mbti).into(binding.recommendEt5Iv)
            }
        }
    }

    override fun onSchoolSelected() {
        when(selectedButtonId){
            1 -> {
                Glide.with(requireContext()).load(R.drawable.school).into(binding.recommendEt1Iv)
            }
            2 -> {
                Glide.with(requireContext()).load(R.drawable.school).into(binding.recommendEt2Iv)
            }
            3 -> {
                Glide.with(requireContext()).load(R.drawable.school).into(binding.recommendEt3Iv)
            }
            4 -> {
                Glide.with(requireContext()).load(R.drawable.school).into(binding.recommendEt4Iv)
            }
            5 -> {
                Glide.with(requireContext()).load(R.drawable.school).into(binding.recommendEt5Iv)
            }
        }
    }
}