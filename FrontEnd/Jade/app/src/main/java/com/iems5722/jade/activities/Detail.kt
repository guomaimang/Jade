package com.iems5722.jade.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.iems5722.jade.R
import com.iems5722.jade.ui.theme.JadeTheme
import com.iems5722.jade.utils.RetrofitInstance
import com.iems5722.jade.utils.UserPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: 高级信息获取展示, 具体如何展示, 类已经定义好了, 明天商讨
class Detail : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 从 Intent 获取传递的数据
        val postId = intent.getIntExtra("id", 0)
        val postTitle = intent.getStringExtra("postTitle")
        val postContent = intent.getStringExtra("postContent")

        val postImage = intent.getStringExtra("postImage")
        val highResolutionPostImage = postImage?.replace("thumbnail", "picture")
        val pictures = mutableListOf<String>()
        pictures.add(highResolutionPostImage.toString())

        enableEdgeToEdge()
        setContent {
            JadeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    DetailScreen(postId, postTitle, postContent, pictures)
                }
            }
        }
    }
}

data class ExtraInfo(
    val exifSize: String,
    val exifTime: String,
    val exifLatitude: String,
    val exifLongitude: String,
    val exifLocation: String,
    val exifDevice: String
)

@Composable
fun DetailScreen(
    postId: Int?,
    postTitle: String?,
    postContent: String?,
    pictures: MutableList<String>,
) {
    // TODO: Get Post User Location
    var postLocation by remember { mutableStateOf("") }
    postLocation = ""

    val context = LocalContext.current

    val jwt = UserPrefs.getJwt(context).toString()

    val testExifSize = ""
    val testExifTime = ""
    val testExifLatitude = ""
    val testExifLongitude = ""
    val testExifLocation = ""
    val testExifDevice = ""

    var picInfo by remember {
        mutableStateOf(
            ExtraInfo(
                testExifSize,
                testExifTime,
                testExifLatitude,
                testExifLongitude,
                testExifLocation,
                testExifDevice
            )
        )
    }

    val avatar = UserPrefs.getAvatar(context)
    val nickname = UserPrefs.getNickname(context)

    var bgHeight = ContentScale.FillHeight
    var headerHeight by remember { mutableIntStateOf(0) }
    var bottomHeight by remember { mutableIntStateOf(0) }

    val pictureApiService = RetrofitInstance(jwt).pictureApiService()
    LaunchedEffect(true) {
        try {
            // 异步请求图片数据
            val response = withContext(Dispatchers.IO) {
                postId?.let { pictureApiService.getPictureInfo(it) }
            }

            if (response != null) {
                if (response.code == 0 && response.data != null) {
                    val pictureInfoData = response.data

                    val extraInfo = ExtraInfo(
                        exifSize = pictureInfoData.exifSize,
                        exifTime = pictureInfoData.exifTime,
                        exifLatitude = pictureInfoData.exifLatitude,
                        exifLongitude = pictureInfoData.exifLongitude,
                        exifLocation = pictureInfoData.exifLocation,
                        exifDevice = pictureInfoData.exifDevice,
                    )

                    picInfo = extraInfo
                    postLocation = pictureInfoData.location
                } else {
                    // 处理图片数据为空的情况
                    throw Exception("Failed to fetch picture data or data is empty")
                }
            }

        } catch (e: Exception) {
            // 捕获异常并在 UI 上显示错误信息
            Log.e("TopicScreen", "Error: ${e.message}")
        }
    }



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
                .zIndex(1f)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    // TODO: jump to ?
                    onClick = {
                        val intent = Intent(context, Topic::class.java)
                        context.startActivity(intent)
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back), contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

                Row {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(avatar).httpHeaders(
                            NetworkHeaders.Builder().add("jwt", jwt).build()
                        ).crossfade(true).build(),
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
                        painter = painterResource(id = R.drawable.map), contentDescription = "Map"
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
                .zIndex(1f)) {
        }

        Box(
            // Lazy Column
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .align(Alignment.TopCenter)
                .background(Color.Transparent)
        ) {
            LazyColumn {
                item { Spacer(modifier = Modifier.height(headerHeight.dp)) }
                item { banner(pictures) }
                item {
                    if (postTitle != null) {
                        if (postContent != null) {
                            DetailPostShow(postTitle, postContent, postLocation)
                        }
                    }
                }
                item { AdvancedInfo(picInfo) }
                item { Spacer(modifier = Modifier.height(bottomHeight.dp)) }
            }
        }
    }
}

@Composable
fun AdvancedInfo(extraInfo: ExtraInfo) {
    var needToShow by remember { mutableStateOf(false) }
    Text(
        text = stringResource(R.string.AdvanceInformation),
        style = TextStyle(color = Color.Gray, fontSize = 20.sp),
        modifier = Modifier
            .clickable {
                needToShow = true
            }
            .padding(16.dp, 0.dp)
    )

    if (needToShow) {
        if (extraInfo.exifSize != "") {
            Text(
                text = "Size: ${extraInfo.exifSize}",
                style = TextStyle(color = Color.Gray, fontSize = 20.sp),
                modifier = Modifier.padding(16.dp, 0.dp)
            )
        }
        if (extraInfo.exifTime != "") {
            Text(
                text = "Time: ${extraInfo.exifTime}",
                style = TextStyle(color = Color.Gray, fontSize = 20.sp),
                modifier = Modifier.padding(16.dp, 0.dp)
            )
        }
        if (extraInfo.exifLocation != "") {
            Text(
                text = "Location: ${extraInfo.exifLocation}",
                style = TextStyle(color = Color.Gray, fontSize = 20.sp),
                modifier = Modifier.padding(16.dp, 0.dp)
            )
        }
        if (extraInfo.exifDevice != "") {
            Text(
                text = "Device: ${extraInfo.exifDevice}",
                style = TextStyle(color = Color.Gray, fontSize = 20.sp),
                modifier = Modifier.padding(16.dp, 0.dp)
            )
        }
        Text(text = stringResource(R.string.HideInformation),
            style = TextStyle(color = colorResource(R.color.microsoftBlue), fontSize = 20.sp),
            modifier = Modifier
                .clickable {
                    needToShow = false
                }
                .padding(16.dp, 0.dp)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun PostDivider() {
    Text(
        text = stringResource(R.string.CommitArea),
        style = TextStyle(color = Color.Black, fontSize = 16.sp)
    )
    Spacer(modifier = Modifier.height(4.dp))
    HorizontalDivider()
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun DetailPostShow(postTitle: String, postContent: String, postLocation: String) {
    Text(
        text = postTitle,
        style = TextStyle(color = Color.Black, fontSize = 24.sp),
        modifier = Modifier.padding(16.dp, 0.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = postContent,
        style = TextStyle(color = Color.Black, fontSize = 20.sp),
        modifier = Modifier.padding(16.dp, 0.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))

    Row {
        IconButton(
            onClick = {

            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.pin),
                contentDescription = "Sign Out",
                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        if (postLocation == "") {
            Text(
                text = "Unknown",
                style = TextStyle(color = Color.Black, fontSize = 16.sp),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        } else {
            Text(
                text = postLocation, style = TextStyle(color = Color.Black, fontSize = 16.sp),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun banner(picList: List<String>) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { picList.size })
    Column {
        Box {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 32.dp),
                pageSpacing = 16.dp
            ) { page ->
                // Our page content
                AsyncImage(
                    model = ImageRequest.Builder(context).data(picList[page]).build(),
                    placeholder = painterResource(R.drawable.placeholder),
                    contentDescription = "user_img",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = {
                        })
                )
            }

            DotIndicators(
                pageCount = picList.size,
                pagerState = pagerState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DotIndicators(
    pageCount: Int, pagerState: PagerState, modifier: Modifier
) {
    Row(modifier = modifier) {
        repeat(pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.Black else Color.Gray
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}



