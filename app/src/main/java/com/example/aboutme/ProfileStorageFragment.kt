package com.example.aboutme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.aboutme.databinding.FragmentProfilestorageBinding

class ProfileStorageFragment : Fragment() {

    lateinit var binding : FragmentProfilestorageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding=FragmentProfilestorageBinding.inflate(inflater,container,false)

        initRecycler()

        return binding.root
    }

    private fun initRecycler(){
        val itemList = mutableListOf<ProfileData>()

        itemList.add(ProfileData(R.drawable.profilestorage_profile, "프로필1"))
        itemList.add(ProfileData(R.drawable.profilestorage_profile, "프로필2"))
        itemList.add(ProfileData(R.drawable.profilestorage_profile, "프로필3"))
        itemList.add(ProfileData(R.drawable.profilestorage_profile, "프로필4"))
        itemList.add(ProfileData(R.drawable.profilestorage_profile, "프로필5"))
        itemList.add(ProfileData(R.drawable.profilestorage_profile, "프로필6"))
        itemList.add(ProfileData(R.drawable.profilestorage_profile, "프로필7"))
        itemList.add(ProfileData(R.drawable.profilestorage_profile, "프로필8"))
        itemList.add(ProfileData(R.drawable.profilestorage_profile, "프로필9"))
        itemList.add(ProfileData(R.drawable.profilestorage_profile, "프로필10"))
        itemList.add(ProfileData(R.drawable.profilestorage_profile, "프로필11"))
        itemList.add(ProfileData(R.drawable.profilestorage_profile, "프로필12"))


        val rvadapter = ProfileRVAdapter(itemList)

        binding.profileStorageRv.adapter = rvadapter

        binding.profileStorageRv.layoutManager = GridLayoutManager(requireContext(), 2)


    }

}
