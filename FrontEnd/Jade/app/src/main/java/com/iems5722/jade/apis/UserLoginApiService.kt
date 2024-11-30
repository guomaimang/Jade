package com.iems5722.jade.apis

import com.iems5722.jade.model.ResultWrapper
import com.iems5722.jade.pojo.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import java.util.UUID

interface UserLoginApiService {

    // Login API
    @POST("/userauth/login")
    fun login(@Body userAttempted: User): Call<ResultWrapper<Map<String, Any>>>

    // Refresh Token API
    @PUT("/userauth/refresh_token")
    fun refreshToken(@Body map: Map<String, String>): Call<ResultWrapper<String>>

    // Generate WebSocket Token API
    @GET("/userauth/gen_ws_token")
    fun generateWsToken(@Header("jwt") jwt: String): Call<ResultWrapper<UUID>>

    // SSO
    @POST("/userauth/oauth2/callback")
    fun callback(@Body code: String): Call<ResultWrapper<Map<String, Any>>>
}
