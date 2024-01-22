package com.example.aboutme

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aboutme.databinding.FragmentMyprofileBinding

class MyProfileFragment : Fragment() {

    lateinit var binding: FragmentMyprofileBinding

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding=FragmentMyprofileBinding.inflate(inflater,container,false)


        setFrag(0)


        /*binding.myprofileShareBtn.setOnClickListener {

            val bottomSheet2 = BottomSheet2()

            bottomSheet2.show(childFragmentManager, bottomSheet2.tag)
        }*/


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        //공유 버튼 클릭 시 이벤트 발생
        binding.myprofileShareBtn.setOnClickListener {

            val bottomSheet2 = BottomSheet2()

            bottomSheet2.show(childFragmentManager, bottomSheet2.tag)
        }

    }

    private fun setFrag(fragNum : Int){
        val ft = childFragmentManager.beginTransaction()
        when(fragNum)
        {
            0 -> {
                Log.d("MyProfileFragment", "FrontProfileFragment로 교체 중")
                ft.replace(R.id.profile_frame, FrontProfileFragment()).commit()
            }

            1 -> {
                Log.d("MyProfileFragment", "BackProfileFragment로 교체 중")
                ft.replace(R.id.profile_frame, BackProfileFragment()).commit()
            }
        }
    }

}