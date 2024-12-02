package com.iems5722.jade.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.iems5722.jade.ui.theme.JadeTheme

class WebViewActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            JadeTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    WebViewScreen()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun WebViewScreen() {
    val url = "https://jade.dev.hirsun.tech/map.html?topicId=4"

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true // 启用 JavaScript 支持
                settings.setDomStorageEnabled(true) // 启用 DOM 存储
                setLayerType(View.LAYER_TYPE_SOFTWARE, null) // 禁用硬件加速

                // WebViewClient 来处理网页加载
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        return super.shouldOverrideUrlLoading(view, request) // 允许 WebView 加载 URL
                    }

                    override fun onPageStarted(
                        view: WebView?,
                        url: String?,
                        favicon: android.graphics.Bitmap?
                    ) {
                        super.onPageStarted(view, url, favicon)
                        // 页面开始加载
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        // 页面加载完成后
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        super.onReceivedError(view, request, error)
                        // 页面加载错误
                    }
                }

                // 允许 Chrome 调试
                WebView.setWebContentsDebuggingEnabled(true)

                loadUrl(url) // 加载 URL
            }
        }
    )
}
