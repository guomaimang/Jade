package com.iems5722.jade.utils

import android.content.Context
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class HttpClient(private val context: Context) {

    private val client: OkHttpClient = OkHttpClient()

    private val baseUrl: String = "https://jade.dev.hirsun.tech"

    // 获取 JWT Token
    private fun getJwtToken(): String? {
        return UserPrefs.getJwt(context)
    }

    // 构造完整的 URL
    private fun buildUrl(path: String): String {
        return "$baseUrl$path"
    }

    // 通用 GET 请求
    fun get(path: String, callback: Callback) {
        val url = buildUrl(path)
        val jwtToken = getJwtToken()

        val requestBuilder = Request.Builder()
            .url(url)

        // 如果有 JWT Token，则添加到请求头中，key 为 "jwt"
        jwtToken?.let {
            requestBuilder.addHeader("jwt", it)
        }

        val request = requestBuilder.build()
        client.newCall(request).enqueue(callback)
    }

    // 通用 POST 请求
    fun post(path: String, body: RequestBody, callback: Callback) {
        val url = buildUrl(path)
        val jwtToken = getJwtToken()

        val requestBuilder = Request.Builder()
            .url(url)
            .post(body)

        // 如果有 JWT Token，则添加到请求头中，key 为 "jwt"
        jwtToken?.let {
            requestBuilder.addHeader("jwt", it)
        }

        val request = requestBuilder.build()
        client.newCall(request).enqueue(callback)
    }

    // 通用上传文件请求
    fun uploadFile(path: String, file: File, callback: Callback) {
        val url = buildUrl(path)
        val jwtToken = getJwtToken()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                file.name,
                file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            )
            .build()

        val requestBuilder = Request.Builder()
            .url(url)
            .post(requestBody)

        // 如果有 JWT Token，则添加到请求头中，key 为 "jwt"
        jwtToken?.let {
            requestBuilder.addHeader("jwt", it)
        }

        val request = requestBuilder.build()
        client.newCall(request).enqueue(callback)
    }

    // 通用 DELETE 请求
    fun delete(path: String, callback: Callback) {
        val url = buildUrl(path)
        val jwtToken = getJwtToken()

        val requestBuilder = Request.Builder()
            .url(url)
            .delete()

        // 如果有 JWT Token，则添加到请求头中，key 为 "jwt"
        jwtToken?.let {
            requestBuilder.addHeader("jwt", it)
        }

        val request = requestBuilder.build()
        client.newCall(request).enqueue(callback)
    }
}
