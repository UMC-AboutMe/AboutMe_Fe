package com.example.aboutme.Myprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aboutme.databinding.FragmentEditprofilefrontBinding

class EditProfileFrontFragment : Fragment() {

    lateinit var binding: FragmentEditprofilefrontBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditprofilefrontBinding.inflate(inflater, container, false)


        return binding.root
    }
}