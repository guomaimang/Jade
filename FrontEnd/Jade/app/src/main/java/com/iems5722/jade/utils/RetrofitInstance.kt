package com.iems5722.jade.utils

import com.iems5722.jade.apis.MessageApiService
import com.iems5722.jade.apis.PictureApiService
import com.iems5722.jade.apis.TopicApiService
import com.iems5722.jade.apis.UserApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance(jwt: String) {

    private val BASE_URL = "https://jade.dev.hirsun.tech"
    private val jwt = jwt

    // 创建 OkHttpClient，延迟获取JWT并添加JWT拦截器
    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(JwtInterceptor(jwt))
            .build()
    }

    // 需要在每次使用时创建 Retrofit 实例
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun pictureApiService(): PictureApiService {
        return getRetrofit().create(PictureApiService::class.java)
    }

    fun userApiService(): UserApiService {
        return getRetrofit().create(UserApiService::class.java)
    }

    fun topicApiService(): TopicApiService {
        return getRetrofit().create(TopicApiService::class.java)
    }

    fun messageApiService(): MessageApiService {
        return getRetrofit().create(MessageApiService::class.java)
    }
}
