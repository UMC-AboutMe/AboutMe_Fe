package com.example.aboutme

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface LoginItf {
    @POST("/members/{socialType}/loginEmail")
    fun postLogin (
        @Path("socialType") type:String,
        @Body request : LoginResponse.RequestLogin
    ) : Call<LoginResponse.ResponseLogin>

    @POST("/members/{socialType}/login")
    fun postLoginKakao (
        @Path("socialType") type:String,
        @Body request : LoginResponse.RequestLoginAT
    ) : Call<LoginResponse.ResponseLogin>
}