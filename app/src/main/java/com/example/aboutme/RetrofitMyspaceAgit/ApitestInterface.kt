package com.example.aboutme.RetrofitMyspaceAgit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApitestInterface {
    // 아지트 내 스페이스 목록 조회
    @GET("/myspaces/storage")
    fun getMySpaces(@Header("member-id") memberId: String): Call<YourResponseType>

    // 마이스페이스 생성
    @POST("/myspaces/")
    fun createMySpaces(@Header("member-id") memberId: String, @Body request: MySpaceCreateRequest): Call<MySpaceCreate>

    // 아지트 내 스페이스 추가
    @POST("/myspaces/storage/3")
    fun addspace(@Header("member-id") memberId: String): Call<YourResponseType>

    // 아지트 내 스페이스 즐겨찾기
    @PATCH("/myspaces/storage/{space-id}/favorite")
    fun agitFavorite(@Path("space-id") spaceId: Long, @Header("member-id") memberId: String): Call<AgitFavoriteResponse>

    // 아지트 내 스페이스 삭제
    @DELETE("/myspaces/storage/{space-id}")
    fun deleteAgitMember(@Path("space-id") spaceId: Long, @Header("member-id") memberId: String): Call<AgitMemberDelete>
}