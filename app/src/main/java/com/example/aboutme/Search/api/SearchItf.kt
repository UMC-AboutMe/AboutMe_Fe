package com.example.aboutme.Search.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchItf {
    //상대방 마이프로필 내 보관함에 추가하기
    @POST("/myprofiles/share")
    fun postProfStorage(
        @Header("member-id") memberId:Long,
        @Body request : SearchResponse.RequestStoreProf
    ) : Call<SearchResponse.ResponseStoreProf>

    //스페이스 검색
    @GET("/myspaces/search")
    fun getSearchSpace(
        @Query("keyword") keyword:String?
    ) : Call<SearchResponse.ResponseSearchSpace>

    //상대방 스페이스 내 보관함에 추가하기
    @POST("/myspaces/storage/{space-id}")
    fun postSpaceStorage (
        @Path ("space-id") spaceId:Long,
        @Header("member-id") memberId:Int
    ) : Call<SearchResponse.ResponseSpaceStorage>

    //내 프로필 상대방에게 공유
    @POST ("/myprofiles/share/mine")
    fun postShareProf(
    @Header("member-id") memberId:Int,
    @Body requestShareProf : SearchResponse.RequestShareProf
    ) : Call<SearchResponse.ResponseShareProf>

    //내 마이프로필 조회
    @GET ("/myprofiles")
    fun getProfileList (
        @Header ("member-id") memberId: Long
    ) : Call<SearchResponse.ResponseGetProfiles>

    //마이프로필 검색
    @GET ("/myprofiles/search")
    fun getSearchProf (
        @Query("q") q:Int
    ) : Call<SearchResponse.ResponseSearchProf>
}