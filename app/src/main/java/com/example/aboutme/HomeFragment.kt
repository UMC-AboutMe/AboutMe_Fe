package com.example.aboutme

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.LinearSmoothScroller.SNAP_TO_START
import com.example.aboutme.Alarm.AlarmActivity
import com.example.aboutme.Mypage.MypageActivity
import com.example.aboutme.Myspace.MemberFragment
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
        val pref = requireContext().getSharedPreferences("pref", 0)
        val token: String? = pref.getString("Gtoken", null)
        Log.d("token", token ?: "null")

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
        //startAutoScroll()

    }

    private fun initRecycler() {
        HomeAdapter = HomeFragmentAdapter(requireContext())
        binding.homeitemRc.adapter = HomeAdapter

        HomeAdapter.onItemClick = { position ->
            // 인덱스를 다음 프래그먼트로 전달
            val bundle = Bundle()
            bundle.putInt("position", position)
            val fragment = MemberFragment()
            fragment.arguments = bundle

            // 프래그먼트 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.layout_home, fragment)
                .addToBackStack(null)
                .commit()
        }

        datas.apply {
            add(HomeFragmentData(R.drawable.home_rc1, "테디 | 유태연"))
            add(HomeFragmentData(R.drawable.home_rc2, "파스텔 | 정서연"))
            add(HomeFragmentData(R.drawable.home_rc3, "윤 | 오유은"))
            add(HomeFragmentData(R.drawable.home_rc4, "하나 | 최가나"))
            add(HomeFragmentData(R.drawable.home_rc5, "렉스 | 변성호"))
            add(HomeFragmentData(R.drawable.home_rc6, "혬 | 송혜음"))
            add(HomeFragmentData(R.drawable.home_rc7, "모아 | 조희수"))
            add(HomeFragmentData(R.drawable.home_rc9, "쩡 | 차현정"))
            add(HomeFragmentData(R.drawable.home_rc8, "다에몬 | 정승원"))
            HomeAdapter.datas = datas
            HomeAdapter.notifyDataSetChanged()
        }
    }

    //리싸이클러뷰 자동 스크롤
//    private fun startAutoScroll() {
//        var currentPosition = 0
//        val timer = Timer()
//        timer?.scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                activity?.runOnUiThread {
//                    val nextPosition = (currentPosition + 1) % datas.size
//                    activity?.let { activity ->
//                        val smoothScroller = object : LinearSmoothScroller(activity) {
//                            override fun getVerticalSnapPreference(): Int {
//                                return SNAP_TO_START
//                            }
//                        }
//                        smoothScroller.targetPosition = nextPosition
//                        binding.homeitemRc.layoutManager?.startSmoothScroll(smoothScroller)
//                    }
//                    currentPosition = nextPosition
//                }
//            }
//        }, DELAY_MS, PERIOD_MS)
//    }
    companion object {
        private const val DELAY_MS = 0L // 시작 딜레이
        private const val PERIOD_MS = 2000L // 스크롤 간격
    }
}