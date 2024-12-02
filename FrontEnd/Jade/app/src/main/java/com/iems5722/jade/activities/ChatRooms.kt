package com.iems5722.jade.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.iems5722.jade.R
import com.iems5722.jade.ui.theme.JadeTheme
import com.iems5722.jade.utils.ImageLinkGenerator
import com.iems5722.jade.utils.RetrofitInstance
import com.iems5722.jade.utils.UserPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatRooms : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JadeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    chatRoomsScreen()
                }
            }
        }
    }
}

@Composable
fun chatRoomsScreen() {

    val context = LocalContext.current

    val nickname = UserPrefs.getNickname(context)
    val avatar = UserPrefs.getAvatar(context)

    var chatRoomList by remember { mutableStateOf(listOf<Chatroom>()) }

    val jwt = UserPrefs.getJwt(context).toString()

    val topicApiService = RetrofitInstance(jwt).topicApiService()
    LaunchedEffect(true) {
        try {
            val response = withContext(Dispatchers.IO) {
                topicApiService.getTopics()
            }

            if (response.code == 0 && response.data != null) {
                val topicsTemp = response.data
                val chatRooms = topicsTemp.map { topic ->
                    Chatroom(
                        chatroomId = topic.id.toString(),
                        chatroomAvatar = ImageLinkGenerator.getUserImage(topic.id),
                        chatroomName = topic.tag,
                        latestMessage = "Hello",
                        latestMessageTime = "Today 13:14",
                        unRead = 0
                    )
                }

                withContext(Dispatchers.Main) {
                    chatRoomList = chatRooms
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ChatActivity", "Error loading messages: ${e.localizedMessage}")
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
                Row(
                    modifier = Modifier.clickable(
                        onClick = {
                            // TODO: What to pass for setting?

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
//                    Text(text = stringResource(R.string.app_name))
                    if (nickname != null) {
                        Text(
                            text = nickname,
                            style = TextStyle(fontSize = 24.sp),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))



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
            // TODO: Bottom
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
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
            LazyColumn {
                item {
                    // Leave place for header
                    Spacer(modifier = Modifier.height(headerHeight.dp))
                }

                itemsIndexed(chatRoomList) { index, chatroom ->
                    Spacer(modifier = Modifier.height(8.dp))
                    SingleChatroomShow(chatroom)
                    Spacer(modifier = Modifier.height(8.dp))
                    if (index != chatRoomList.size - 1) {
                        HorizontalDivider()
                    } else {
                        Spacer(modifier = Modifier.height(bottomHeight.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SingleChatroomShow(chatroom: Chatroom) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.clickable(
            onClick = {
                // TODO: What to bring?

                val intent = Intent(context, ChatActivities::class.java)
                intent.putExtra("name", chatroom.chatroomName)
                intent.putExtra("id", chatroom.chatroomId)
                context.startActivity(intent)
            }
        )
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(chatroom.chatroomAvatar)
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
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically),
        ) {
            Text(
                text = chatroom.chatroomName,
                style = TextStyle(fontSize = 20.sp)
            )
            Text(
                text = chatroom.latestMessage,
                style = TextStyle(color = Color.Gray, fontSize = 10.sp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = chatroom.latestMessageTime,
                style = TextStyle(fontSize = 10.sp)
            )
            val flag = (chatroom.unRead == 0)
            if (flag) {
                Text(
                    text = "${chatroom.unRead}",
                    style = TextStyle(color = Color.Gray)
                )
            } else {
                Text(
                    text = "${chatroom.unRead}",
                    style = TextStyle(color = Color.Red)
                )
            }

        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}

data class Chatroom(
    var chatroomId: String,
    var chatroomAvatar: String,
    var chatroomName: String,
    var latestMessage: String,
    var latestMessageTime: String,
    var unRead: Int
)

