package com.iems5722.jade

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.iems5722.jade.ui.theme.JadeTheme

class Topic : ComponentActivity() {
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
}

data class Post(
    var image: String,
    var title: String,
    var content: String,
    var userAvatar: String,
    var userNickname: String,
    val time: String
)

// TODO: Define the kind!
enum class Type {
    ChineseNewYear, MidAutumnFestival, ChingMingFestival, DragonBoatFestival, LanternFestival
}

@Composable
fun TopicScreen() {
    val topics = remember { Type.entries.toTypedArray() }
    var selected by remember { mutableStateOf("${Type.entries[0]}") }

    // TODO: Get content by selected tag, selected tag are String in Type

    val testImage = "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411222320597.png"
    val testTitle = "Test Title"
    val testContent = "Test content"
    val testUserAvatar = "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411222320597.png"
    val testUserNickname = "Test"
    val testTime = "Today 13:14"

    var postList by remember { mutableStateOf(listOf<Post>()) }
    postList = listOf(
        Post(testImage, testTitle, testContent, testUserAvatar, testUserNickname, testTime),
        Post(testImage, testTitle, testContent, testUserAvatar, testUserNickname, testTime),
        Post(testImage, testTitle, testContent, testUserAvatar, testUserNickname, testTime),
        Post(testImage, testTitle, testContent, testUserAvatar, testUserNickname, testTime),
        Post(testImage, testTitle, testContent, testUserAvatar, testUserNickname, testTime),
        Post(testImage, testTitle, testContent, testUserAvatar, testUserNickname, testTime),
        Post(testImage, testTitle, testContent, testUserAvatar, testUserNickname, testTime),
        Post(testImage, testTitle, testContent, testUserAvatar, testUserNickname, testTime),
        Post(testImage, testTitle, testContent, testUserAvatar, testUserNickname, testTime),
        Post(testImage, testTitle, testContent, testUserAvatar, testUserNickname, testTime),
        Post(testImage, testTitle, testContent, testUserAvatar, testUserNickname, testTime),
        Post(testImage, testTitle, testContent, testUserAvatar, testUserNickname, testTime)
    )



    val evenList = mutableListOf<Post>()
    val oddList = mutableListOf<Post>()
    postList.forEachIndexed { index, post ->
        if (index % 2 == 0) {
            evenList.add(post)
        } else {
            oddList.add(post)
        }
    }

    val context = LocalContext.current

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
                Text(text = stringResource(R.string.app_name))
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        // TODO: What to deliver?
                        val intent = Intent(context, Setting::class.java)
                        context.startActivity(intent)
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.settings),
                        contentDescription = "Settings"
                    )
                }
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
            // TODO: Bottom
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        // TODO: jump to ?
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
                        // TODO: Upload logic
                        onClick = {

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
                        // TODO: What to bring?
                        onClick = {
                            val intent = Intent(context, Album::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.album),
                            contentDescription = "Album"
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
                Spacer(modifier = Modifier.height(headerHeight.dp))

                // Tag Selection
                LazyRow {
                    items(topics) { topic ->
                        Column (
                            modifier = Modifier.clickable(
                                onClick={
                                    selected = topic.name

                                    // TODO: Selected tag changed, re-get the postList
                                }
                            )
                        ) {
                            if (selected == topic.name) {
                                Text(
                                    text = topic.name,
                                    style = TextStyle(color = Color.Black)
                                )
                            }else{
                                Text(
                                    text = topic.name,
                                    style = TextStyle(color = Color.Gray)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                // TODO: LazyColumn
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        myLazyColumn(oddList, bottomHeight)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        myLazyColumn(evenList, bottomHeight)
                    }
                }


                // Usage of Image(From Web)
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(self_fig)
//                        .crossfade(true)
//                        .build(),
//                    placeholder = painterResource(R.drawable.placeholder),
//                    contentDescription = "user_img",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .clip(CircleShape)
//                        .size(48.dp),
//                )
//                Text(text = "Topic")
                // TODO: LazyColumn List
            }
        }
    }
}

@Composable
fun myLazyColumn(halfList: List<Post>, bottomHeight: Int){
    val context = LocalContext.current
    LazyColumn {
        itemsIndexed(halfList) { index, postItem ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            val intent = Intent(context, Detail::class.java)
                            context.startActivity(intent)
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "$index")
            }
            if (index == halfList.size - 1) {
                Spacer(modifier = Modifier.height(bottomHeight.dp))
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun TopicPreview() {
//    JadeTheme {
//        TopicScreen()
//    }
//}