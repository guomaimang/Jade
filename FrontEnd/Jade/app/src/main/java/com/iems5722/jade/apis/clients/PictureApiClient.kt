package com.iems5722.jade.apis.clients

import android.content.Context
import com.iems5722.jade.model.ResultWrapper
import com.iems5722.jade.pojo.Picture
import com.iems5722.jade.utils.RetrofitInstance.pictureApiService
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PictureApiClient(context: Context) {

    private val pictureApiService = pictureApiService(context)

    // Get pictures
    fun getPictures(
        topicId: Int,
        pageNum: Int = 1,
        pageSize: Int = 20,
        callback: (ResultWrapper<List<Picture>>) -> Unit
    ) {
        pictureApiService.getPictures(topicId, pageNum, pageSize)
            .enqueue(object : Callback<ResultWrapper<List<Picture>>> {
                override fun onResponse(
                    call: Call<ResultWrapper<List<Picture>>>,
                    response: Response<ResultWrapper<List<Picture>>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.code == 0) {  // 如果 code 是 0，表示成功
                                callback(it)  // 返回 ResultWrapper
                            } else {
                                callback(
                                    ResultWrapper(
                                        500,
                                        it.message,
                                        emptyList(),
                                        it.error,
                                        it.timestamp,
                                        it.path
                                    )
                                )
                            }
                        }
                    } else {
                        callback(
                            ResultWrapper(
                                500,
                                "Error: ${response.message()}",
                                emptyList(),
                                "Network error",
                                null,
                                null
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<ResultWrapper<List<Picture>>>, t: Throwable) {
                    callback(
                        ResultWrapper(
                            500,
                            "Error: ${t.localizedMessage}",
                            emptyList(),
                            t.message,
                            null,
                            null
                        )
                    )
                }
            })
    }

    // Upload picture
    fun uploadPicture(
        file: MultipartBody.Part,
        pictureJson: String,
        callback: (ResultWrapper<Picture>) -> Unit
    ) {
        pictureApiService.uploadPicture(file, pictureJson)
            .enqueue(object : Callback<ResultWrapper<Picture>> {
                override fun onResponse(
                    call: Call<ResultWrapper<Picture>>,
                    response: Response<ResultWrapper<Picture>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.code == 0) {  // 如果 code 是 0，表示成功
                                callback(it)  // 返回 ResultWrapper
                            } else {
                                callback(
                                    ResultWrapper(
                                        500,
                                        it.message,
                                        null,
                                        it.error,
                                        it.timestamp,
                                        it.path
                                    )
                                )
                            }
                        }
                    } else {
                        callback(
                            ResultWrapper(
                                500,
                                "Error: ${response.message()}",
                                null,
                                "Network error",
                                null,
                                null
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<ResultWrapper<Picture>>, t: Throwable) {
                    callback(
                        ResultWrapper(
                            500,
                            "Error: ${t.localizedMessage}",
                            null,
                            t.message,
                            null,
                            null
                        )
                    )
                }
            })
    }

    // Delete picture
    fun deletePicture(pictureId: Int, callback: (ResultWrapper<Int>) -> Unit) {
        pictureApiService.deletePicture(pictureId).enqueue(object : Callback<ResultWrapper<Int>> {
            override fun onResponse(
                call: Call<ResultWrapper<Int>>,
                response: Response<ResultWrapper<Int>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.code == 0) {  // 如果 code 是 0，表示成功
                            callback(it)  // 返回 ResultWrapper
                        } else {
                            callback(
                                ResultWrapper(
                                    500,
                                    it.message,
                                    it.data,
                                    it.error,
                                    it.timestamp,
                                    it.path
                                )
                            )
                        }
                    }
                } else {
                    callback(
                        ResultWrapper(
                            500,
                            "Error: ${response.message()}",
                            null,
                            "Network error",
                            null,
                            null
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ResultWrapper<Int>>, t: Throwable) {
                callback(
                    ResultWrapper(
                        500,
                        "Error: ${t.localizedMessage}",
                        null,
                        t.message,
                        null,
                        null
                    )
                )
            }
        })
    }

    // Get picture file
    fun getPictureFile(
        fileName: String,
        userId: String,
        resolution: String,
        callback: (ResultWrapper<ResponseBody>) -> Unit
    ) {
        pictureApiService.getPictureFile(fileName, userId, resolution)
            .enqueue(object : Callback<ResponseBody> {  // 使用 ResponseBody 处理文件
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            // 成功获取文件
                            callback(ResultWrapper(0, "Success", it, "", null, null))
                        }
                    } else {
                        // 如果响应失败
                        callback(
                            ResultWrapper(
                                500,
                                "Error: ${response.message()}",
                                null,
                                "Network error",
                                null,
                                null
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // 网络请求失败
                    callback(
                        ResultWrapper(
                            500,
                            "Error: ${t.localizedMessage}",
                            null,
                            t.message,
                            null,
                            null
                        )
                    )
                }
            })
    }
}

// Get picture info
//fun getPictureInfo(pictureId: Int, callback: (ResultWrapper<Picture>) -> Unit) {
//    pictureApiService.getPictureInfo(pictureId)
//        .enqueue(object : Callback<ResultWrapper<Picture>> {
//            override fun onResponse(
//                call: Call<ResultWrapper<Picture>>,
//                response: Response<ResultWrapper<Picture>>
//            ) {
//                if (response.isSuccessful) {
//                    response.body()?.let {
//                        if (it.code == 0) {  // 如果 code 是 0，表示成功
//                            callback(it)  // 返回 ResultWrapper
//                        } else {
//                            callback(
//                                ResultWrapper(
//                                    500,
//                                    it.message,
//                                    null,
//                                    it.error,
//                                    it.timestamp,
//                                    it.path
//                                )
//                            )
//                        }
//                    }
//                } else {
//                    callback(
//                        ResultWrapper(
//                            500,
//                            "Error: ${response.message()}",
//                            null,
//                            "Network error",
//                            null,
//                            null
//                        )
//                    )
//                }
//            }
//
//            override fun onFailure(call: Call<ResultWrapper<Picture>>, t: Throwable) {
//                callback(
//                    ResultWrapper(
//                        500,
//                        "Error: ${t.localizedMessage}",
//                        null,
//                        t.message,
//                        null,
//                        null
//                    )
//                )
//            }
//        })
//}

