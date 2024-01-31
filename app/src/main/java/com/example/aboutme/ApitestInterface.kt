package com.example.aboutme

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ApitestInterface {
    @GET("/myspaces/storage")
    fun getMySpaces(@Header("member-id") memberId: String): Call<YourResponseType>
}