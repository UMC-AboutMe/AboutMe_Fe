package com.example.aboutme.Search.api

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchItf {
//    fun getToken(context: Context): String {
//        val pref = context.getSharedPreferences("pref", 0)
//        val token: String? = pref.getString("token", null)
//        Log.d("token", token ?: "null")
//        return token ?: ""
//    }

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
}