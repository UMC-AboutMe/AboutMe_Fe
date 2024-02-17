package com.example.aboutme.MyprofileStorage.api

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfStorageItf {
    //프로필 보관함 조회
    @GET("/myprofiles/storage")
    fun getProfStorage(
        @Header("token") token:String
    ) : Call<ProfStorageResponse.ResponeProfStorage>

    //프로필 보관함에서 특정 마이프로필 삭제
    @DELETE("/myprofiles/storage/{profile-id}")
    fun deleteProfStorage(
        @Path("profileId") profileId : Long,
        @Header("token") token:String
    ) : Call<ProfStorageResponse.ResponseDeleteProf>

    //프로필 보관함 즐겨찾기 등록
    @PATCH("/myprofiles/storage/{profile-id}/favorite")
    fun patchProfStorage(
        @Path("profile-id") profileId: Long,
        @Header("token") token:String
    ) : Call<ProfStorageResponse.ResponseFavProf>

    //프로필 보관함 검색
    @GET("/myprofiles/storage/search")
    fun getSearchProf(
        @Query("keyword") keyword : String? ,
        @Header("token") token:String
    ) : Call <ProfStorageResponse.ResponseSearchProf>

    //마이프로필 조회 - 단건
    @GET("/myprofiles/{profile-id}")
    fun getProfList(
        @Path("profile-id") profileId: Long,
        ) : Call <ProfStorageResponse.ResponseProf>
}