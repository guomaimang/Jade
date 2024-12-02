package com.iems5722.jade.apis

import com.iems5722.jade.model.ResultWrapper
import com.iems5722.jade.pojo.Topics
import retrofit2.http.GET

interface TopicApiService {

    @GET("/topic/list")
    suspend fun getTopics(): ResultWrapper<List<Topics>>

}