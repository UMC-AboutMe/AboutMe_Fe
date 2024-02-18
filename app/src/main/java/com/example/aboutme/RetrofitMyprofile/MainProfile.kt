package com.example.aboutme.RetrofitMyprofile

import com.example.aboutme.RetrofitMyprofileData.DeleteMyprofile
import com.example.aboutme.RetrofitMyprofileData.GetAllProfile
import com.example.aboutme.RetrofitMyprofileData.MainProfileData
import com.example.aboutme.RetrofitMyprofileData.PatchDefaultProfile
import com.example.aboutme.RetrofitMyprofileData.PatchMyprofile
import com.example.aboutme.RetrofitMyprofileData.PatchProfileImage
import com.example.aboutme.RetrofitMyprofileData.PostProfile
import com.example.aboutme.RetrofitMyprofileData.RequestPatchProfile
import com.example.aboutme.RetrofitMyprofileData.RequestProfileImage
import com.example.aboutme.RetrofitMyprofileData.ResponsePostProfile
import com.google.android.gms.fido.u2f.api.common.ResponseData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface MainProfile {
    @GET("/myprofiles")
    fun getData(@Header("token") token:String,
    ): Call<MainProfileData>

    @GET("/myprofiles/{profile-id}")
    suspend fun getDataAll(@Header("token") token:String,
        @Path(value = "profile-id") profileId : Long) : Response<GetAllProfile>

    @POST("/myprofiles")
    fun submitData(@Header("token") token:String,
        @Body postData : PostProfile): Call<ResponsePostProfile>

    @DELETE("/myprofiles/{profile-id}")
    suspend fun deleteData(@Header("token") token:String,
        @Path(value = "profile-id") profileId: Long) : Response<DeleteMyprofile>

    @PATCH("/myprofiles/{profile-id}")
    suspend fun patchProfile(@Header("token") token:String,
        @Path(value = "profile-id") profile_id : Long, @Body patchData: RequestPatchProfile) : Response<PatchMyprofile>

    @PATCH("/myprofiles/default/{profile-id}")
    suspend fun patchDefaultProfile(@Header("token") token:String,
        @Path(value = "profile-id") profileId : Long) : Response<PatchDefaultProfile>

    @Multipart
    @PATCH("/myprofiles/{profile-id}/image")
    fun patchProfileImage(@Header("token") token:String,
        @Path(value = "profile-id") profileId: Long, @Part("body") body: RequestBody, @Part image :MultipartBody.Part?):Call<PatchProfileImage>
}
