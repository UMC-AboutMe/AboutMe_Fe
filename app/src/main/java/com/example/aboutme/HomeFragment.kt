package com.example.aboutme

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.LinearSmoothScroller.SNAP_TO_START
import com.example.aboutme.Alarm.AlarmActivity
import com.example.aboutme.Alarm.AlarmDay7Adapter
import com.example.aboutme.Alarm.Alarm_day7
import com.example.aboutme.Alarm.api.AlarmResponse
import com.example.aboutme.Mypage.MypageActivity
import com.example.aboutme.Mypage.api.AlarmObj
import com.example.aboutme.Myspace.MemberFragment
import com.example.aboutme.Search.SearchProfActivity
import com.example.aboutme.Search.SearchSpaceActivity
import com.example.aboutme.Tutorial.NotificationHelper
import com.example.aboutme.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Timer
import java.util.TimerTask

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding
    lateinit var HomeAdapter: HomeFragmentAdapter
    private val datas = mutableListOf<HomeFragmentData>()
    private lateinit var notificationHelper: NotificationHelper
    lateinit var token: String // token 변수를 추가

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // notificationHelper 초기화
        notificationHelper = NotificationHelper(requireContext())
        // token을 SharedPreferences에서 가져와서 초기화
        val pref = requireActivity().getSharedPreferences("pref", 0)
        token = pref.getString("Gtoken", null) ?: ""
        Log.d("token", token)

        getAlarms(token)


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
    override fun onResume() {
        super.onResume()
        getAlarms(token)
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
    private fun startAutoScroll() {
        var currentPosition = 0
        val timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    val nextPosition = (currentPosition + 1) % datas.size
                    activity?.let { activity ->
                        val smoothScroller = object : LinearSmoothScroller(activity) {
                            override fun getVerticalSnapPreference(): Int {
                                return SNAP_TO_START
                            }
                        }
                        smoothScroller.targetPosition = nextPosition
                        binding.homeitemRc.layoutManager?.startSmoothScroll(smoothScroller)
                    }
                    currentPosition = nextPosition
                }
            }
        }, DELAY_MS, PERIOD_MS)
    }
    companion object {
        private const val DELAY_MS = 0L // 시작 딜레이
        private const val PERIOD_MS = 2000L // 스크롤 간격
    }

    //상단바 알림
//    private fun showNotification(title: String, message: String) {
//        val nb: NotificationCompat.Builder =
//            notificationHelper.getChannelNotification(title, message)
//
//        notificationHelper.getManager().notify(1, nb.build())
//    }
    // 상단바 알림
    private fun showNotification(title: String, message: String) {
        val intent = Intent(requireContext(), AlarmActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE) // FLAG_IMMUTABLE 사용

        val nb: NotificationCompat.Builder =
            notificationHelper.getChannelNotification(title, message)
                .setContentIntent(pendingIntent) // 클릭 시 PendingIntent 실행

        notificationHelper.getManager().notify(1, nb.build())
    }



    //알람 데이터 조회 api
    private fun getAlarms(token: String) {
        val call = AlarmObj.getRetrofitService.getAlarms(token)

        call.enqueue(object : Callback<AlarmResponse.ResponseAlarm> {
            override fun onResponse(
                call: Call<AlarmResponse.ResponseAlarm>,
                response: Response<AlarmResponse.ResponseAlarm>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()
                    if (response != null) {
                        if (response.isSuccess) {
                            // 성공했을 때
                            Log.d("Retrofit_Alarm_Success", response.toString())
                            if (response.result.alarms.isNotEmpty()) {
                                // 서버 응답에서 alarms가 비어있지 않는 경우 푸시 알림을 표시
                                showNotification("AboutMe", "알람페이지에서 공유 알람을 확인하세요!")
                            binding.imageView4.visibility = View.VISIBLE
                            }
                            else {
                                binding.imageView4.visibility = View.GONE
                            }
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e(
                        "Retrofit_Alarm_Failed",
                        "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody"
                    )
                }
            }
            override fun onFailure(
                call: Call<AlarmResponse.ResponseAlarm>,
                t: Throwable
            ) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Alarm_Error", errorMessage)
            }
        })
    }
}