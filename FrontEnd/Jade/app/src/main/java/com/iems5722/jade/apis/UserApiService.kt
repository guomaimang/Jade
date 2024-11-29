package com.iems5722.jade.apis

import com.iems5722.jade.pojo.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {

    @GET("/user/info")
    fun getUserInfo(@Query("id") id: Int): Call<Result<User>>
}
