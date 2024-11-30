package com.iems5722.jade.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.iems5722.jade.R
import com.iems5722.jade.ui.theme.JadeTheme

class Detail : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JadeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    DetailScreen()
                }
            }
        }
    }
}


@Composable
@Preview
fun DetailScreen() {
    // TODO: Get Pictures

    val testPic = "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411260316528.png"
    val picList = listOf(
        testPic,
        testPic,
        testPic,
        testPic,
        testPic,
    )

    // TODO: Get Post Title and content

    val postTitle = "Test"
    val postContent = "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest"

    // TODO: Get extra information

    // TODO: This is the post's not current user's
    val avatar = "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411222320597.png"
    val nickname = "nickname"

    var context = LocalContext.current

    var bgHeight = ContentScale.FillHeight
    var headerHeight by remember { mutableIntStateOf(0) }
    var bottomHeight by remember { mutableIntStateOf(0) }

    Box(
        // Background layer
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 48.dp)
    ) {
        // TODO: If bg is needed
//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(backgroundImgUrl)
//                .crossfade(true)
//                .build(),
//            contentDescription = "bg_img",
//            contentScale = bgHeight,
//            modifier = Modifier.fillMaxSize()
//        )
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
                IconButton(
                    // TODO: jump to ?
                    onClick = {
                        val intent = Intent(context, Topic::class.java)
                        context.startActivity(intent)
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

                Row(
//                    // If need to access target account info
//                    modifier = Modifier.clickable(
//                        onClick = {
//                            // TODO: What to pass for setting?
//
//                            val intent = Intent(context, Setting::class.java)
//                            context.startActivity(intent)
//                        }
//                    )
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

                    Text(
                        text = nickname,
                        style = TextStyle(fontSize = 24.sp),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    // TODO: Brings what?
                    onClick = {

                        val intent = Intent(context, Album::class.java)
                        context.startActivity(intent)
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
            // TODO: Bottom : commitment input like chatroom

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
                item { banner(picList) }
                item { DetailPostShow(postTitle, postContent) }
                item { AdvancedInfo() }
                item { PostDivider() }

                item {
                    // TODO: Copy your chatroom
                }

                item { Spacer(modifier = Modifier.height(bottomHeight.dp)) }
            }


        }
    }
}

@Composable
fun AdvancedInfo() {
    var needToShow by remember { mutableStateOf(false) }
    Text(
        text = stringResource(R.string.AdvanceInformation),
        style = TextStyle(color = Color.Gray, fontSize = 16.sp),
        modifier = Modifier.clickable {
            needToShow = true
        }
    )

    if (needToShow) {
        Text("1234")
        Text(
            text = stringResource(R.string.HideInformation),
            style = TextStyle(color = colorResource(R.color.microsoftBlue), fontSize = 16.sp),
            modifier = Modifier.clickable {
                needToShow = false
            }
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
fun DetailPostShow(postTitle: String, postContent: String) {
    Text(
        text = postTitle,
        style = TextStyle(color = Color.Black, fontSize = 24.sp)
    )

    Text(
        text = postContent,
        style = TextStyle(color = Color.Black, fontSize = 16.sp)
    )

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
                    model = ImageRequest.Builder(context)
                        .data(picList[page])
                        .build(),
                    placeholder = painterResource(R.drawable.placeholder),
                    contentDescription = "user_img",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            onClick = {
                                // TODO: If click do what?

                            }
                        )
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
    pageCount: Int,
    pagerState: PagerState,
    modifier: Modifier
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