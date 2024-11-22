package com.iems5722.jade

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

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
fun Login(modifier: Modifier = Modifier) {
    // TODO: User image, logic, appearance
    val avatar = "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411222320597.png"
    val nickname = "nickname"

    var context = LocalContext.current
    var text1 by remember { mutableStateOf(TextFieldValue()) }
    var text2 by remember { mutableStateOf(TextFieldValue()) }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
        // TODO: background needed?
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
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
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = text1,
                onValueChange = { newText ->
                    text1 = newText
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                label = { Text("Enter your uid") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = text2,
                onValueChange = { newText ->
                    text2 = newText
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                label = { Text("Enter your password") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    // TODO: Update to Server

                    // TODO: What to deliver?
                    val intent = Intent(context, Topic::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Login/Register")
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JadeTheme {
        Login()
    }
}