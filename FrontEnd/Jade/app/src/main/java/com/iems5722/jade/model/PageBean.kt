package com.iems5722.jade.model

data class PageBean<T>(
    val total: Int,
    val rows: List<T>,
    val totalPage: Int,
    val currentPage: Int
)
