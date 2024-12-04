package com.iems5722.jade.apis

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface DemoApiService {

    @Multipart
    @POST("/demo/upload")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("picture") picture: RequestBody
    ): Call<String>
}
