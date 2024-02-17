package com.example.aboutme.Alarm.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface AlarmItf {
    @GET("/alarms")
    fun getAlarms(
        @Header("member-id") memberId:Long
    ) : Call<AlarmResponse.ResponseAlarm>
}