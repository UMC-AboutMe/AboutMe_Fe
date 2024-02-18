package com.example.aboutme.Alarm

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.aboutme.Alarm.api.AlarmResponse
import com.example.aboutme.Mypage.api.AlarmObj
import com.example.aboutme.R
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmDay7Adapter (private val context: Context, private val itemList: ArrayList<Alarm_day7>) :
    RecyclerView.Adapter<AlarmDay7Adapter.AlarmViewHolder>() {
    // token을 SharedPreferences에서 가져와서 초기화
    private val pref = context.getSharedPreferences("pref", 0)
    private val token = pref.getString("Gtoken", null) ?: ""
    init {
        Log.d("token", token)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_alarm_item, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.alarmImg.setImageResource(itemList[position].img)
        holder.alarmMessage.text = itemList[position].name

    }

    override fun getItemCount(): Int = itemList.size


    inner class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alarmImg: ImageView = itemView.findViewById(R.id.alarm_img_iv)
        val alarmMessage = itemView.findViewById<TextView>(R.id.alarm_tv)
        val yesButton = itemView.findViewById<ImageButton>(R.id.alarm_yes_btn)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.alarm_save_btn)
        init {
            yesButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = itemList[position]
                    if (item.spaceId == 0L) {
                        // spaceId가 0이면 getStorageProf 함수 호출
                        postStorageProf(item.serialNumber)
                        delteAlarm(item.alarmId)
                    } else {
                        // spaceId가 0이 아니면 postStorageSpace 함수 호출
                        postStorageSpace(token, item.spaceId)
                        delteAlarm(item.alarmId)
                    }
                }
            }/**
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = itemList[position]
                    delteAlarm(item.alarmId)
                }
            }*/
        }
    }

    //마이프로필 보관함에 저장하기
    private fun postStorageProf(profileSerial: Int) {
        Log.d("Retrofit_Add", "보관함 추가 실행")

        val requestStoreProf = AlarmResponse.RequestStorageProf(listOf(profileSerial))
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
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    Log.e(
                        "Retrofit_Storage_Failed",
                        "응답코드: ${response.code()}, 응답메시지: ${response.message()}, 오류 내용: $errorBody"
                    )
                    try {
                        val jsonObject = JSONObject(errorBody)
                        val errorMessage = jsonObject.getString("message")
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<AlarmResponse.ResponseStorageProf>, t: Throwable) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Storage", errorMessage)
            }
        })
    }

    //스페이스 아지트에 저장하기
    private fun postStorageSpace(token: String, spaceId: Long) {
        val call = AlarmObj.getRetrofitService.postStorageSpace(token, spaceId)

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

    //알림 데이터 삭제
    private fun delteAlarm(alarmId: Long) {
        val call = AlarmObj.getRetrofitService.deleteAlarm(alarmId, token)

        call.enqueue(object : Callback<AlarmResponse.ResponseDeleteAlarm> {
            override fun onResponse(
                call: Call<AlarmResponse.ResponseDeleteAlarm>,
                response: Response<AlarmResponse.ResponseDeleteAlarm>
            ) {
                if (response.isSuccessful) {
                    val response = response.body()
                    if (response != null) {
                        if (response.isSuccess) {
                            // 성공했을 때
                            Log.d("Retrofit_Storage_Success", response.toString())
                        }
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
                call: Call<AlarmResponse.ResponseDeleteAlarm>,
                t: Throwable
            ) {
                val errorMessage = "Call Failed:  ${t.message}"
                Log.d("Retrofit_Storage_Error", errorMessage)
            }
        })
    }
}