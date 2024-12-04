package com.iems5722.jade.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.iems5722.jade.R
import com.iems5722.jade.ui.theme.JadeTheme
import com.iems5722.jade.utils.RetrofitInstance
import com.iems5722.jade.utils.UserPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatActivities : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val topicId = intent.getStringExtra("topic_id")
        val topicName = intent.getStringExtra("topic_name")
        val avatar = intent.getStringExtra("avatar")

        setContent {
            JadeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    ChatActivitiesScreen(
                        topicId = topicId?.toInt() ?: 4,
                        chatroomName = topicName,
                        avatar = avatar,
//                        onBackPressed = { onBackPressedDispatcher.onBackPressed() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatActivitiesScreen(chatroomName: String?, avatar: String?, topicId: Int) {

    val context = LocalContext.current

    val jwt = UserPrefs.getJwt(context).toString()
    val currentUserId = UserPrefs.getUserId(context)
    val nickname = UserPrefs.getNickname(context)

    val messages = remember { mutableStateListOf<Message>() }
    var messageText by remember { mutableStateOf(TextFieldValue()) }

    var headerHeight by remember { mutableIntStateOf(0) }
    var bottomHeight by remember { mutableIntStateOf(0) }

    val messageApiService = RetrofitInstance(jwt).messageApiService()

    LaunchedEffect(messages.size) {
        try {
            val response = withContext(Dispatchers.IO) {
                messageApiService.getMessagesByTopicId(
                    topicId = topicId,
                )
            }

            if (response.code == 0 && response.data != null) {
                val messagesData = response.data
                val messageList = mutableListOf<Message>()

                messagesData.map { message ->
                    messageList.add(
                        Message(
                            nickname = message.nickname,
                            content = message.content,
                            type = message.type,
                            userId = message.userId,
                            createTime = formatTimestampToDiscordStyle(message.createTime.toString())
                        )
                    )
                }

                withContext(Dispatchers.Main) {
                    messages.clear()
                    messages.addAll(messageList)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Failed to load messages.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.e("ChatScreen", "Error: ${e.message}")
                Toast.makeText(
                    context,
                    "Error loading messages: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
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
                    onClick = {
                        val intent = Intent(context, ChatRooms::class.java)
                        context.startActivity(intent)
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

                Row {
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

                    if (chatroomName != null) {
                        Text(
                            text = chatroomName,
                            style = TextStyle(fontSize = 24.sp),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
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
            // TODO: Bottom
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        placeholder = { Text("Enter your message...") },
                        shape = MaterialTheme.shapes.medium,
                    )
                    IconButton(onClick = {
                        if (messageText.text.isNotEmpty()) {
                            CoroutineScope(Dispatchers.Main).launch {
                                try {
                                    val response = withContext(Dispatchers.IO) {
                                        messageApiService.postMessage(
                                            com.iems5722.jade.pojo.Message(
                                                id = "",
                                                createTime = null,
                                                userId = currentUserId?.toInt() ?: 0,
                                                type = 1,
                                                content = messageText.text,
                                                nickname = nickname,
                                                topicId = topicId
                                            )
                                        )
                                    }

                                    if (response.code == 0) {
                                        withContext(Dispatchers.Main) {
                                            messages.add(
                                                Message(
                                                    nickname = nickname,
                                                    content = messageText.text,
                                                    type = 1,
                                                    createTime = formatTimestampToDiscordStyle(Date().toString()),
                                                    userId = currentUserId?.toInt() ?: 0
                                                )
                                            )
                                            messageText = TextFieldValue()
                                        }
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(
                                                context,
                                                "Failed to send messages.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "Error sending messages: ${e.localizedMessage}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send Message",
                            tint = Color.Blue
                        )
                    }
                }
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    // TODO: Input?
//                }
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

                itemsIndexed(messages) { index, message ->
                    Spacer(modifier = Modifier.height(8.dp))
                    MessageCard(message)
                    Spacer(modifier = Modifier.height(8.dp))
                    if (index == messages.size - 1) {
                        Spacer(modifier = Modifier.height(bottomHeight.dp))
                    }
                }
            }
        }
    }
}

data class Message(
    val nickname: String?,
    val content: String,
    val type: Int?,
    val createTime: String?,
    val userId: Int,
)

@Composable
fun MessageCard(message: Message) {
    val context = LocalContext.current
    val currentUserId = UserPrefs.getUserId(context)
    val alignment = if (message.userId == (currentUserId?.toInt() ?: 0)) {
        Alignment.CenterEnd
    } else Alignment.CenterStart
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (message.userId == (currentUserId?.toInt() ?: 0)) Color(
                    0xFFD1FFDA
                ) else Color(
                    0xFFE0E0E0
                )
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = message.nickname.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (message.userId == (currentUserId?.toInt()
                            ?: 0)
                    ) Color(0xFF00796B) else Color(0xFF5D4037)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message.createTime.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@SuppressLint("NewApi")
fun formatTimestampToDiscordStyle(timestamp: String): String {
    // 使用合适的格式来解析收到的时间字符串
    val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
    val date = inputFormat.parse(timestamp)

    // 设置输出格式为 Discord 样式的时间格式（MM/dd/yyyy h:mm a）
    val outputFormat = SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.US)

    // 格式化成指定的字符串
    return outputFormat.format(date)
}

