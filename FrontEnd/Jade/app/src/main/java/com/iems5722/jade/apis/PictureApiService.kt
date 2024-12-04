package com.iems5722.jade.apis

import com.iems5722.jade.model.PageBean
import com.iems5722.jade.model.ResultWrapper
import com.iems5722.jade.pojo.Picture
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface PictureApiService {

    @GET("/picture/list")
    suspend fun getPictures(
        @Query("topicId") topicId: Int,
        @Query("pageNum") pageNum: Int = 1,
        @Query("pageSize") pageSize: Int = 100
    ): ResultWrapper<PageBean<Picture>>

    @GET("/picture/myself")
    suspend fun getPicturesMyself(
        @Query("pageNum") pageNum: Int = 1,
        @Query("pageSize") pageSize: Int = 100
    ): ResultWrapper<PageBean<Picture>>

    @Multipart
    @POST("/picture/upload")
    suspend fun uploadPicture(
        @Part file: MultipartBody.Part,
        @Part("picture") picture: RequestBody
    ): ResultWrapper<Picture>

    @DELETE("/picture/delete")
    suspend fun deletePicture(@Query("pictureId") pictureId: Int): ResultWrapper<Int>

    @GET("/picture/get_file")
    suspend fun getPictureFile(
        @Query("file_name") fileName: String,
        @Query("user_id") userId: String,
        @Query("resolution") resolution: String
    ): Response<ResponseBody>

    @GET("/picture/info")
    suspend fun getPictureInfo(@Query("pictureId") pictureId: Int): ResultWrapper<Picture>
}
