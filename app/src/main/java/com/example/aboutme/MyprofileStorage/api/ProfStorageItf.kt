package com.example.aboutme.MyprofileStorage.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ProfStorageItf {
    //프로필 보관함 조회
    @GET("/myprofiles/storage")
    fun getProfStorage(
        @Header("member-id") memberId:Long
    ) : Call<ProfStorageResponse.ResponeProfStorage>
}