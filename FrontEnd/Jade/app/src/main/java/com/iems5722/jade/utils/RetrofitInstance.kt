package com.iems5722.jade.utils

import com.iems5722.jade.apis.PictureApiService
import com.iems5722.jade.apis.UserLoginApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://jade.dev.hirsun.tech"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val pictureApiService: PictureApiService by lazy {
        retrofit.create(PictureApiService::class.java)
    }

    val userAuthApiService: UserLoginApiService by lazy {
        retrofit.create(UserLoginApiService::class.java)
    }
}
