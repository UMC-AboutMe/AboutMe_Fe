package com.example.aboutme.MyprofileStorage

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProfStorageClient {
    private const val BASE_URL =
        "http://aboutme-prod-env.eba-3cw2pgyk.ap-northeast-2.elasticbeanstalk.com"

    //임시값
    private const val MEMBER_ID_VALUE = "1"

    val apitest: ProfStorageItf by lazy<ProfStorageItf> {
        val okHttpClientBuilder = OkHttpClient.Builder()

        okHttpClientBuilder.addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("member-id", MEMBER_ID_VALUE)
                .build()
            chain.proceed(newRequest)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
            .build()

        retrofit.create(ProfStorageItf::class.java)
    }
}