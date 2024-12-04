package com.iems5722.jade.utils

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.iems5722.jade.R
import com.iems5722.jade.activities.PostEdit

/**
 * 图片上传工具类
 *
 * @author CalvinHaynes
 * @since
 */
class ImageUploadHelper {

    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun setupMediaPicker(context: Context) = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uri ->
            val intent = Intent(context, PostEdit::class.java).apply {
                putExtra("selected_image", uri.toString())
            }
            val options = ActivityOptions.makeCustomAnimation(
                context,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            context.startActivity(intent, options.toBundle())
        }
    )
}
