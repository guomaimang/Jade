package com.iems5722.jade.apis.clients

import com.iems5722.jade.model.ResultWrapper
import com.iems5722.jade.pojo.User
import com.iems5722.jade.utils.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class UserLoginApiClient(private val jwt: String? = null) {

    private val userLoginApiService = RetrofitInstance.userLoginApiService

    // 登录API
    fun login(userAttempted: User, callback: (ResultWrapper<Map<String, Any>>) -> Unit) {
        userLoginApiService.login(userAttempted)
            .enqueue(object : Callback<ResultWrapper<Map<String, Any>>> {
                override fun onResponse(
                    call: Call<ResultWrapper<Map<String, Any>>>,
                    response: Response<ResultWrapper<Map<String, Any>>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            callback(ResultWrapper(0, "Success", it.data, "", null, null))
                        }
                    } else {
                        callback(
                            ResultWrapper(
                                500,
                                "Error: ${response.message()}",
                                null,
                                "Network error",
                                null,
                                null
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<ResultWrapper<Map<String, Any>>>, t: Throwable) {
                    callback(
                        ResultWrapper(
                            500,
                            "Error: ${t.localizedMessage}",
                            null,
                            t.message,
                            null,
                            null
                        )
                    )
                }
            })
    }

    // 刷新Token API
    fun refreshToken(map: Map<String, String>, callback: (ResultWrapper<String>) -> Unit) {
        userLoginApiService.refreshToken(map).enqueue(object : Callback<ResultWrapper<String>> {
            override fun onResponse(
                call: Call<ResultWrapper<String>>,
                response: Response<ResultWrapper<String>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(ResultWrapper(0, "Success", it.data, "", null, null))
                    }
                } else {
                    callback(
                        ResultWrapper(
                            500,
                            "Error: ${response.message()}",
                            null,
                            "Network error",
                            null,
                            null
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ResultWrapper<String>>, t: Throwable) {
                callback(
                    ResultWrapper(
                        500,
                        "Error: ${t.localizedMessage}",
                        null,
                        t.message,
                        null,
                        null
                    )
                )
            }
        })
    }

    // 生成 WebSocket Token API
    fun generateWsToken(callback: (ResultWrapper<UUID>) -> Unit) {
        userLoginApiService.generateWsToken(jwt ?: "")
            .enqueue(object : Callback<ResultWrapper<UUID>> {
                override fun onResponse(
                    call: Call<ResultWrapper<UUID>>,
                    response: Response<ResultWrapper<UUID>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            callback(ResultWrapper(0, "Success", it.data, "", null, null))
                        }
                    } else {
                        callback(
                            ResultWrapper(
                                500,
                                "Error: ${response.message()}",
                                null,
                                "Network error",
                                null,
                                null
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<ResultWrapper<UUID>>, t: Throwable) {
                    callback(
                        ResultWrapper(
                            500,
                            "Error: ${t.localizedMessage}",
                            null,
                            t.message,
                            null,
                            null
                        )
                    )
                }
            })
    }

    // SSO 登录回调
    fun callback(code: String, callback: (ResultWrapper<Map<String, Any>>) -> Unit) {
        userLoginApiService.callback(code)
            .enqueue(object : Callback<ResultWrapper<Map<String, Any>>> {
                override fun onResponse(
                    call: Call<ResultWrapper<Map<String, Any>>>,
                    response: Response<ResultWrapper<Map<String, Any>>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            callback(ResultWrapper(0, "Success", it.data, "", null, null))
                        }
                    } else {
                        callback(
                            ResultWrapper(
                                500,
                                "Error: ${response.message()}",
                                null,
                                "Network error",
                                null,
                                null
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<ResultWrapper<Map<String, Any>>>, t: Throwable) {
                    callback(
                        ResultWrapper(
                            500,
                            "Error: ${t.localizedMessage}",
                            null,
                            t.message,
                            null,
                            null
                        )
                    )
                }
            })
    }
}
