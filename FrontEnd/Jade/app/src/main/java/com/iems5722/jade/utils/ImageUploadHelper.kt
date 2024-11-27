package com.iems5722.jade.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.iems5722.jade.activities.PostEdit
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

    companion object {
        private const val IMAGE_UPLOAD_URL =
            "https://run.mocky.io/v3/fea3a252-8760-4180-bae9-4a6f29a242af"
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun setupMediaPickerWithUpload(
        context: Context,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = { error ->
            Toast.makeText(context, "错误: $error", Toast.LENGTH_LONG).show()
        }
    ) = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            if (uris.isEmpty()) {
                onError("未选择任何媒体文件")
                return@rememberLauncherForActivityResult
            }

            val selectedImages = uris.mapNotNull { uri ->
                try {
                    val bitmap = uriToBitmap(context, uri)
                    bitmap to uri
                } catch (e: Exception) {
                    onError("无法处理文件: $uri")
                    null
                }
            }

            if (selectedImages.isNotEmpty()) {
                uploadImagesWithNavigationMock(context, selectedImages, onSuccess, onError)
//                uploadImagesWithNavigation(context, selectedImages, onSuccess, onError)
            }
        }
    )

    private fun uploadImagesWithNavigation(
        context: Context,
        images: List<Pair<Bitmap, Uri>>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uploadedUris = mutableListOf<Uri>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                images.forEach { (bitmap, uri) ->
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                    val imageBytes = stream.toByteArray()
                    val encodedImage =
                        android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)

                    val client = OkHttpClient()
                    val requestBody = "{\"data\":\"$encodedImage\"}"
                        .toRequestBody("application/json".toMediaTypeOrNull())
                    val request = Request.Builder()
                        .url(IMAGE_UPLOAD_URL)
                        .post(requestBody)
                        .build()

                    val response = client.newCall(request).execute()
                    if (response.isSuccessful) {
                        uploadedUris.add(uri)
                    } else {
                        throw Exception("HTTP 错误代码: ${response.code}")
                    }
                }

                withContext(Dispatchers.Main) {
                    // 跳转到目标页面
                    val intent = Intent(context, PostEdit::class.java).apply {
                        putParcelableArrayListExtra("selected_images", ArrayList(uploadedUris))
                    }
                    context.startActivity(intent)
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onError(e.message ?: "未知错误")
                }
            }
        }
    }

    private fun uploadImagesWithNavigationMock(
        context: Context,
        images: List<Pair<Bitmap, Uri>>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 模拟延时，模拟上传逻辑
                kotlinx.coroutines.delay(1000)
                val uploadedUris = images.map { it.second }

                withContext(Dispatchers.Main) {
                    // 模拟上传成功后跳转
                    val intent = Intent(context, PostEdit::class.java).apply {
                        putParcelableArrayListExtra("selected_images", ArrayList(uploadedUris))
                    }
                    context.startActivity(intent)
                    onSuccess()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("模拟上传失败: ${e.message}")
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
