package com.iems5722.jade.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.iems5722.jade.apis.PictureApiService
import com.iems5722.jade.apis.TopicApiService
import com.iems5722.jade.apis.UserApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://jade.dev.hirsun.tech"

    // 创建 OkHttpClient，延迟获取JWT并添加JWT拦截器
    @Composable
    fun getOkHttpClient(): OkHttpClient {
        val context = LocalContext.current
        val jwt = UserPrefs.getJwt(context)
        return OkHttpClient.Builder()
            .addInterceptor(JwtInterceptor(jwt.toString()))
            .build()
    }

    // 需要在每次使用时创建 Retrofit 实例
    @Composable
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 每次请求时通过传入 context 获取 api 服务
    @Composable
    fun pictureApiService(): PictureApiService {
        return getRetrofit().create(PictureApiService::class.java)
    }

    @Composable
    fun userApiService(): UserApiService {
        return getRetrofit().create(UserApiService::class.java)
    }

    @Composable
    fun topicApiService(): TopicApiService {
        return getRetrofit().create(TopicApiService::class.java)
    }

}
