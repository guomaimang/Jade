package com.iems5722.jade.apis

import com.iems5722.jade.model.ResultWrapper
import com.iems5722.jade.pojo.User
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {

    @GET("/user/info")
    suspend fun getUserInfo(@Query("id") id: Int): ResultWrapper<User>
}
