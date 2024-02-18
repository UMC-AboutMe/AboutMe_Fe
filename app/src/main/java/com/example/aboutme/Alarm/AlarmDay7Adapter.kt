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

    private val pref = context.getSharedPreferences("pref",0)
    private val token = pref.getString("Gtoken",null) ?: ""
    init {
        Log.d("token",token)
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

    // 새로고침을 위한 함수
    fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alarmImg: ImageView = itemView.findViewById(R.id.alarm_img_iv)
        val alarmMessage = itemView.findViewById<TextView>(R.id.alarm_tv)
        val saveButton = itemView.findViewById<ImageButton>(R.id.alarm_yes_btn) // 저장 버튼 ID에 맞게 변경해주세요
        val deleteButton = itemView.findViewById<ImageButton>(R.id.alarm_save_btn) // 저장 버튼 ID에 맞게 변경해주세요

        //프로필일때/스페이스일때 구분 해야함
        //만약 클릭한 아이템의 serialNumber가 0이라면(null) postStorageSpace을 호출하고,아니라면 getStorageProf을 호출한다.
        init {
            saveButton.setOnClickListener {
                val item = itemList[adapterPosition]
                val serialNumber = item.serialNumber // 아이템의 시리얼 넘버 가져오기
                val spaceId = item.spaceId
                if (serialNumber == 0) {
                    // 시리얼 넘버가 0/ 시리얼 넘버가 없다는 뜻 즉 ,스페이스 저장임
                    postStorageSpace(spaceId!!.toLong()) // postStorageSpace 함수 호출
                } else {
                    // 프로필
                    postStorageProf(serialNumber!!.toInt()) // postStorageProf 함수 호출
                }
                // 저장 후에 새로고침
                removeItem(adapterPosition)
            }
        }
        init {
            deleteButton.setOnClickListener {
                val item = itemList[adapterPosition]
                val alarmId = item.alarmId // 아이템의 시리얼 넘버 가져오기
                delteAlarm(alarmId) // getStorageProf 함수 호출
                // 저장 후에 새로고침
                removeItem(adapterPosition)
            }
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
    private fun postStorageSpace(spaceId: Long) {
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