package com.example.aboutme

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aboutme.databinding.FragmentMyprofileBinding

class MyProfileFragment : Fragment() {

    lateinit var binding: FragmentMyprofileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding=FragmentMyprofileBinding.inflate(inflater,container,false)


        setFrag(0)

        binding.myprofileFrontBtn.setOnClickListener {
            setFrag(0)
        }

        binding.myprofileBackBtn.setOnClickListener {
            setFrag(1)
        }

        binding.myprofileShareBtn.setOnClickListener {

            val bottomSheet2 = BottomSheet2()

            bottomSheet2.show(childFragmentManager, bottomSheet2.tag)
        }


        return binding.root
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