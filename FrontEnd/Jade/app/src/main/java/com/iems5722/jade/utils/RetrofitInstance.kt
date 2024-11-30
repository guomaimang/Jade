package com.iems5722.jade.utils

import com.iems5722.jade.apis.PictureApiService
import com.iems5722.jade.apis.UserLoginApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://jade.dev.hirsun.tech"

    private const val jwt =
        "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZW1haWwiOiJqaWFtaW5nLmhhbkBsaW5rLmN1aGsuZWR1LmhrIiwiZXhwIjoxNzM0MTc1NTk3fQ.tx4dmec6EkMpOdXmiYJ6vsJcrR9CJ5bM9325pS-x1r"

    // 创建 OkHttpClient，添加JWT拦截器
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(JwtInterceptor(jwt))
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val pictureApiService: PictureApiService by lazy {
        retrofit.create(PictureApiService::class.java)
    }

    val userLoginApiService: UserLoginApiService by lazy {
        retrofit.create(UserLoginApiService::class.java)
    }
}
