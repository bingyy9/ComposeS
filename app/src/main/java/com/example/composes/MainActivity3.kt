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
import com.example.composes.todo.TodoIcon
import com.example.composes.todo.TodoItem
import com.example.composes.todo.one.TodoScreen
import com.example.composes.ui.theme.ComposeSTheme
import com.example.composes.ui.theme.PhotographerCard

class MainActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSTheme {
//                PhotographerCard()
//                LayoutStudy()
//                SimpleList()
//                ScrollingList()
//                TextWithPaddingToBaseLine()
//                StaggeredGridBodyContent()
//                ConstrainLayoutContent()
//                LargeConstrainLayoutContent()
//                DecoupleConstrainLayoutContent2()
//                showTodoScreen()
            }
        }
    }

    @Composable
    private fun showTodoScreen(){
//        val items = listOf(
//            TodoItem("Learn compose", TodoIcon.Event),
//            TodoItem("Take the code lab"),
//            TodoItem("Apply state", TodoIcon.Done),
//            TodoItem("Build dynamic UIs", TodoIcon.Square)
//        )
//        TodoScreen(items)
    }
}
