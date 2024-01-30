package com.example.aboutme

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface MainProfile {
    @GET("/myprofiles")
    fun getData(): Call<ResponseBody>
}
