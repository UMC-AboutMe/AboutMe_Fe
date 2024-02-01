package com.example.aboutme.RetrofitMyprofile

import com.example.aboutme.RetrofitMyprofileData.MainProfileData
import com.example.aboutme.RetrofitMyprofileData.PostProfile
import com.google.android.gms.fido.u2f.api.common.ResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface MainProfile {
    @GET("/myprofiles")
    @Headers("member-id: 1")
    fun getData(): Call<MainProfileData>

    @POST("/myprofiles")
    @Headers("member-id: 1")
    fun submitData(@Body data: PostProfile): Call<ResponseData>
}
