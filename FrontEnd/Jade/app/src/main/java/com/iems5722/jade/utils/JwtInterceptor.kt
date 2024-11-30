package com.iems5722.jade.utils

import okhttp3.Interceptor
import okhttp3.Response

class JwtInterceptor(private val jwt: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // 检查请求的 URL 路径，如果是某些路径，跳过 JWT
        if (request.url.toString().contains("/userauth")) {
            return chain.proceed(request)
        }
        // 添加 jwt header
        request = chain.request().newBuilder()
            .addHeader("jwt", jwt)
            .build()
        return chain.proceed(request)
    }
}
