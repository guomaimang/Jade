package com.iems5722.jade.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.iems5722.jade.ui.theme.JadeTheme
import com.iems5722.jade.utils.RetrofitInstance
import com.iems5722.jade.utils.UserPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

@Suppress("UNREACHABLE_CODE")
class ChatActivities : ComponentActivity() {

    private val messages = mutableStateListOf<Message>()

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val name = intent.getStringExtra("name")
        val topicId = intent.getStringExtra("id")

        setContent {
            JadeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
//                    ChatScreen(
//                        messages = messages,
//                        chatroomName = name.toString(),
//                        onSendMessage = ::sendMessage,
//                        onRefreshMessages = {
//                            if (topicId != null) {
//                                fetchMessages(topicId)
//                            }
//                        },
//                        onBackPressed = TODO()
//                    )
                }
            }
        }
    }

    data class Message(
        val nickname: String,
        val content: String,
        val type: Int,
        val createTime: Date,
    )

    @SuppressLint("CoroutineCreationDuringComposition")
    private fun fetchMessages(topicId: String) {
        val jwt = UserPrefs.getJwt(this).toString()
        val messageApiService = RetrofitInstance(jwt).messageApiService()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = withContext(Dispatchers.IO) {
                    messageApiService.getMessagesByTopicId(
                        topicId = topicId.toInt(),
                    )
                }

                if (response.code == 0 && response.data != null) {
                    val messagesData = response.data
                    val totalMessages = messagesData.total
                    val messageList = mutableListOf<Message>()

                    println("Total messages: $totalMessages")

                    // 构造 Post 对象列表并更新 postList
                    messagesData.rows.map { message ->
                        messageList.add(
                            Message(
                                nickname = message.nickname,
                                content = message.content,
                                type = message.type,
                                createTime = message.createTime,
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
                            this@ChatActivities,
                            "Failed to load messages.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ChatActivities,
                        "Error loading messages: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}


//    @SuppressLint("CoroutineCreationDuringComposition")
//    private fun sendMessage() {
//        val jwt = UserPrefs.getJwt(this).toString()
//        val messageApiService = RetrofitInstance(jwt).messageApiService()
//        lifecycleScope.launch(Dispatchers.IO) {
//            try {
//                val response = withContext(Dispatchers.IO) {
//                    messageApiService.postMessage(
//
//                    )
//                }
//
//                if (response.code == 0 && response.data != null) {
//                    val messagesData = response.data
//                    val totalMessages = messagesData.total
//                    val messageList = mutableListOf<Message>()
//
//                    println("Total messages: $totalMessages")
//
//                    // 构造 Post 对象列表并更新 postList
//                    messagesData.rows.map { message ->
//                        messageList.add(
//                            Message(
//                                nickname = message.nickname,
//                                content = message.content,
//                                type = message.type,
//                                createTime = message.createTime,
//                            )
//                        )
//                    }
//
//                    withContext(Dispatchers.Main) {
//                        messages.clear()
//                        messages.addAll(messageList)
//                    }
//                } else {
//                    withContext(Dispatchers.Main) {
//                        Toast.makeText(
//                            this@ChatActivities,
//                            "Failed to load messages.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(
//                        this@ChatActivities,
//                        "Error loading messages: ${e.localizedMessage}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }
//    }
//
//    @Composable
//    fun ChatScreen(
//        messages: List<Message>,
//        chatroomName: String,
//        onSendMessage: (String) -> Unit,
//        onRefreshMessages: () -> Unit,
//        onBackPressed: () -> Unit
//    ) {
//        var messageText by remember { mutableStateOf(TextFieldValue()) }
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(8.dp),
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                IconButton(onClick = onBackPressed) {
//                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                }
//                Spacer(Modifier.weight(1f))
//                Text(
//                    text = chatroomName,
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier.padding(start = 8.dp),
//                    color = Color.Black
//                )
//                Spacer(Modifier.weight(1f))
//                IconButton(onClick = onRefreshMessages) {
//                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.Blue)
//                }
//            }
//
//            LazyColumn(
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(8.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                items(messages) { message ->
//                    MessageCard(message)
//                }
//            }
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                TextField(
//                    value = messageText,
//                    onValueChange = { messageText = it },
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(8.dp),
//                    placeholder = { Text("Enter your message...") },
//                    shape = MaterialTheme.shapes.medium,
//                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
//                    keyboardActions = KeyboardActions(onSend = {
//                        if (messageText.text.isNotEmpty()) {
//                            onSendMessage(messageText.text)
//                            messageText = TextFieldValue()
//                        }
//                    })
//                )
//                IconButton(onClick = {
//                    if (messageText.text.isNotEmpty()) {
//                        onSendMessage(messageText.text)
//                        messageText = TextFieldValue()
//                    }
//                }) {
//                    Icon(
//                        Icons.AutoMirrored.Filled.Send,
//                        contentDescription = "Send Message",
//                        tint = Color.Blue
//                    )
//                }
//            }
//        }
//    }
//
//    @Composable
//    fun MessageCard(message: Message) {
//        val alignment = if (message.isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
//        Box(
//            modifier = Modifier.fillMaxWidth(),
//            contentAlignment = alignment
//        ) {
//            Card(
//                shape = RoundedCornerShape(16.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = if (message.isCurrentUser) Color(0xFFD1FFDA) else Color(
//                        0xFFE0E0E0
//                    )
//                ),
//                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(12.dp)
//                ) {
//                    Text(
//                        text = message.name,
//                        style = MaterialTheme.typography.labelSmall,
//                        color = if (message.isCurrentUser) Color(0xFF00796B) else Color(0xFF5D4037)
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = message.message,
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = message.time,
//                        style = MaterialTheme.typography.labelSmall,
//                        color = Color.Gray
//                    )
//                }
//            }
//        }
//    }
//
//}

//
//@Composable
//fun ChatActivitiesScreen(nickname: String?, avatar: String?) {
//
//    val context = LocalContext.current
//
//    // TODO: Get Messages
//    val testMessageId = "1"
//    val testSenderId = "12345"
//    val testSenderAvatar =
//        "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411222320597.png"
//    val testSenderNickname = "TestSender"
//    val testMessageString = "Hello"
//    val testMessageTime = "Today 13:14"
//
//    var messageList by remember { mutableStateOf(listOf<Message>()) }
//    messageList = listOf(
//        Message(
//            testMessageId,
//            testSenderId,
//            testSenderAvatar,
//            testSenderNickname,
//            testMessageString,
//            testMessageTime
//        ),
//        Message(
//            testMessageId,
//            testSenderId,
//            testSenderAvatar,
//            testSenderNickname,
//            testMessageString,
//            testMessageTime
//        ),
//        Message(
//            testMessageId,
//            testSenderId,
//            testSenderAvatar,
//            testSenderNickname,
//            testMessageString,
//            testMessageTime
//        ),
//        Message(
//            testMessageId,
//            testSenderId,
//            testSenderAvatar,
//            testSenderNickname,
//            testMessageString,
//            testMessageTime
//        ),
//    )
//
//    var bgHeight = ContentScale.FillHeight
//    var headerHeight by remember { mutableIntStateOf(0) }
//    var bottomHeight by remember { mutableIntStateOf(0) }
//    Box(
//        // Background layer
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(vertical = 48.dp)
//    ) {
//        // TODO: If bg is needed
////        AsyncImage(
////            model = ImageRequest.Builder(LocalContext.current)
////                .data(backgroundImgUrl)
////                .crossfade(true)
////                .build(),
////            contentDescription = "bg_img",
////            contentScale = bgHeight,
////            modifier = Modifier.fillMaxSize()
////        )
//        Box(
//            // Header
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.TopCenter)
//                .background(Color(android.graphics.Color.parseColor("#F8FFFFFF")))
//                .onGloballyPositioned { coordinates ->
//                    headerHeight = coordinates.size.height / 2
//                }
//                .zIndex(1f)
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Spacer(modifier = Modifier.width(8.dp))
//                IconButton(
//                    onClick = {
//                        val intent = Intent(context, ChatRooms::class.java)
//                        context.startActivity(intent)
//                    },
//                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.back),
//                        contentDescription = "Back"
//                    )
//                }
//                Spacer(modifier = Modifier.width(8.dp))
//
//                Row {
//                    AsyncImage(
//                        model = ImageRequest.Builder(LocalContext.current)
//                            .data(avatar)
//                            .crossfade(true)
//                            .build(),
//                        placeholder = painterResource(R.drawable.placeholder),
//                        contentDescription = "user_img",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .clip(CircleShape)
//                            .size(48.dp)
//                            .align(Alignment.CenterVertically)
//                    )
//                    Spacer(modifier = Modifier.width(16.dp))
//
//                    if (nickname != null) {
//                        Text(
//                            text = nickname,
//                            style = TextStyle(fontSize = 24.sp),
//                            modifier = Modifier.align(Alignment.CenterVertically)
//                        )
//                    }
//                }
//                Spacer(modifier = Modifier.weight(1f))
//            }
//        }
//
//        Box(
//            // Bottom
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.BottomCenter)
//                .background(Color(android.graphics.Color.parseColor("#F8FFFFFF")))
//                .onGloballyPositioned { coordinates ->
//                    bottomHeight = coordinates.size.height / 2
//                }
//                .zIndex(1f)
//        ) {
//            // TODO: Bottom
//            Column {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    // TODO: Input?
//                }
//            }
//        }
//
//        Box(
//            // Lazy Column
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 16.dp)
//                .align(Alignment.TopCenter)
//                .background(Color.Transparent)
//        ) {
//            LazyColumn {
//                item {
//                    // Leave place for header
//                    Spacer(modifier = Modifier.height(headerHeight.dp))
//                }
//
//                itemsIndexed(messageList) { index, message ->
//                    Spacer(modifier = Modifier.height(8.dp))
//                    SingleMessageShow(message)
//                    Spacer(modifier = Modifier.height(8.dp))
//                    if (index == messageList.size - 1) {
//                        Spacer(modifier = Modifier.height(bottomHeight.dp))
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun SingleMessageShow(message: Message) {
//    // TODO: your chatroom
//}

