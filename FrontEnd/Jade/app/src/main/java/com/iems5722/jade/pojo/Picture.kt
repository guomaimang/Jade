package com.iems5722.jade.pojo

import java.util.Date

data class Picture(
    val id: Int,
    val userId: Int,
    val topicId: Int,
    val fileName: String,
    val viewCount: Int,
    val location: String,
    val coordinateX: String,
    val coordinateY: String,
    val title: String,
    val description: String,
    val createTime: Date,
    val exifSize: String,
    val exifTime: String,
    val exifLatitude: String,
    val exifLongitude: String,
    val exifLocation: String,
    val exifDevice: String
)
