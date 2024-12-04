package com.iems5722.jade.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import coil3.compose.rememberAsyncImagePainter
import com.iems5722.jade.R
import com.iems5722.jade.ui.theme.JadeTheme
import com.iems5722.jade.utils.MyLocationListener
import com.iems5722.jade.utils.RetrofitInstance
import com.iems5722.jade.utils.UserPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.InputStream

@Suppress("DEPRECATION")
class PostEdit : ComponentActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private lateinit var locationListener: MyLocationListener

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestLocationPermission()

        // 从 Intent 中获取选中的图片 URI 列表
        val selectedImage = intent.getStringExtra("selected_image")
        val selectedImageUri = Uri.parse(selectedImage)

        setContent {
            JadeTheme {
                Scaffold {
                    PostEditScreen(selectedImageUri)
                }
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    @SuppressLint("MissingPermission", "UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PostEditScreen(selectedImage: Uri) {
        val context = LocalContext.current
        locationListener = MyLocationListener(this)

        val jwt = UserPrefs.getJwt(context).toString()

        var title by remember { mutableStateOf("") }
        var content by remember { mutableStateOf("") }
        var tag by remember { mutableStateOf<Int?>(null) }
        var location by remember { mutableStateOf("Add Location") }

        val pictureApiService = RetrofitInstance(jwt).pictureApiService()

        var topics by remember { mutableStateOf(listOf<Topics>()) }
        val topicApiService = RetrofitInstance(jwt).topicApiService()
        LaunchedEffect(true) {
            try {
                val response = withContext(Dispatchers.IO) {
                    topicApiService.getTopics()
                }

                if (response.code == 0 && response.data != null) {
                    val topicsTemp = response.data
                    val topicsList = topicsTemp.map { topic ->
                        Topics(
                            id = topic.id, tag = topic.tag
                        )
                    }

                    withContext(Dispatchers.Main) {
                        topics = topicsList
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ChatActivity", "Error loading messages: ${e.localizedMessage}")
            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            TopAppBar(title = { Text("Edit Post") }, navigationIcon = {
                IconButton(onClick = {
                    val activity = context as? Activity
                    activity?.finish()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Back"
                    )
                }
            }, actions = {
                IconButton(
                    onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                val jsonObject = JSONObject()
                                jsonObject.put("title", title)
                                jsonObject.put("description", content)
                                jsonObject.put("topicId", tag)
                                if (location != "Add Location") {
                                    // 解析 location 字符串
                                    val coordinates = location.trim('(', ')').split(",")
                                    val coordinateX = coordinates[0]
                                    val coordinateY = coordinates[1]
                                    jsonObject.put("coordinateX", coordinateX)
                                    jsonObject.put("coordinateY", coordinateY)
                                }
                                val jsonString = jsonObject.toString()
                                val requestBody = RequestBody.create(
                                    "application/json".toMediaTypeOrNull(),
                                    jsonString
                                )
                                println("jsonString: $jsonString")

                                val fileParam = createMultipartBodyPart(context, selectedImage)

                                val response = withContext(Dispatchers.IO) {
                                    fileParam?.let {
                                        pictureApiService.uploadPicture(
                                            it,
                                            requestBody
                                        )
                                    }
                                }

                                if (response != null) {
                                    if (response.code == 0 && response.data != null) {
                                        withContext(Dispatchers.Main) {
                                            // 上传成功
                                            Toast.makeText(
                                                context, "Upload Successful", Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        throw Exception("Failed to fetch picture data or data is empty")
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    // 上传成功
                                    Toast.makeText(
                                        context, "Error: ${e.message}", Toast.LENGTH_SHORT
                                    ).show()
                                }
                                Log.e("TopicScreen", "Error: ${e.message}")
                            }
                        }
                        // 退出当前界面
                        (context as? Activity)?.finish()
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.upload),
                        contentDescription = "upload"
                    )
                }
            })
            // 图片展示区域
            Image(
                painter = rememberAsyncImagePainter(selectedImage),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { /* Image click action */ },
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.UploadTitle),
                modifier = Modifier.padding(16.dp, 0.dp)
            )
            BasicTextField(
                value = title,
                onValueChange = { title = it },
                textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
                decorationBox = { innerTextField ->
                    if (title.isEmpty()) {
                        Text("Add Title", color = Color.Gray)
                    }
                    innerTextField()
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.UploadContent),
                modifier = Modifier.padding(16.dp, 0.dp)
            )
            // 正文输入框
            BasicTextField(
                value = content,
                onValueChange = { content = it },
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp)
                    .height(200.dp),
                decorationBox = { innerTextField ->
                    if (content.isEmpty()) {
                        Text("Share your thoughts...", color = Color.Gray)
                    }
                    innerTextField()
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 标签添加区域
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                topics.forEach { topic ->
                    item {
                        val tagColor = if (tag == topic.id) Color.Cyan else Color.LightGray
                        Box(modifier = Modifier
                            .background(tagColor, RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .clickable {
                                // 点击标签时更新tag的值为当前topic.id
                                tag = topic.id
                            }) {
                            Text("#${topic.tag}", color = Color.Black)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 位置信息等其他选项
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable {
                        locationListener.updateLocation()
                        location = locationListener.getLocationString()
                    }, contentAlignment = Alignment.CenterStart
            ) {
                if (location == stringResource(R.string.NoLocation)) {
                    Toast.makeText(
                        LocalContext.current, "Failed to get location", Toast.LENGTH_SHORT
                    ).show()
                    location = "Add Location"
                }
                Text(text = location, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

// 获取文件的 MultipartBody.Part
@SuppressLint("Range")
fun createMultipartBodyPart(context: Context, uri: Uri): MultipartBody.Part? {
    // 获取文件的 InputStream
    val contentResolver: ContentResolver = context.contentResolver
    val inputStream: InputStream? = contentResolver.openInputStream(uri)

    // 确保文件存在且 InputStream 不为 null
    if (inputStream != null) {
        // 获取文件的 MIME 类型
        val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"

        // 将 InputStream 转换为 RequestBody
        val requestBody = inputStream.readBytes().toRequestBody(mimeType.toMediaTypeOrNull())

        // 获取文件名
        val fileName = uri.lastPathSegment ?: "default_name.jpg"

        // 创建 MultipartBody.Part
        return MultipartBody.Part.createFormData("file", fileName, requestBody)
    }
    return null
}


@Composable
fun MyButtonWithProgress() {
    val context = LocalContext.current
    var isUploading by remember { mutableStateOf(false) }  // 控制上传中状态
    val progress = remember { mutableStateOf(0f) }
    var uploadSuccess by remember { mutableStateOf<Boolean?>(null) }  // 控制上传成功或失败的状态
    // 当 isUploading 状态变化时，触发 LaunchedEffect
    LaunchedEffect(isUploading) {
        if (isUploading) {
            // 模拟上传过程，更新进度条
            for (i in 1..100) {
                progress.value = i / 100f
                delay(50) // 模拟上传延迟
            }
            isUploading = false
            uploadSuccess = true // 假设上传成功
        }
    }
    // 显示上传进度或结果
    if (isUploading) {
        // 上传中，显示进度条
        LinearProgressIndicator(
            progress = {
                progress.value // 设置进度
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    } else {
        // 上传完成，显示提示信息
        uploadSuccess?.let {
            if (it) {
                // 上传成功
                Toast.makeText(
                    context, "Upload Successful", Toast.LENGTH_SHORT
                ).show()
            } else {
                // 上传失败
                Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

}


