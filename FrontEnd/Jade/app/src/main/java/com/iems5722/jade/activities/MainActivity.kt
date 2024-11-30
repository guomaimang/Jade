package com.iems5722.jade.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.iems5722.jade.R
import com.iems5722.jade.ui.theme.JadeTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JadeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Login()
                }
            }
        }
    }
}

@Composable
fun Login() {
    // TODO: User image, logic, appearance
    val avatar = "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411252351822.png"
    val nickname = "nickname"

    var text1 by remember { mutableStateOf(TextFieldValue()) }
    var text2 by remember { mutableStateOf(TextFieldValue()) }

    var openSSOWebView by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
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
                    .size(128.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = nickname,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = text1,
                onValueChange = { newText -> text1 = newText },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.extraLarge,
                label = { Text("Enter your uid") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = text2,
                onValueChange = { newText -> text2 = newText },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                shape = MaterialTheme.shapes.extraLarge,
                label = { Text("Enter your password") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
//                    openSSOWebView = true

                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Login/Register")
            }
            Spacer(modifier = Modifier.height(48.dp))
        }

        // 在这里显示 WebView
        if (openSSOWebView) {
            SSOWebView()
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SSOWebView() {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val webView = remember { WebView(context) }
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://login.microsoftonline.com/common/oauth2/v2.0/authorize?client_id=45792ac5-5f4c-49a7-ba2d-1845333171a1&response_type=code&redirect_uri=https://jade.dev.hirsun.tech/oauth2.html&response_mode=query&scope=openid+profile+email&state=12345")

        AndroidView(
            factory = { webView },
            modifier = Modifier.fillMaxSize()
        )

        Button(
            onClick = { /* 关闭WebView，可能需要修改状态来控制WebView的显示与隐藏 */ },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text("Close WebView")
        }
    }
}
