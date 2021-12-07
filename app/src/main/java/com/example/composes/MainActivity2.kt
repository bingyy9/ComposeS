package com.example.composes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.composes.ui.theme.ComposeSTheme

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSTheme {
                Conversation(SampleData.conversationSample)
            }
        }
    }

    @Composable
    fun MessageCard(message: Message){
        Row(
            Modifier
                .padding(all = 20.dp)
                .background(MaterialTheme.colors.background)) {
            Image(
                painter = painterResource(id = R.drawable.ic_document_filled) ,
                contentDescription = null,
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            //remember 是当前composable的私有变量
            var isExpanded by remember { mutableStateOf(false) }
            val surfaceColor: Color by animateColorAsState(
                if(isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface
            )
            Column(
                modifier = Modifier.clickable { isExpanded = !isExpanded }
            ) {
                Text(text = "Hello ${message.author}",
                    color = MaterialTheme.colors.secondaryVariant )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = MaterialTheme.shapes.medium, //圆角
                    elevation = 1.dp, //阴影
                    color = surfaceColor,
                    modifier = Modifier
                        .animateContentSize() //动画大小慢慢变大
                        .padding(1.dp)
                ) {
                    Text(
                        text = "Hello ${message.body}",
                        modifier = Modifier.padding(all = 4.dp),
                        style = MaterialTheme.typography.body2,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1
                    )
                }
            }
        }
    }

    @Composable
    fun Conversation(messages: List<Message>){
        LazyColumn{
            items(messages){
                message -> MessageCard(message)
            }
        }
    }
}
