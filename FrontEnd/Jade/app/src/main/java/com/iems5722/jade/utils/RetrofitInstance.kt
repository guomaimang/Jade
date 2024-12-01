package com.iems5722.jade.utils

import android.content.Context
import com.iems5722.jade.apis.PictureApiService
import com.iems5722.jade.apis.UserLoginApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://jade.dev.hirsun.tech"

    // 创建 OkHttpClient，延迟获取JWT并添加JWT拦截器
    private fun getOkHttpClient(context: Context): OkHttpClient {
        val jwt = UserPrefs.getJwt(context)
        return OkHttpClient.Builder()
            .addInterceptor(JwtInterceptor(jwt.toString()))
            .build()
    }

    // 需要在每次使用时创建 Retrofit 实例
    private fun getRetrofit(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 每次请求时通过传入 context 获取 api 服务
    fun pictureApiService(context: Context): PictureApiService {
        return getRetrofit(context).create(PictureApiService::class.java)
    }

    fun userLoginApiService(context: Context): UserLoginApiService {
        return getRetrofit(context).create(UserLoginApiService::class.java)
    }
}
