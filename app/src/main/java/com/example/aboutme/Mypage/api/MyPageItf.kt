package com.example.aboutme.Mypage.api

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header

interface MyPageItf {
    @GET("/mypages")
    fun getMypage (
        @Header ("member-id") memberId : Long
    ) : Call<MyPageResponse.ResponseMypage>

    @DELETE("/members/unregister")
    fun deleteUser (
        @Header ("member-id") memberId : Long
    ): Call<MyPageResponse.ResponseDeleteUser>
}