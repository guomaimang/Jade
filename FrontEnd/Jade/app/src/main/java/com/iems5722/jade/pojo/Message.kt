package com.iems5722.jade.pojo

import java.util.Date

data class Message(
    val id: String,
    val createTime: Date?,
    val userId: Int,
    val type: Int?,
    val content: String,
    val nickname: String?,
    val topicId: Int
)
