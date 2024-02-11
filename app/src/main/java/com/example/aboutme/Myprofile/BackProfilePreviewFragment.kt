package com.example.aboutme.Myprofile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.aboutme.R
import com.example.aboutme.RetrofitMyprofile.RetrofitClient
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.RetrofitMyprofileData.PatchMyprofile
import com.example.aboutme.databinding.FragmentBackprofileBinding
import com.example.aboutme.databinding.FragmentFrontprofileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class BackProfilePreviewFragment: Fragment() {

    lateinit var binding: FragmentBackprofileBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBackprofileBinding.inflate(inflater, container, false)

        val profileId = arguments?.getString("profileId")
        Log.d("preview_id_in_fragment!!", profileId.toString())

        binding.turnBtn2.setOnClickListener {
            val ft = requireActivity().supportFragmentManager.beginTransaction()

            ft.replace(R.id.profile_frame2, FrontProfilePreviewFragment()).commit()
        }


        return binding.root
    }


}