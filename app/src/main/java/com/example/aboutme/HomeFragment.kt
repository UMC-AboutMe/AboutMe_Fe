package com.example.aboutme

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.LinearSmoothScroller.SNAP_TO_START
import com.example.aboutme.Alarm.AlarmActivity
import com.example.aboutme.Mypage.MypageActivity
import com.example.aboutme.Search.SearchProfActivity
import com.example.aboutme.Search.SearchSpaceActivity
import com.example.aboutme.databinding.FragmentHomeBinding
import java.util.Timer
import java.util.TimerTask

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding

    lateinit var HomeAdapter: HomeFragmentAdapter
    private val datas = mutableListOf<HomeFragmentData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.mypageBtn.setOnClickListener{
            startActivity(Intent(requireActivity(), MypageActivity::class.java))
        }
        binding.alarmBtn.setOnClickListener{
            startActivity(Intent(requireActivity(), AlarmActivity::class.java))
        }
        binding.myprofIv.setOnClickListener {
            startActivity(Intent(requireActivity(), SearchProfActivity::class.java))
        }
        binding.myspaceIv.setOnClickListener {
            startActivity(Intent(requireActivity(), SearchSpaceActivity::class.java))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        startAutoScroll()

    }

    private fun initRecycler() {
        HomeAdapter = HomeFragmentAdapter(requireContext())
        binding.homeitemRc.adapter = HomeAdapter

        datas.apply {
            add(HomeFragmentData(R.drawable.home_rc1, "Member"))
            add(HomeFragmentData(R.drawable.home_rc2, "Member"))
            add(HomeFragmentData(R.drawable.home_rc3, "Member"))
            add(HomeFragmentData(R.drawable.home_rc4, "Member"))
            add(HomeFragmentData(R.drawable.home_rc5, "Member"))
            add(HomeFragmentData(R.drawable.home_rc6, "Member"))
            add(HomeFragmentData(R.drawable.home_rc7, "Member"))
            add(HomeFragmentData(R.drawable.home_rc8, "Member"))
            add(HomeFragmentData(R.drawable.home_rc9, "Member"))

            HomeAdapter.datas = datas
            HomeAdapter.notifyDataSetChanged()
        }
    }

    //리싸이클러뷰 자동 스크롤
    private fun startAutoScroll() {
        var currentPosition = 0
        val timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    val nextPosition = (currentPosition + 1) % datas.size
                    val smoothScroller = object : LinearSmoothScroller(activity) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_START
                        }
                    }
                    smoothScroller.targetPosition = nextPosition
                    binding.homeitemRc.layoutManager?.startSmoothScroll(smoothScroller)
                    currentPosition = nextPosition
                }
            }
        }, DELAY_MS, PERIOD_MS)
    }

    companion object {
        private const val DELAY_MS = 0L // 시작 딜레이
        private const val PERIOD_MS = 2000L // 스크롤 간격
    }
}