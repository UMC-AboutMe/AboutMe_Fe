package com.example.aboutme.RetrofitMyprofile

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object RetrofitClient {
    private const val BASE_URL = "http://aboutme-prod-env.eba-3cw2pgyk.ap-northeast-2.elasticbeanstalk.com/"

    private const val MEMBER_ID_HEADER = "member-id"
    private const val TEMP_MEMBER_ID = "5"

    private val retrofit: Retrofit by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest: Request = chain.request()
                val requestWithHeaders: Request = originalRequest.newBuilder()
                    .header(MEMBER_ID_HEADER, TEMP_MEMBER_ID)
                    .build()
                chain.proceed(requestWithHeaders)
            }
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val mainProfile: MainProfile by lazy {
        retrofit.create(MainProfile::class.java)
    }

}
