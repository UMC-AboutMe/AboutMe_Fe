package com.example.aboutme.Alarm.api

import com.example.aboutme.Search.api.SearchResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AlarmItf {
    //알람 조회하기
    @GET("/alarms")
    fun getAlarms(
        @Header("token") token:String
    ) : Call<AlarmResponse.ResponseAlarm>

    //마이프로필 내 보관함에 저장하기
    @POST("/myprofiles/share")
    fun postStorageProf (
        @Header("token") token:String,
        @Body requestStorageProf: AlarmResponse.RequestStorageProf
    ) : Call<AlarmResponse.ResponseStorageProf>

    //스페이스 아지트에 저장하기
    @POST ("/myspaces/storage/{space-id}")
    fun postStorageSpace (
        @Header("token") token:String,
        @Path("space-id") spaceId : Long
    ) : Call<AlarmResponse.ResponseAlarm>
}

