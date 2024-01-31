package com.example.aboutme

import com.example.aboutme.Myprofile.FrontFeature
import com.example.aboutme.Myprofile.MultiProfileData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface MainProfile {
    @GET("/myprofiles")
    @Headers("member-id: 1")
    fun getData(): Call<FrontFeature>
}
