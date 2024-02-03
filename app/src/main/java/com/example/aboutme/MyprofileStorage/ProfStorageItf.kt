package com.example.aboutme.MyprofileStorage

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ProfStorageItf {
    @PATCH("/myprofiles/storage/{profile-id}/favorite")

    fun getProfStorage(
        @Path("profile-id") profileId : String,
        @Header("member-id") memberId : String
        ) : Call<ProfStorageData>
}