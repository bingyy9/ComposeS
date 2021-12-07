package com.example.composes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch

@Composable
fun SimpleColumn() {
    Column{
        repeat(100){
            Text(text = "Item #$it", style = MaterialTheme.typography.subtitle1)
        }
    }
}

//可滚动
@Composable
fun SimpleList() {
    val scrollState = rememberScrollState()
    //滑动导致scrollState对象改变，Column重组/重绘
    Column(Modifier.verticalScroll(scrollState)){
        repeat(100){
            Text(text = "Item #$it", style = MaterialTheme.typography.subtitle1)
        }
    }
}

@Composable
fun LazyListColumn() {
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState){
        items(100){
            Text(text = "Item #$it", style = MaterialTheme.typography.subtitle1)
        }
    }
}

@Composable
fun ScrollingList(){
    val listSize = 100;
    val scrollState = rememberLazyListState()
    var coroutineScope = rememberCoroutineScope()
    Column() {
        Row(){
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(0)
                    }
                }
            ){
                Text(text = "Scroll To Top")
            }

            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(listSize - 1)
                    }
                }
            ){
                Text(text = "Scroll To Bottom")
            }
        }

        LazyColumn(state = scrollState){
            items(listSize){
                ImageListItem(index = it)
            }
        }
    }
}

@Composable
fun ImageListItem(index: Int){
    Row(verticalAlignment = Alignment.CenterVertically){
        Image(painter = rememberImagePainter(
                data = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fbpic.588ku.com%2Felement_pic%2F01%2F00%2F03%2F6856f398cabcca4.jpg&refer=http%3A%2F%2Fbpic.588ku.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1640325790&t=6e68c55d971ec18f71a44cc9c65a5887"
            ),
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = "Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}