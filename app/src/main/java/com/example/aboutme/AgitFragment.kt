package com.example.aboutme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.aboutme.databinding.FragmentAgitBinding

class AgitFragment : Fragment() {

    lateinit var binding : FragmentAgitBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentAgitBinding.inflate(inflater,container,false)

        initRecycler()

        return binding.root
    }

    private fun initRecycler(){
        val itemList = mutableListOf<AgitSpaceData>()

        itemList.add(AgitSpaceData(R.drawable.agit_space, "00's 스페이스"))
        itemList.add(AgitSpaceData(R.drawable.agit_space, "00's 스페이스"))
        itemList.add(AgitSpaceData(R.drawable.agit_space, "00's 스페이스"))
        itemList.add(AgitSpaceData(R.drawable.agit_space, "00's 스페이스"))
        itemList.add(AgitSpaceData(R.drawable.agit_space, "00's 스페이스"))
        itemList.add(AgitSpaceData(R.drawable.agit_space, "00's 스페이스"))
        itemList.add(AgitSpaceData(R.drawable.agit_space, "00's 스페이스"))
        itemList.add(AgitSpaceData(R.drawable.agit_space, "00's 스페이스"))
        itemList.add(AgitSpaceData(R.drawable.agit_space, "00's 스페이스"))
        itemList.add(AgitSpaceData(R.drawable.agit_space, "00's 스페이스"))
        itemList.add(AgitSpaceData(R.drawable.agit_space, "00's 스페이스"))
        itemList.add(AgitSpaceData(R.drawable.agit_space, "00's 스페이스"))

        val rvadapter = AgitSpaceRVAdapter(itemList)

        binding.spaceStorageRv.adapter = rvadapter

        binding.spaceStorageRv.layoutManager = GridLayoutManager(requireContext(), 2)
    }
}
