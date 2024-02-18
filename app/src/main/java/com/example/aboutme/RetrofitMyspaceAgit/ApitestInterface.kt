package com.example.aboutme.RetrofitMyspaceAgit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApitestInterface {
    // 아지트 내 스페이스 목록 조회
    @GET("/myspaces/storage")
    fun getMySpaces(@Header("token") token: String): Call<YourResponseType>

    // 마이스페이스 생성
    @POST("/myspaces/")
    fun createMySpaces(@Header("token") token: String, @Body request: MySpaceCreateRequest): Call<MySpaceCreate>

    // 아지트 내 스페이스 추가
    @POST("/myspaces/storage/3")
    fun addspace(@Header("token") token: String): Call<YourResponseType>

    // 아지트 내 스페이스 즐겨찾기
    @PATCH("/myspaces/storage/{space-id}/favorite")
    fun agitFavorite(@Path("space-id") spaceId: Long, @Header("token") token: String): Call<AgitFavoriteResponse>

    // 아지트 내 스페이스 삭제
    @DELETE("/myspaces/storage/{space-id}")
    fun deleteAgitMember(@Path("space-id") spaceId: Long, @Header("token") token: String): Call<AgitMemberDelete>

    // 스페이스 검색
    @GET("/myspaces/search")
    fun searchAgitMember(@Query("keyword") keyword: String): Call<AgitSpaceSearchResponse>
}