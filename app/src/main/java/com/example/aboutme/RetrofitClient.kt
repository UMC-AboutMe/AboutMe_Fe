package com.example.aboutme

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://aboutme-prod-env.eba-3cw2pgyk.ap-northeast-2.elasticbeanstalk.com/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val mainProfile: MainProfile by lazy {
        retrofit.create(MainProfile::class.java)
    }


}