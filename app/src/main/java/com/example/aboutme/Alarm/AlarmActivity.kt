package com.example.aboutme.Alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aboutme.Alarm.api.AlarmResponse
import com.example.aboutme.Mypage.api.AlarmObj
import com.example.aboutme.R
import com.example.aboutme.databinding.ActivityAlarmBinding
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmActivity : AppCompatActivity() {
    lateinit var binding: ActivityAlarmBinding
    lateinit var token: String // token 변수를 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // token을 SharedPreferences에서 가져와서 초기화
        val pref = this.getSharedPreferences("pref", 0)
        token = pref.getString("Gtoken", null) ?: ""
        Log.d("token", token)

        getAlarms(token)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }
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

                            //최근 7일 알림
                            val itemList = ArrayList<Alarm_day7>()
                            // alarms 배열을 순회하며 각 알림 항목의 content를 itemList에 추가
                            response.result.alarms.forEach { alarm ->
                                val content = alarm.content
                                val imageResource = when {
                                    "프로필" in content -> R.drawable.nav_myprof2
                                    "스페이스" in content -> R.drawable.nav_myspace2
                                    else -> R.drawable.nav_myprof2
                                }
                                val serialNum = alarm.profile_serial_number
                                val spaceId = alarm.space_id
                                val alarmId = alarm.alarm_id
                                itemList.add(Alarm_day7(imageResource, content, serialNum, spaceId,alarmId))
                                //보관함에 추가하기 api -> 자동 보관되서 일단 막아놓음
                                //getStorageProf(serialNum)
                            }
                            for (item in itemList) {
                                Log.d(
                                    "AlarmDay7Data",
                                    "ImageResource: ${item.img}, Content: ${item.name}, SerialNum: ${item.serialNumber}, SpaceId: ${item.spaceId}"
                                )
                            }
                            val AlarmDay7Adapter = AlarmDay7Adapter(this@AlarmActivity,itemList)
                            AlarmDay7Adapter.notifyDataSetChanged()

                            binding.day7Rc.adapter = AlarmDay7Adapter
                            binding.day7Rc.layoutManager = LinearLayoutManager(
                                this@AlarmActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
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