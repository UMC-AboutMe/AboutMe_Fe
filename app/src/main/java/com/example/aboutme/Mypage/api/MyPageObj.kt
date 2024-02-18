package com.example.aboutme.Mypage.api

import com.example.aboutme.MyprofileStorage.api.ProfStorageItf
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object MyPageObj  {

    private const val BASE_URL = "https://aboutmeteam.shop"


    private val getRetrofit by lazy{

        val clientBuilder = OkHttpClient.Builder()

        clientBuilder.readTimeout(30, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(30, TimeUnit.SECONDS)

        val client = clientBuilder.build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val getRetrofitService : MyPageItf by lazy { getRetrofit.create(MyPageItf::class.java) }
}