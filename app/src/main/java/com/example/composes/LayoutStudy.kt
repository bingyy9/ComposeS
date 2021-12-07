package com.example.composes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LayoutStudy() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "LayoutStudy") },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Favorite, contentDescription = null)
                    }
                }
            )
        }
    ) {
        //TopBar下面内部填充
        innerPadding -> BodyContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier){
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Hi there!")
        Text(text = "asdfasdfasfdasdf")
    }
}