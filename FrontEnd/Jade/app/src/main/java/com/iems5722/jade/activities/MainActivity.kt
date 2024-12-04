package com.iems5722.jade.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.graphics.Shader
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.iems5722.jade.R
import com.iems5722.jade.ui.theme.JadeTheme
import com.iems5722.jade.utils.ImageLinkGenerator
import com.iems5722.jade.utils.RetrofitInstance
import com.iems5722.jade.utils.UserPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                    initializeSelectedTopic()
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

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    AppNameText(stringResource(R.string.app_name), textSize = 56)
                    Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(16.dp))
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
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (agreed) {
                            val url =
                                "https://login.microsoftonline.com/common/oauth2/v2.0/authorize?client_id=45792ac5-5f4c-49a7-ba2d-1845333171a1&response_type=code&redirect_uri=https://jade.dev.hirsun.tech/oauth2.html&response_mode=query&scope=openid+profile+email&state=12345"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        } else {
                            Toast.makeText(context, R.string.NotAgreeTips, Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Login/Register")
                }
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Checkbox(
                        checked = agreed,
                        onCheckedChange = { agreed = it },
                        modifier = Modifier
                    )
                    Text(
                        text = stringResource(R.string.tips),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = stringResource(R.string.file),
                        style = TextStyle(color = colorResource(R.color.microsoftBlue)),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable {
                            }
                    )
                }
                Spacer(modifier = Modifier.height(48.dp))

            }

        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    private fun initializeSelectedTopic() {
        val jwt = UserPrefs.getJwt(this).toString()
        val topicApiService = RetrofitInstance(jwt).topicApiService()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = withContext(Dispatchers.IO) {
                    topicApiService.getTopics()
                }

                if (response.code == 0 && response.data != null) {
                    val topics = response.data
                    // 获取第一个 topic 的 id
                    val firstTopicId = topics.firstOrNull()?.id
                    if (firstTopicId != null) {
                        UserPrefs.setSelectedTopic(this@MainActivity, firstTopicId)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ChatActivity", "Error loading messages: ${e.localizedMessage}")
            }
        }
    }
}


fun px2dp(scale: Float, px: Int): Int {
    return (px / scale + 0.5f).toInt()
}

@Composable
fun AppNameText(
    text: String,
    textSize: Int = 24,
    gradientShader: (Rect) -> Shader = {
        LinearGradientShader(
            from = Offset(0f, 0f),
            to = Offset((it.right - it.left).toFloat(), 0f),
            colors = listOf(
                Color(0xFF7DE0FC),
                Color(0xFF77C6F9),
                Color(0xFFA1B1FF),
                Color(0xFFD9ACFE),
            )
        )
    }
) {
    val density = LocalDensity.current.density
    var width by remember { mutableStateOf(0.dp) }
    var height by remember { mutableStateOf(0.dp) }

    Canvas(
        modifier = Modifier
            .padding(height / 5)
            .width(width)
            .height(height)
    ) {
        drawIntoCanvas { canvas ->
            val paint = Paint().asFrameworkPaint().apply {
                this.isAntiAlias = true
                this.style = android.graphics.Paint.Style.FILL
                this.isFakeBoldText = true
                this.textSize = textSize * density
            }
            val rect = Rect()
            paint.getTextBounds(text, 0, text.length, rect)

            width = px2dp(density, rect.right - rect.left).dp
            height = px2dp(density, rect.bottom - rect.top).dp

            paint.shader = gradientShader.invoke(rect)

            val fontMetrics = paint.fontMetrics
            val distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
            canvas.nativeCanvas.drawText(
                text,
                0f,
                size.height / 2 + distance,
                paint
            )
        }
    }
}


