package com.example.aboutme.RetrofitMyspaceAgit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MyspaceInterface {
    @GET("/myspaces/")
    fun checkMySpace(@Header("member-id") memberId: String): Call<MyspaceCheckResponse>
}