package com.iems5722.jade.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.iems5722.jade.R
import com.iems5722.jade.ui.theme.JadeTheme
import com.iems5722.jade.utils.ImageLinkGenerator
import com.iems5722.jade.utils.ImageUploadHelper
import com.iems5722.jade.utils.RetrofitInstance
import com.iems5722.jade.utils.UserPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Setting : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JadeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    SettingScreen()
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
@Preview
fun SettingScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    val jwt = UserPrefs.getJwt(context).toString()

    val pictureApiService = RetrofitInstance(jwt).pictureApiService()
    val userApiService = RetrofitInstance(jwt).userApiService()

    val imageUploadHelper = ImageUploadHelper()
    val photoPickerLauncher = imageUploadHelper.setupMediaPicker(
        context = context,
    )

    val avatar = UserPrefs.getAvatar(context)
    val nickname = UserPrefs.getNickname(context)
    val mail = UserPrefs.getEmail(context)

    var myPostList by remember { mutableStateOf(listOf<Post>()) }

    LaunchedEffect(true) {
        try {
            // 异步请求图片数据
            val response = withContext(Dispatchers.IO) {
                pictureApiService.getPicturesMyself(
                    pageNum = 1,
                    pageSize = 20
                )
            }

            if (response.code == 0 && response.data != null) {
                val pictureData = response.data
                val totalPictures = pictureData?.total

                println("Total pictures: $totalPictures")

                // 构造 Post 对象列表并更新 postList
                val posts = pictureData.rows?.map { image ->
                    // 使用异步请求获取用户信息
                    val userInfo = withContext(Dispatchers.IO) {
                        userApiService.getUserInfo(image.userId)
                    }

                    // 防止 userInfo 为 null
                    val nickname = userInfo?.data?.nickname ?: "Unknown User"

                    Post(
                        id = image.id,
                        image = "https://jade.dev.hirsun.tech/picture/get_file?file_name=${image.fileName}&user_id=${image.userId}&resolution=thumbnail",
                        title = image.title ?: "No Title",
                        content = image.description ?: "No Description",
                        userAvatar = ImageLinkGenerator.getUserImage(image.userId),
                        userNickname = nickname,
                        time = image.createTime?.toString() ?: "Unknown Time"
                    )
                } ?: emptyList()

                withContext(Dispatchers.Main) {
                    myPostList = posts
                }
            } else {
                // 处理图片数据为空的情况
                throw Exception("Failed to fetch picture data or data is empty")
            }

        } catch (e: Exception) {
            // 捕获异常并在 UI 上显示错误信息
            Log.e("TopicScreen", "Error: ${e.message}")
        }
    }

    var bgHeight = ContentScale.FillHeight
    var headerHeight by remember { mutableIntStateOf(0) }
    var bottomHeight by remember { mutableIntStateOf(0) }

    Box(
        // Background layer
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 48.dp)
    ) {
        Box(
            // Header
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(Color(android.graphics.Color.parseColor("#F8FFFFFF")))
                .onGloballyPositioned { coordinates ->
                    headerHeight = coordinates.size.height / 2
                }
                .zIndex(1f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.SettingString))
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Box(
            // Bottom
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color(android.graphics.Color.parseColor("#F8FFFFFF")))
                .onGloballyPositioned { coordinates ->
                    bottomHeight = coordinates.size.height / 2
                }
                .zIndex(1f)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            val intent = Intent(context, Topic::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.topic),
                            contentDescription = "Topics"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageAndVideo
                                )
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.plus),
                            contentDescription = "Upload"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            val intent = Intent(context, ChatRooms::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.chatrooms),
                            contentDescription = "Chatroom"
                        )
                    }
                }
            }
        }

        Box(
            // Lazy Column
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .background(Color.Transparent)
        ) {
            Column {
                // Leave place for header
                Spacer(modifier = Modifier.height(headerHeight.dp))

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(avatar)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.placeholder),
                        contentDescription = "user_img",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(72.dp)
                    )
                    Spacer(modifier = Modifier.width(32.dp))
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        if (nickname != null) {
                            Text(
                                text = nickname,
                                style = TextStyle(fontSize = 20.sp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (mail != null) {
                            Text(
                                text = mail,
                                style = TextStyle(fontSize = 16.sp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = {
                            // 用户退出的时候重置本地用户数据
                            UserPrefs.resetJWT(context)
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.log_out),
                            contentDescription = "Sign Out",
                            tint = Color.Red
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = stringResource(R.string.SettingTitle))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                }

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 0.dp, bottom = bottomHeight.dp, start = 0.dp, end = 0.dp)
                ) {
                    myPostList.forEachIndexed { _, postItem ->
                        item {
                            PostItemShow(postItem, jwt)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(bottomHeight.dp))
            }
        }
    }
}