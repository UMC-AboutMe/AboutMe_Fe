package com.example.aboutme.MyprofileStorage.api

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ProfStorageItf {
    //프로필 보관함 조회
    @GET("/myprofiles/storage")
    fun getProfStorage(
        @Header("member-id") memberId:Long
    ) : Call<ProfStorageResponse.ResponeProfStorage>

    //프로필 보관함에서 특정 마이프로필 삭제
    @DELETE("/myprofiles/storage/{profile-id}")
    fun deleteProfStorage(
        @Path("profile-id") profileId : Long,
        @Header("member-id") memberId: Long
    ) : Call<ProfStorageResponse.ResponseDeleteProf>

    //프로필 보관함 즐겨찾기 등록
    @PATCH("/myprofiles/storage/{profile-id}/favorite")
    fun patchProfStorage(
        @Path("profile-id") profileId: Long,
        @Header("member-id") memberId: Int
    ) : Call<ProfStorageResponse.ResponseFavProf>
}