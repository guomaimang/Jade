@file:Suppress("DEPRECATION")

package com.iems5722.jade.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.iems5722.jade.R
import com.iems5722.jade.ui.theme.JadeTheme

class Album : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            JadeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    AlbumScreen()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
@Preview
fun AlbumScreen() {

    val topicId = 4

    val url = "https://jade.dev.hirsun.tech/map.html?topicId=$topicId"

    var context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // 使用 Custom Tabs 打开 URL
        val customTabsIntent = CustomTabsIntent.Builder().apply {
            // 设置自定义的工具栏颜色
            setToolbarColor(Color(0xFF6200EE).toArgb())  // 例如紫色
        }.build()

        // 点击时打开 Custom Tab
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }

    // 这里可以添加你页面其他内容，比如 header 和底部导航栏
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
                .zIndex(1f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(32.dp))
                Text(text = "Album")  // 替换为你实际的标题
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        // 其他内容
        Box(
            // Bottom
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color(android.graphics.Color.parseColor("#F8FFFFFF")))
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
                            // 跳转到 Topic 页面
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
    }
}

