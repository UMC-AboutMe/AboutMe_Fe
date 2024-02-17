package com.example.aboutme.Alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aboutme.Alarm.api.AlarmResponse
import com.example.aboutme.Mypage.api.AlarmObj
import com.example.aboutme.Mypage.api.MyPageObj
import com.example.aboutme.Mypage.api.MyPageResponse
import com.example.aboutme.R
import com.example.aboutme.Search.CustomDialog
import com.example.aboutme.Search.api.SearchObj
import com.example.aboutme.Search.api.SearchResponse
import com.example.aboutme.databinding.ActivityAlarmBinding
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmActivity : AppCompatActivity() {
    lateinit var binding : ActivityAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //알림 데이터 조회 임의값 설정
        getAlarms(6)
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
                            // alarms 배열을 순회하며 각 알림 항목의 content를 itemList에 추가
                            response.result.alarms.forEach { alarm ->
                                val content = alarm.content
                                val imageResource = when {
                                    "프로필" in content -> R.drawable.nav_myprof2
                                    "스페이스" in content -> R.drawable.nav_myspace2
                                    else -> R.drawable.nav_myprof2
                                }
                                itemList.add(Alarm_day7(imageResource, content))
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
    //마이프로필 보관함에 저장하기
    private fun getStorageProf( token : String,spaceId:Long? , profileSerial : Int? ) {
        Log.d("Retrofit_Add","보관함 추가 실행")
        //토큰 임의값 : 멤버아이디6의 토큰
        val token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyMDIyMTE1OUBzdW5nc2hpbi5hYy5rciJ9.I3xzSrIY0GGv4b75e-6nwSnD-uedAaEm1B5bwon4QHiSicYMtGH5zJNgRDnn9bMvmsqHqo2NqoYynHdLSsr_1g"

//        val requestStoreProf = if (spaceId != null) {
//            AlarmResponse.RequestStorageProf(listOf(spaceId.toInt()))
//        } else {
//            AlarmResponse.RequestStorageProf(listOfNotNull(profileSerial))
//        }
        val requestStoreProf = AlarmResponse.RequestStorageProf(listOf(12026))
        val call = AlarmObj.getRetrofitService.postStorageProf(token, requestStoreProf)
        call.enqueue(object : Callback<AlarmResponse.ResponseStorageProf> {
            override fun onResponse(
                call: Call<AlarmResponse.ResponseStorageProf>,
                response: Response<AlarmResponse.ResponseStorageProf>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()
                    if (response != null) {
                        if (response.isSuccess) {
                            //성공했을 때
                            Log.d("Retrofit_Storage", response.message)
                        } else {
                            //실패했을 때
                            Log.d("Retrofit_Storage", response.message)
                        }
                    }
                }else {
                    //Log.d("Retrofit_Search_Failed", response.toString())
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e(
                        "Retrofit_Storage_Failed",
                        "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody"
                    )
                    try {
                        val jsonObject = JSONObject(errorBody)
                        val errorMessage = jsonObject.getString("message")
                        Toast.makeText(this@AlarmActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call:Call<AlarmResponse.ResponseStorageProf>, t:Throwable) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Storage",errorMessage)
            }
        })
    }
    //스페이스 아지트에 저장하기
    private fun postStorageSpace(memberId : Int,spaceId:Long ) {
        val call = AlarmObj.getRetrofitService.postStorageSpace(memberId,spaceId)

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
                            Log.d("Retrofit_Storage_Success", response.toString())
                        }
                    } else {
                        // 실패했을 때
                        Log.d("Retrofit_Storage_Failed", response.toString())
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e(
                        "Retrofit_Storage_Failed",
                        "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody"
                    )
                }
            }
            override fun onFailure(
                call: Call<AlarmResponse.ResponseAlarm>,
                t: Throwable
            ) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Storage_Error", errorMessage)
            }
        })
    }
}