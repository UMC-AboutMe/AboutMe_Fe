package com.example.aboutme.MyprofileStorage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.aboutme.R
import com.example.aboutme.databinding.FragmentProfilestorageBinding

class ProfileStorageFragment : Fragment() {

    lateinit var binding: FragmentProfilestorageBinding
    private val itemList = mutableListOf<ProfileData>()
    private lateinit var rvAdapter: ProfileRVAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfilestorageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        //val intent = Intent(requireContext(), ProfileStorageDetailFragment::class.java)

        rvAdapter.setOnItemClickListener(object : ProfileRVAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Log.d("클릭2", "success")
                //startActivity(intent)
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                val fragment = ProfileStorageDetailFragment()
                fragmentTransaction.replace(R.id.detailLayout, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        })
    }

    private fun initRecycler() {
        rvAdapter = ProfileRVAdapter(itemList) // rvAdapter 초기화
        binding.profileStorageRv.adapter = rvAdapter
        binding.profileStorageRv.layoutManager = GridLayoutManager(requireContext(), 2)

        // 프로필 데이터 추가 - api 연결 전 임시 데이터
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



    }

}

