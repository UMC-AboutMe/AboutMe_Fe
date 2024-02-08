package com.example.aboutme.Myprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aboutme.databinding.FragmentEditprofilebackBinding
import com.example.aboutme.databinding.FragmentEditprofilefrontBinding

class EditProfileBackFragment : Fragment() {

    lateinit var binding: FragmentEditprofilebackBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditprofilebackBinding.inflate(inflater, container, false)


        return binding.root
    }
}