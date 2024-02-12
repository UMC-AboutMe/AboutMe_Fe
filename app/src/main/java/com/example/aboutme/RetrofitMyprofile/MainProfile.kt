package com.example.aboutme.RetrofitMyprofile

import com.example.aboutme.RetrofitMyprofileData.DeleteMyprofile
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.RetrofitMyprofileData.MainProfileData
import com.example.aboutme.RetrofitMyprofileData.PatchDefaultProfile
import com.example.aboutme.RetrofitMyprofileData.PatchMyprofile
import com.example.aboutme.RetrofitMyprofileData.PostProfile
import com.example.aboutme.RetrofitMyprofileData.RequestPatchProfile
import com.example.aboutme.RetrofitMyprofileData.ResponsePostProfile
import com.google.android.gms.fido.u2f.api.common.ResponseData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface MainProfile {
    @GET("/myprofiles")
    //@Headers("member-id: 4")
    fun getData(): Call<MainProfileData>

    @GET("/myprofiles/{profile-id}")
    //@Headers("member-id: 4")
    suspend fun getDataAll(@Path(value = "profile-id") profileId : Long) : Response<GetAllProfile>

    @POST("/myprofiles")
    //@Headers("member-id: 4")
    fun submitData(@Body postData : PostProfile): Call<ResponsePostProfile>

    @DELETE("/myprofiles/{profile-id}")
    suspend fun deleteData(@Path(value = "profile-id") profileId: Long) : Response<DeleteMyprofile>

    @PATCH("/myprofiles/{profile-id}")
    suspend fun patchProfile(@Path(value = "profile-id") profile_id : Long, @Body patchData: RequestPatchProfile) : Response<PatchMyprofile>

    @PATCH("/myprofiles/default/{profile-id}")
    suspend fun patchDefaultProfile(@Path(value = "profile-id") profileId : Long) : Response<PatchDefaultProfile>
}
