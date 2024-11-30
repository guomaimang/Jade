package com.iems5722.jade.apis

import com.iems5722.jade.model.ResultWrapper
import com.iems5722.jade.pojo.Picture
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface PictureApiService {

    @GET("/picture/list")
    fun getPictures(
        @Query("topicId") topicId: Int,
        @Query("pageNum") pageNum: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): Call<ResultWrapper<List<Picture>>>

    @Multipart
    @POST("/picture/upload")
    fun uploadPicture(
        @Header("jwt") jwt: String,
        @Part file: MultipartBody.Part,
        @Part("picture") picture: String
    ): Call<ResultWrapper<Picture>>

    @DELETE("/picture/delete")
    fun deletePicture(@Query("pictureId") pictureId: Int): Call<ResultWrapper<Int>>

    @GET("/picture/get_file")
    fun getPictureFile(
        @Query("file_name") fileName: String,
        @Query("user_id") userId: String,
        @Query("resolution") resolution: String
    ): Call<ResponseBody>

    @GET("/picture/info")
    fun getPictureInfo(@Query("pictureId") pictureId: Int): Call<ResultWrapper<Picture>>
}
