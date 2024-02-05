package com.example.aboutme

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApitestInterface {
    @GET("/myspaces/storage")
    fun getMySpaces(@Header("member-id") memberId: String): Call<YourResponseType>

    @POST("/myspaces/storage/3")
    fun addspace(@Header("member-id") memberId: String): Call<YourResponseType>
}