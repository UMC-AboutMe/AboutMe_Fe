package com.example.aboutme.Myprofile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentBackprofileBinding

class BackProfileFragment : Fragment(), RecommendBottomSheet.OnMbtiSelectedListener,
    RecommendBottomSheet.OnSchoolSelectedListener,
    RecommendBottomSheet.OnCompanySelectedListener, RecommendBottomSheet.OnHobbySelectedListener,
    RecommendBottomSheet.OnJobSelectedListener {

    lateinit var binding : FragmentBackprofileBinding

    private var selectedButtonId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = FragmentBackprofileBinding.inflate(inflater,container, false)

        binding.turnBtn2.setOnClickListener {
            val ft = parentFragmentManager.beginTransaction()

            ft.replace(R.id.profile_frame, FrontProfileFragment()).commit()
        }


        //연필모양 버튼 활성/비활성 구현
        val profileEdit1 : EditText = binding.backProfileEt1
        val profileBtn1 : ImageButton = binding.profileEdit1Btn

        var message1 : String = ""

        profileEdit1.addTextChangedListener (object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                message1 = profileEdit1.text.toString()

                if (message1.isNotEmpty()){
                    profileBtn1.isEnabled = false
                    profileBtn1.alpha = 0.1f
                }else{
                    profileBtn1.isEnabled = true
                    profileBtn1.alpha = 1.0f
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        val profileEdit2 : EditText = binding.backProfileEt2
        val profileBtn2 : ImageButton = binding.profileEdit2Btn

        var message2 : String = ""

        profileEdit2.addTextChangedListener (object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 이 경우에는 구현이 필요하지 않습니다.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                message2 = profileEdit2.text.toString()

                if (message2.isNotEmpty()){
                    profileBtn2.isEnabled = false
                    profileBtn2.alpha = 0.1f
                }else{
                    profileBtn2.isEnabled = true
                    profileBtn2.alpha = 1.0f
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        val profileEdit3 : EditText = binding.backProfileEt3
        val profileBtn3 : ImageButton = binding.profileEdit3Btn

        var message3 : String = ""

        profileEdit3.addTextChangedListener (object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 이 경우에는 구현이 필요하지 않습니다.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                message3 = profileEdit3.text.toString()

                if (message3.isNotEmpty()){
                    profileBtn3.isEnabled = false
                    profileBtn3.alpha = 0.1f
                }else{
                    profileBtn3.isEnabled = true
                    profileBtn3.alpha = 1.0f
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        val profileEdit4 : EditText = binding.backProfileEt4
        val profileBtn4 : ImageButton = binding.profileEdit4Btn

        var message4 : String = ""

        profileEdit4.addTextChangedListener (object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 이 경우에는 구현이 필요하지 않습니다.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                message4 = profileEdit4.text.toString()

                if (message4.isNotEmpty()){
                    profileBtn4.isEnabled = false
                    profileBtn4.alpha = 0.1f
                }else{
                    profileBtn4.isEnabled = true
                    profileBtn4.alpha = 1.0f
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        val profileEdit5 : EditText = binding.backProfileEt5
        val profileBtn5 : ImageButton = binding.profileEdit5Btn

        var message5 : String = ""

        profileEdit5.addTextChangedListener (object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 이 경우에는 구현이 필요하지 않습니다.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                message5 = profileEdit5.text.toString()

                if (message5.isNotEmpty()){
                    profileBtn5.isEnabled = false
                    profileBtn5.alpha = 0.1f
                }else{
                    profileBtn5.isEnabled = true
                    profileBtn5.alpha = 1.0f
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.profileEdit1Btn.setOnClickListener {

            val bottomSheet = RecommendBottomSheet()
            selectedButtonId = 1

            bottomSheet.setOnMbtiSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnSchoolSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnJobSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnHobbySelectedListener(this@BackProfileFragment)
            bottomSheet.setOnCompanySelectedListener(this@BackProfileFragment)

            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        binding.profileEdit2Btn.setOnClickListener {

            val bottomSheet = RecommendBottomSheet()
            selectedButtonId = 2
            bottomSheet.setOnMbtiSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnSchoolSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnJobSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnHobbySelectedListener(this@BackProfileFragment)
            bottomSheet.setOnCompanySelectedListener(this@BackProfileFragment)

            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        binding.profileEdit3Btn.setOnClickListener {

            val bottomSheet = RecommendBottomSheet()
            selectedButtonId = 3

            bottomSheet.setOnMbtiSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnSchoolSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnJobSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnHobbySelectedListener(this@BackProfileFragment)
            bottomSheet.setOnCompanySelectedListener(this@BackProfileFragment)
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        binding.profileEdit4Btn.setOnClickListener {

            val bottomSheet = RecommendBottomSheet()
            selectedButtonId = 4

            bottomSheet.setOnMbtiSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnSchoolSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnJobSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnHobbySelectedListener(this@BackProfileFragment)
            bottomSheet.setOnCompanySelectedListener(this@BackProfileFragment)
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        binding.profileEdit5Btn.setOnClickListener {

            val bottomSheet = RecommendBottomSheet()
            selectedButtonId = 5

            bottomSheet.setOnMbtiSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnSchoolSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnJobSelectedListener(this@BackProfileFragment)
            bottomSheet.setOnHobbySelectedListener(this@BackProfileFragment)
            bottomSheet.setOnCompanySelectedListener(this@BackProfileFragment)
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        if(binding.backProfileEt1.text.toString().trim().isNotEmpty()){
            binding.backProfileEt1.isEnabled = false
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

    override fun onCompanySelected() {
        when(selectedButtonId){
            1 -> {
                Glide.with(requireContext()).load(R.drawable.company).into(binding.recommendEt1Iv)
            }
            2 -> {
                Glide.with(requireContext()).load(R.drawable.company).into(binding.recommendEt2Iv)
            }
            3 -> {
                Glide.with(requireContext()).load(R.drawable.company).into(binding.recommendEt3Iv)
            }
            4 -> {
                Glide.with(requireContext()).load(R.drawable.company).into(binding.recommendEt4Iv)
            }
            5 -> {
                Glide.with(requireContext()).load(R.drawable.company).into(binding.recommendEt5Iv)
            }
        }
    }

    override fun onHobbySelected() {
        when(selectedButtonId){
            1 -> {
                Glide.with(requireContext()).load(R.drawable.hobby).into(binding.recommendEt1Iv)
            }
            2 -> {
                Glide.with(requireContext()).load(R.drawable.hobby).into(binding.recommendEt2Iv)
            }
            3 -> {
                Glide.with(requireContext()).load(R.drawable.hobby).into(binding.recommendEt3Iv)
            }
            4 -> {
                Glide.with(requireContext()).load(R.drawable.hobby).into(binding.recommendEt4Iv)
            }
            5 -> {
                Glide.with(requireContext()).load(R.drawable.hobby).into(binding.recommendEt5Iv)
            }
        }
    }

    override fun onJobSelected() {
        when(selectedButtonId){
            1 -> {
                Glide.with(requireContext()).load(R.drawable.job).into(binding.recommendEt1Iv)
            }
            2 -> {
                Glide.with(requireContext()).load(R.drawable.job).into(binding.recommendEt2Iv)
            }
            3 -> {
                Glide.with(requireContext()).load(R.drawable.job).into(binding.recommendEt3Iv)
            }
            4 -> {
                Glide.with(requireContext()).load(R.drawable.job).into(binding.recommendEt4Iv)
            }
            5 -> {
                Glide.with(requireContext()).load(R.drawable.job).into(binding.recommendEt5Iv)
            }
        }
    }
}