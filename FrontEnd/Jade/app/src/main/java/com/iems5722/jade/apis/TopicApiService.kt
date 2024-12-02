package com.iems5722.jade.apis

import com.iems5722.jade.model.ResultWrapper
import com.iems5722.jade.pojo.Topics
import retrofit2.Call
import retrofit2.http.GET

interface TopicApiService {

    @GET("/topic/list")
    fun getTopics(): Call<ResultWrapper<List<Topics>>>

}