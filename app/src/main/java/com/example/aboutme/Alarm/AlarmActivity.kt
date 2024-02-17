package com.example.aboutme.Alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aboutme.Alarm.api.AlarmResponse
import com.example.aboutme.Mypage.api.AlarmObj
import com.example.aboutme.Mypage.api.MyPageObj
import com.example.aboutme.Mypage.api.MyPageResponse
import com.example.aboutme.R
import com.example.aboutme.databinding.ActivityAlarmBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmActivity : AppCompatActivity() {
    lateinit var binding : ActivityAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //알림 데이터 조회 임의값 설정
        getAlarms(1)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }
//        //최근 7일 알림
//        val itemList = ArrayList<Alarm_day7>()
//        itemList.add(Alarm_day7(R.drawable.nav_myprof2,"테디님이 프로필을 공유하였습니다."))
//        itemList.add(Alarm_day7(R.drawable.nav_myspace2,"테디님이 스페이스를 공유하였습니다."))
//        itemList.add(Alarm_day7(R.drawable.nav_myprof2,"테디님이 프로필을 공유하였습니다."))
//        itemList.add(Alarm_day7(R.drawable.nav_myspace2,"테디님이 스페이스를 공유하였습니다."))
//        itemList.add(Alarm_day7(R.drawable.nav_myprof2,"테디님이 프로필을 공유하였습니다."))
//        itemList.add(Alarm_day7(R.drawable.nav_myspace2,"테디님이 스페이스를 공유하였습니다."))
//
//        val AlarmDay7Adapter = AlarmDay7Adapter(itemList)
//        AlarmDay7Adapter.notifyDataSetChanged()
//        binding.day7Rc.adapter = AlarmDay7Adapter
//        binding.day7Rc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//
//        //예전 알림
//        val itemListOld = ArrayList<Alarm_old>()
//        itemListOld.add(Alarm_old(R.drawable.nav_myprof2,"테디님이 프로필을 공유하였습니다."))
//        itemListOld.add(Alarm_old(R.drawable.nav_myspace2,"테디님이 스페이스를 공유하였습니다."))
//        itemListOld.add(Alarm_old(R.drawable.nav_myprof2,"테디님이 프로필을 공유하였습니다."))
//        itemListOld.add(Alarm_old(R.drawable.nav_myspace2,"테디님이 스페이스를 공유하였습니다."))
//        itemListOld.add(Alarm_old(R.drawable.nav_myprof2,"테디님이 프로필을 공유하였습니다."))
//        itemListOld.add(Alarm_old(R.drawable.nav_myspace2,"테디님이 스페이스를 공유하였습니다."))
//
//        val AlarmOldAdapter = AlarmDay7Adapter(itemList)
//        AlarmOldAdapter.notifyDataSetChanged()
//        binding.oldRc.adapter = AlarmOldAdapter
//        binding.oldRc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    //알람 데이터 조회 api
    private fun getAlarms(memberId : Long ) {
        val call = AlarmObj.getRetrofitService.getAlarms(memberId)

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
                            //최근 7일 알림
                            val itemList = ArrayList<Alarm_day7>()
                            // alarms 배열을 순회하며 각 알림 항목의 content를 itemList에 추가합니다.
                            response.result.alarms.forEach { alarm ->
                                val content = alarm.content
                                // 여기서 R.drawable.nav_myprof2는 임의로 지정한 이미지 리소스입니다.
                                itemList.add(Alarm_day7(R.drawable.nav_myprof2, content))
                            }

                            val AlarmDay7Adapter = AlarmDay7Adapter(itemList)
                            AlarmDay7Adapter.notifyDataSetChanged()

                            binding.day7Rc.adapter = AlarmDay7Adapter
                            binding.day7Rc.layoutManager = LinearLayoutManager(this@AlarmActivity, LinearLayoutManager.VERTICAL, false)
                        }
                    } else {
                        // 실패했을 때
                        Log.d("Retrofit_Alarm_Failed", response.toString())
                    }
                } else {
                    //Log.d("Retrofit_Get_Failed", response.toString())
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