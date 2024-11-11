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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
            // TODO: User image, logic, appearance
            Text(
                text = "Image",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Nick name",
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
                    // TODO: What to deliver?
                    val intent = Intent(context, Topic::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Login/Register")
            }
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