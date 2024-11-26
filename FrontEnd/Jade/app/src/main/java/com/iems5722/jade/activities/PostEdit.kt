package com.iems5722.jade.activities

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.iems5722.jade.R
import com.iems5722.jade.ui.theme.JadeTheme

@Suppress("DEPRECATION")
//TODO: 闪退问题
class PostEdit : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 从 Intent 中获取选中的图片 URI 列表
        val selectedImages = intent.getParcelableArrayListExtra<Uri>("selected_images")

        // 检查是否为 null
        if (selectedImages.isNullOrEmpty()) {
            // 如果为空，显示错误日志并结束 Activity
            Log.e("PostEditActivity", "No images provided for editing.")
//            finish() // 结束当前 Activity，避免崩溃
            return
        }

        setContent {
            JadeTheme {
                Scaffold {
                    PostEditScreen(selectedImages)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostEditScreen(selectedImages: List<Uri>) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val tags = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 顶部导航栏
        TopAppBar(
            title = { Text("Edit Post") },
            navigationIcon = {
                IconButton(onClick = { /* Back operation */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                TextButton(onClick = { /* Publish action */ }) {
                    Text("Publish", color = MaterialTheme.colorScheme.primary)
                }
            }
        )

        // 图片展示区域
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(selectedImages) { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { /* Image click action */ },
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 标题输入框
        BasicTextField(
            value = title,
            onValueChange = { title = it },
            textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            decorationBox = { innerTextField ->
                if (title.isEmpty()) {
                    Text("Add Title", color = Color.Gray)
                }
                innerTextField()
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 正文输入框
        BasicTextField(
            value = content,
            onValueChange = { content = it },
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(200.dp),
            decorationBox = { innerTextField ->
                if (content.isEmpty()) {
                    Text("Share your thoughts...", color = Color.Gray)
                }
                innerTextField()
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 标签添加区域
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tags.forEach { tag ->
                Box(
                    modifier = Modifier
                        .background(Color.LightGray, RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("#$tag", color = Color.Black)
                }
            }
            TextButton(onClick = { /* Add Tag logic */ }) {
                Text("Add Tag")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 位置信息等其他选项
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { /* Location action */ },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = "Add Location", color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
