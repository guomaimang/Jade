package com.iems5722.jade.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.iems5722.jade.R
import com.iems5722.jade.ui.theme.JadeTheme
import com.iems5722.jade.utils.ImageLinkGenerator
import com.iems5722.jade.utils.UserPrefs
import org.json.JSONObject

@Suppress("NAME_SHADOWING")
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UserPrefs.initializeUserData(this)

        val intent = intent
        if (Intent.ACTION_VIEW == intent.action) {
            val data = intent.data
            if (data != null && "myapp" == data.scheme) {
                val text = data.getQueryParameter("text")
                // Android 显示
                Log.i("MainActivity", text.toString())

                if (text != null) {
                    try {
                        // 解析JSON字符串
                        val jsonResponse = JSONObject(text)
                        val code = jsonResponse.getInt("code")
                        if (code == 0) {
                            // 登录成功，获取 JWT 和用户信息
                            val jwt = jsonResponse.getJSONObject("data").getString("jwt")
                            val user = jsonResponse.getJSONObject("data").getJSONObject("user")
                            val userId = user.getString("id")
                            val nickname = user.getString("nickname")
                            val email = user.getString("email")
                            val avatar = ImageLinkGenerator.getUserImage(userId.toInt())

                            // 存储用户数据
                            UserPrefs.saveUserData(
                                this, userId, email, nickname, avatar, jwt,
                            )

                            // 提示用户登录成功
                            Toast.makeText(this, "登录成功: $nickname", Toast.LENGTH_SHORT).show()

                            // 跳转到APP主界面
                            val intent = Intent(this, Topic::class.java)
                            this.startActivity(intent)

                        } else {
                            // 登录失败，提示错误信息
                            val message = jsonResponse.getString("message")
                            Toast.makeText(this, "登录失败: $message", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "解析返回数据失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        enableEdgeToEdge()
        setContent {
            JadeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Login()
                }
            }
        }
    }


    @Composable
    fun Login() {
        var avatar by remember { mutableStateOf("") }
        avatar = UserPrefs.getAvatar(this).toString()
        val nickname = UserPrefs.getNickname(this)
        var agreed by remember { mutableStateOf(false) }

        val context = LocalContext.current

//        var text1 by remember { mutableStateOf(TextFieldValue()) }
//        var text2 by remember { mutableStateOf(TextFieldValue()) }

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
                if (nickname != null) {
                    Text(
                        text = nickname,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
//                TextField(
//                    value = text1,
//                    onValueChange = { newText -> text1 = newText },
//                    modifier = Modifier.align(Alignment.CenterHorizontally),
//                    colors = TextFieldDefaults.colors(
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent,
//                        disabledIndicatorColor = Color.Transparent
//                    ),
//                    shape = MaterialTheme.shapes.extraLarge,
//                    label = { Text("Enter your email") }
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//            TextField(
//                value = text2,
//                onValueChange = { newText -> text2 = newText },
//                colors = TextFieldDefaults.colors(
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
//                    disabledIndicatorColor = Color.Transparent
//                ),
//                modifier = Modifier.align(Alignment.CenterHorizontally),
//                shape = MaterialTheme.shapes.extraLarge,
//                label = { Text("Enter your password") }
//            )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (agreed) {
                            val url =
                                "https://login.microsoftonline.com/common/oauth2/v2.0/authorize?client_id=45792ac5-5f4c-49a7-ba2d-1845333171a1&response_type=code&redirect_uri=https://jade.dev.hirsun.tech/oauth2.html&response_mode=query&scope=openid+profile+email&state=12345"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        }else {
                            Toast.makeText(context,R.string.NotAgreeTips,Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Login/Register")
                }
                Row (
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Checkbox(
                        checked = agreed,
                        onCheckedChange = { agreed = it },
                        modifier = Modifier
                    )
                    Text(
                        text =  stringResource(R.string.tips),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text =  stringResource(R.string.file),
                        style = TextStyle(color = colorResource(R.color.microsoftBlue)),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable {
                                // TODO: Check User Agreement
                            }
                    )
                }
                Spacer(modifier = Modifier.height(48.dp))

            }

        }
    }
}


