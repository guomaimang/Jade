package com.iems5722.jade.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsIntent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.iems5722.jade.R
import com.iems5722.jade.ui.theme.JadeTheme
import com.iems5722.jade.utils.ImageLinkGenerator
import com.iems5722.jade.utils.RetrofitInstance
import com.iems5722.jade.utils.UserPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class Topic : ComponentActivity() {

    companion object {
        private const val FILE_SELECT_CODE = 0
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JadeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    TopicScreen()
                }
            }
        }
    }

    fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, FILE_SELECT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
            val uri = data?.data
            uri?.let {
                val intent = Intent(this, PostEdit::class.java).apply {
                    putExtra("selected_image", it.toString())
                }
                startActivity(intent)
            }
        }
    }
}


data class Post(
    var id: Int,
    var image: String,
    var title: String,
    var content: String,
    var userAvatar: String,
    var userNickname: String,
    val time: String
)

data class Topics(
    val id: Int,
    val tag: String,
)

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun TopicScreen() {

    val context = LocalContext.current

    var topics by remember { mutableStateOf(listOf<Topics>()) }

    val jwt = UserPrefs.getJwt(context).toString()

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
                        id = topic.id,
                        tag = topic.tag
                    )
                }

                withContext(Dispatchers.Main) {
                    topics = topicsList
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Topics", "Error loading topics: ${e.localizedMessage}")
        }
    }

    var selected by remember { mutableIntStateOf(-1) }
    selected = UserPrefs.getSelectedTopic(context)
    var postList by remember { mutableStateOf(listOf<Post>()) }

    val pictureApiService = RetrofitInstance(jwt).pictureApiService()
    val userApiService = RetrofitInstance(jwt).userApiService()

    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        try {
            // 异步请求图片数据
            val response = withContext(Dispatchers.IO) {
                pictureApiService.getPictures(
                    topicId = selected,
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
                    postList = posts
                    isLoading = false
                }
            } else {
                // 处理图片数据为空的情况
                throw Exception("Failed to fetch picture data or data is empty")
            }

        } catch (e: Exception) {
            // 捕获异常并在 UI 上显示错误信息
            Log.e("TopicScreen", "Error: ${e.message}")
            isLoading = false
        }
    }

    // TODO: Get user name and user image
    val nickname = UserPrefs.getNickname(context)
    val avatar = UserPrefs.getAvatar(context)

    var bgHeight = ContentScale.FillHeight
    var headerHeight by remember { mutableIntStateOf(0) }
    var bottomHeight by remember { mutableIntStateOf(0) }

    if (isLoading) {
        Box(
            // Background layer
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 48.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(128.dp)
                    .align(Alignment.Center)
                    .clip(shape = RoundedCornerShape(10.dp)),
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(R.drawable.wait)
                        .build(),
                ),
                contentDescription = null
            )
        }
    } else {
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
                    Row(
                        modifier = Modifier.clickable(
                            onClick = {
                                val intent = Intent(context, Setting::class.java)
                                context.startActivity(intent)
                            }
                        )
                    ) {
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
                                .size(48.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        if (nickname != null) {
                            Text(
                                text = nickname,
                                style = TextStyle(fontSize = 24.sp),
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            openMap(context)
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.map),
                            contentDescription = "Map"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
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
                                (context as Topic).showFileChooser()
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
                                // TODO: What to deliver?
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
                    .padding(horizontal = 16.dp)
                    .align(Alignment.TopCenter)
                    .background(Color.Transparent)
            ) {
                Column {
                    // Leave place for header
                    Spacer(modifier = Modifier.height((headerHeight - 16).dp))

                    // Tag Selection
                    LazyRow {
                        topics.forEachIndexed { _, topic ->
                            item {
                                Column(
                                    modifier = Modifier.clickable(
                                        onClick = {
                                            CoroutineScope(Dispatchers.Main).launch {
                                                selected = topic.id
                                                UserPrefs.setSelectedTopic(
                                                    context,
                                                    selected
                                                )
                                                // TODO: Selected tag changed, re-get the postList
                                                try {
                                                    // 异步请求图片数据
                                                    val response = withContext(Dispatchers.IO) {
                                                        pictureApiService.getPictures(
                                                            topicId = selected,
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
                                                            val userInfo =
                                                                withContext(Dispatchers.IO) {
                                                                    userApiService.getUserInfo(image.userId)
                                                                }

                                                            // 防止 userInfo 为 null
                                                            val nickname =
                                                                userInfo?.data?.nickname
                                                                    ?: "Unknown User"

                                                            Post(
                                                                id = image.id,
                                                                image = "https://jade.dev.hirsun.tech/picture/get_file?file_name=${image.fileName}&user_id=${image.userId}&resolution=thumbnail",
                                                                title = image.title ?: "No Title",
                                                                content = image.description
                                                                    ?: "No Description",
                                                                userAvatar = ImageLinkGenerator.getUserImage(
                                                                    image.userId
                                                                ),
                                                                userNickname = nickname,
                                                                time = image.createTime?.toString()
                                                                    ?: "Unknown Time"
                                                            )
                                                        } ?: emptyList()

                                                        postList = posts
                                                    } else {
                                                        // 处理图片数据为空的情况
                                                        throw Exception("Failed to fetch picture data or data is empty")
                                                    }

                                                } catch (e: Exception) {
                                                    // 捕获异常并在 UI 上显示错误信息
                                                    Log.e("TopicScreen", "Error: ${e.message}")
                                                }
                                            }
                                        }
                                    )
                                ) {
                                    if (selected == topic.id) {
                                        Text(
                                            text = topic.tag,
                                            style = TextStyle(color = Color.Black)
                                        )
                                    } else {
                                        Text(
                                            text = topic.tag,
                                            style = TextStyle(color = Color.Gray)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))

                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    // TODO: LazyColumn

                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        verticalItemSpacing = 8.dp,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 0.dp, bottom = bottomHeight.dp, start = 0.dp, end = 0.dp)
                    ) {
                        postList.forEachIndexed { _, postItem ->
                            item {
                                PostItemShow(postItem, jwt)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostItemShow(postItem: Post, jwt: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .clickable(
                    onClick = {
                        val intent = Intent(context, Detail::class.java)
                        intent.putExtra("postTitle", postItem.title)
                        intent.putExtra("postContent", postItem.content)
                        intent.putExtra("postImage", postItem.image)
                        intent.putExtra("id", postItem.id)
                        intent.putExtra("time", postItem.time)
                        context.startActivity(intent)
                    }
                )
                .padding(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(postItem.image)
                    .httpHeaders(
                        NetworkHeaders.Builder()
                            .add("jwt", jwt)
                            .build()
                    )
                    .build(),
                placeholder = painterResource(R.drawable.placeholder),
                contentDescription = "user_img",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = postItem.title,
                style = TextStyle(color = Color.Black, fontSize = 24.sp)
            )
            Text(
                text = postItem.content,
                style = TextStyle(color = Color.Gray, fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(postItem.userAvatar)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.placeholder),
                    contentDescription = "user_img",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(28.dp)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = postItem.userNickname,
                    style = TextStyle(fontSize = 12.sp),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.weight(1f))
                val timeString = timeToShow(postItem.time)
                Text(
                    text = timeString,
                    style = TextStyle(color = Color.Gray, fontSize = 10.sp),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

fun timeToShow(time: String): String {
    val timeReturn: String

    // 将时间字符串按照空格分割成数组
    val timeParts = time.split(" ")

    // 提取前三个元素并合并成字符串
    timeReturn = timeParts.take(3).joinToString(" ")

    return timeReturn
}


fun openMap(context: Context) {
    // TODO: Complete the Map Here!!!!

    val topicId = UserPrefs.getSelectedTopic(context)

    val url = "https://jade.dev.hirsun.tech/map.html?topicId=$topicId"

    val customTabsIntent = CustomTabsIntent.Builder().apply {
        // 设置自定义的工具栏颜色
        setToolbarColor(Color(0xFF6200EE).toArgb())  // 例如紫色
    }.build()

    // 点击时打开 Custom Tab
    customTabsIntent.launchUrl(context, Uri.parse(url))
}


