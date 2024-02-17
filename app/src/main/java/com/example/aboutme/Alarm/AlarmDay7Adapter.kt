package com.example.aboutme.Alarm

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class AlarmDay7Adapter (private val itemList: ArrayList<Alarm_day7>) :
    RecyclerView.Adapter<AlarmDay7Adapter.AlarmViewHolder>() {

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
    }
}