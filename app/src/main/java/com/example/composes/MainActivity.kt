package com.example.composes

import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composes.ui.theme.ComposeSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSTheme {
                MessageCard(Message("test", "test22"))
            }
        }
    }

    @Composable
    fun MessageCard(message: Message){
        Row(Modifier.padding(all = 20.dp)
            .background(MaterialTheme.colors.background)) {
            Image(
                painter = painterResource(id = R.drawable.ic_document_filled) ,
                contentDescription = null,
                Modifier.size(40.dp)
                    .clip(CircleShape)
            )
            Column() {
                Text(text = "Hello ${message.author}")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Hello ${message.body}")
            }
        }
    }

    @Preview
    @Composable
    fun PreviewMessageCard(){
        MessageCard(Message("test", "test22"))
    }
}

