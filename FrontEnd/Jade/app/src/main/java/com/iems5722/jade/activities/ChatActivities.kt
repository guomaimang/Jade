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
import androidx.compose.material3.TextFieldDefaults
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
import com.iems5722.jade.utils.ImageLinkGenerator
import com.iems5722.jade.utils.RetrofitInstance
import com.iems5722.jade.utils.UserPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.util.Calendar
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
    val currentUserAvatar = UserPrefs.getAvatar(context)

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
                    val image = ImageLinkGenerator.getUserImage(message.userId)
                    messageList.add(
                        Message(
                            nickname = message.nickname,
                            content = message.content,
                            type = message.type,
                            userId = message.userId,
                            createTime = formatTimestampToDiscordStyle(message.createTime.toString()),
                            userAvatar = image
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
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        shape = MaterialTheme.shapes.extraLarge,
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
                                                    userId = currentUserId?.toInt() ?: 0,
                                                    userAvatar = currentUserAvatar.toString()
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
    val userAvatar: String
)

@Composable
fun MessageCard(message: Message) {
    val context = LocalContext.current
    val currentUserId = UserPrefs.getUserId(context)
    val isSender = message.userId == (currentUserId?.toInt() ?: false)
    val alignment = if (isSender) {
        Alignment.CenterEnd
    } else Alignment.CenterStart
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        if (isSender) {
            Row {
                SingleMessageCard(message, isSender)
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(message.userAvatar)
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
            }
        } else {
            Row {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(message.userAvatar)
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
                SingleMessageCard(message, isSender)
            }
        }
    }
}

@Composable
fun SingleMessageCard(message: Message, isSender: Boolean) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSender) Color(
                0xFFD1FFDA
            ) else Color(
                0xFFE0E0E0
            )
        ),
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row {
                Text(
                    text = message.nickname.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSender) Color(0xFF00796B) else Color(0xFF5D4037),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = message.createTime.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@SuppressLint("NewApi")
fun formatTimestampToDiscordStyle(timestamp: String): String {
    val currentDate = LocalDate.now()
    val currentYear = Year.now().value
    val currentDayOfYear = currentDate.dayOfYear

    // 使用合适的格式来解析收到的时间字符串
    val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
    val date = inputFormat.parse(timestamp)

    val outputString: String

    if (date != null) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val day = calendar.get(Calendar.DAY_OF_YEAR)
        val isLeapYear = Year.of(year).isLeap
        val daysInYear = if (isLeapYear) 366 else 365

        if (year == currentYear) {
            if (currentDayOfYear == day) {
                // 设置输出格式为 Discord 样式的时间格式（h:mm a）
                val outputFormat = SimpleDateFormat("h:mm a", Locale.US)
                outputString = outputFormat.format(date)
            } else if (currentDayOfYear - day == 1) {
                // 设置输出格式为 Discord 样式的时间格式（h:mm a）
                val outputFormat = SimpleDateFormat("h:mm a", Locale.US)
                outputString = "Yesterday " + outputFormat.format(date)
            } else {
                // 设置输出格式为 Discord 样式的时间格式（MM/dd h:mm a）
                val outputFormat = SimpleDateFormat("MM/dd h:mm a", Locale.US)
                outputString = outputFormat.format(date)
            }
        } else {
            if (daysInYear == day && currentDayOfYear == 1 && currentYear - year == 1) {
                // 设置输出格式为 Discord 样式的时间格式（h:mm a）
                val outputFormat = SimpleDateFormat("h:mm a", Locale.US)
                outputString = outputFormat.format(date)
            } else {
                // 设置输出格式为 Discord 样式的时间格式（MM/dd/yyyy h:mm a）
                val outputFormat = SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.US)
                // 格式化成指定的字符串
                outputString = outputFormat.format(date)
            }
        }
    } else {
        outputString = "Didn't receive Date!"
    }

    return outputString
}

