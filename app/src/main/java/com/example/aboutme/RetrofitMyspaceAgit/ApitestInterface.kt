package com.example.aboutme.RetrofitMyspaceAgit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApitestInterface {
    @GET("/myspaces/storage")
    fun getMySpaces(@Header("member-id") memberId: String): Call<YourResponseType>

    @POST("/myspaces/")
    fun createMySpaces(@Header("member-id") memberId: String, @Body request: MySpaceCreateRequest): Call<MySpaceCreate>

    @POST("/myspaces/storage/3")
    fun addspace(@Header("member-id") memberId: String): Call<YourResponseType>

    @PATCH("/myspaces/storage/{space-id}/favorite")
    fun agitFavorite(@Path("space-id") spaceId: Long, @Header("member-id") memberId: String): Call<AgitFavoriteResponse>
}