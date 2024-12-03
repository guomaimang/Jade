package com.iems5722.jade.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.iems5722.jade.R
import com.iems5722.jade.ui.theme.JadeTheme

@Suppress("DEPRECATION")
class PostEdit : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 从 Intent 中获取选中的图片 URI 列表
        val selectedImages = intent.getParcelableArrayListExtra<Uri>("selected_images")

        // 检查是否为 null
        if (selectedImages.isNullOrEmpty()) {
            // 如果为空，显示错误日志并结束 Activity
            Log.e("PostEditActivity", "No images provided for editing.")
            finish()
            return
        }

        setContent {
            JadeTheme {
                Scaffold {
                    PostEditScreen(selectedImages)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostEditScreen(selectedImages: List<Uri>) {
    var context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val tags = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 顶部导航栏
        TopAppBar(
            title = { Text("Edit Post") },
            navigationIcon = {
                IconButton(onClick = {
                    val activity = context as? Activity
                    activity?.finish()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                TextButton(onClick = {
//                    CoroutineScope(Dispatchers.Main)
//                        .launch {
//                            selected = topic.id
//                            UserPrefs.setSelectedTopic(
//                                context,
//                                selected
//                            )
//                            // TODO: Selected tag changed, re-get the postList
//                            try {
//                                // 异步请求图片数据
//                                val response =
//                                    withContext(Dispatchers.IO) {
//                                        pictureApiService.getPictures(
//                                            topicId = selected,
//                                            pageNum = 1,
//                                            pageSize = 20
//                                        )
//                                    }
//
//                                if (response.code == 0 && response.data != null) {
//                                    val pictureData = response.data
//                                    val totalPictures = pictureData?.total
//
//                                    println("Total pictures: $totalPictures")
//
//                                    // 构造 Post 对象列表并更新 postList
//                                    val posts = pictureData.rows?.map { image ->
//                                        // 使用异步请求获取用户信息
//                                        val userInfo =
//                                            _root_ide_package_.kotlinx.coroutines.withContext(
//                                                _root_ide_package_.kotlinx.coroutines.Dispatchers.IO
//                                            ) {
//                                                userApiService.getUserInfo(image.userId)
//                                            }
//
//                                        // 防止 userInfo 为 null
//                                        val nickname =
//                                            userInfo?.data?.nickname
//                                                ?: "Unknown User"
//
//                                        Post(
//                                            image = "https://jade.dev.hirsun.tech/picture/get_file?file_name=${image.fileName}&user_id=${image.userId}&resolution=thumbnail",
//                                            title = image.title ?: "No Title",
//                                            content = image.description
//                                                ?: "No Description",
//                                            userAvatar = _root_ide_package_.com.iems5722.jade.utils.ImageLinkGenerator.getUserImage(
//                                                image.userId
//                                            ),
//                                            userNickname = nickname,
//                                            time = image.createTime?.toString()
//                                                ?: "Unknown Time"
//                                        )
//                                    } ?: emptyList()
//
//                                    postList = posts
//                                } else {
//                                    // 处理图片数据为空的情况
//                                    throw Exception("Failed to fetch picture data or data is empty")
//                                }
//
//                            } catch (e: Exception) {
//                                // 捕获异常并在 UI 上显示错误信息
//                                Log.e("TopicScreen", "Error: ${e.message}")
//                            }
//                        }
                }) {
                    Text("Publish", color = MaterialTheme.colorScheme.primary)
                }
            }
        )

        // 图片展示区域
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(selectedImages) { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { /* Image click action */ },
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 标题输入框
        BasicTextField(
            value = title,
            onValueChange = { title = it },
            textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            decorationBox = { innerTextField ->
                if (title.isEmpty()) {
                    Text("Add Title", color = Color.Gray)
                }
                innerTextField()
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 正文输入框
        BasicTextField(
            value = content,
            onValueChange = { content = it },
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tags.forEach { tag ->
                Box(
                    modifier = Modifier
                        .background(Color.LightGray, RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("#$tag", color = Color.Black)
                }
            }
            TextButton(onClick = { /* Add Tag logic */ }) {
                Text("Add Tag")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 位置信息等其他选项
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { /* Location action */ },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = "Add Location", color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
