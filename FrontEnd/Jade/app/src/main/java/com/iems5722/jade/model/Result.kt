package com.iems5722.jade.model

data class Result<T>(
    val code: Int,
    val message: String,
    val data: T?,
    val error: String
)
