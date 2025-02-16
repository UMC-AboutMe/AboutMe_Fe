package com.example.aboutme.RetrofitMyspaceAgit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://aboutmeteam.shop"

    val apitest: ApitestInterface by lazy<ApitestInterface> {
        val okHttpClientBuilder = OkHttpClient.Builder()

        okHttpClientBuilder.addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .build()
            chain.proceed(newRequest)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
            .build()

        retrofit.create(ApitestInterface::class.java)
    }
}

object RetrofitClientMyspace {
    private const val BASE_URL = "https://aboutmeteam.shop"

    private const val MEMBER_ID_VALUE = "1"

    val apitest: MyspaceInterface by lazy<MyspaceInterface> {
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

        retrofit.create(MyspaceInterface::class.java)
    }
}

object RetrofitClient2 {
    private const val BASE_URL = "https://aboutmeteam.shop"

    val apitest: ApitestInterface by lazy<ApitestInterface> {
        val okHttpClientBuilder = OkHttpClient.Builder()

        okHttpClientBuilder.addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .build()
            chain.proceed(newRequest)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
            .build()

        retrofit.create(ApitestInterface::class.java)
    }
}
