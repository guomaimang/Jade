package com.iems5722.jade.model

import java.util.Date

data class ResultWrapper<T>(
    val code: Int,
    val message: String,
    val data: T?,
    val error: String?,
    val timestamp: Date?,
    val path: String?
)
