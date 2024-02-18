package com.example.aboutme.Search.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object SearchObj {
    private const val BASE_URL = "http://aboutme-prod-env.eba-wbmipxyp.ap-northeast-2.elasticbeanstalk.com"

    private val getRetrofit by lazy{

        val clientBuilder = OkHttpClient.Builder()

        clientBuilder.readTimeout(30, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(30,TimeUnit.SECONDS)

        val client = clientBuilder.build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val getRetrofitService :  SearchItf by lazy { getRetrofit.create(SearchItf::class.java) }
}