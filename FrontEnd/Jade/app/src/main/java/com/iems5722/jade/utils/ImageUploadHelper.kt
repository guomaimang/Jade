package com.iems5722.jade.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

/**
 * 图片上传工具类
 *
 * @author CalvinHaynes
 * @since
 */
class ImageUploadHelper {

    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun createPhotoPickerLauncher(
        context: Context,
        onImageSelected: (Bitmap) -> Unit
    ) = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            uris.forEach { uri ->
                val bitmap = uriToBitmap(context, uri)
                onImageSelected(bitmap)
            }
        }
    )

    fun uploadImage(
        bitmap: Bitmap,
        url: String,
        onResponse: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                val imageBytes = stream.toByteArray()
                val encodedImage =
                    android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)

                val client = OkHttpClient()
                val requestBody = "{\"data\":\"$encodedImage\"}"
                    .toRequestBody("application/json".toMediaTypeOrNull())
                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        onResponse(response.body?.string() ?: "")
                    }
                } else {
                    throw Exception("HTTP 错误代码: ${response.code}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun uriToBitmap(context: Context, uri: Uri): Bitmap {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        return ImageDecoder.decodeBitmap(source)
    }
}

