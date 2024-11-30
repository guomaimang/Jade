package com.iems5722.jade.apis

import com.iems5722.jade.model.ResultWrapper
import com.iems5722.jade.pojo.Message
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MessageApiService {

    @GET("/message/get_messages_by_topic_id")
    fun getMessagesByTopicId(
        @Query("topicId") topicId: Int,
        @Query("startTime") startTime: Long? = null
    ): Call<ResultWrapper<List<Message>>>

    @POST("/message/post")
    fun postMessage(@Body message: Message): Call<ResultWrapper<Void>>
}
