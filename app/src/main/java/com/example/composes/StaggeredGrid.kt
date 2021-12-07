package com.example.composes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max

val topics = listOf(
    "Arts & Crafts",
    "Beauty",
    "Books",
    "Business",
    "Comics",
    "Arts & Crafts",
    "Beauty",
    "Books",
    "Business",
    "Comics",
    "Comics"
)

//计算Stagger每行的宽，高， 最宽的就是整个自定义view的宽度，每行高度相加就是自定义view的高度

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
){
    Layout(
        modifier = Modifier,
        content = content
    ) { measurables, constraints ->
        //计算每行的宽高
        val rowWidths = IntArray(rows){0}
        val rowHeights = IntArray(rows){0}

        val placeables = measurables.mapIndexed{index, measurable ->
            //测量每一个元素
            val placeable = measurable.measure(constraints)
            //计算每一行的宽度和高度
            //元素下标假设11个， 0,1,2,3,4,5,6,7,8,9,10,11
            // 3行， rows=3, row: 0, 1, 2
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = max(rowHeights[row], placeable.height)
            placeable

        }
        val width = rowWidths.maxOrNull() ?: constraints.minWidth
        val height = rowHeights.sumOf { it }

        //每一个元素的坐标
        val rowY = IntArray(rows){0}
        //第0行的Y坐标肯定是0
        for(i in 1 until rows){
            rowY[i] = rowHeights[i - 1] + rowY[i - 1]
        }
        layout(width, height){
            val rowX = IntArray(rows){0}
            //设置每一个元素的坐标
            placeables.forEachIndexed{ index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row],
                )
                //第一列x坐标全部为0， 下一列x坐标要累加前面元素的宽度
                //设置下一列X的坐标
                rowX[row] += placeable.width
            }
        }
    }
}

@Composable
fun StaggeredGridBodyContent(modifier: Modifier = Modifier){
    Row(
        modifier = modifier
            .background(color = Color.LightGray)
            .padding(16.dp)
            .horizontalScroll(rememberScrollState()),
        content = {
            StaggeredGrid(modifier = Modifier) {
                for(topic in topics){
                    Chip(modifier = Modifier.padding(8.dp), text = topic)
                }
            }
        }

    )
}

@Composable
fun Chip(modifier: Modifier = Modifier,
    text: String){
    //一个圆角Card，里面是Row
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            ){

            }

            Spacer(modifier = Modifier.width(4.dp))
            Text(text = text)
        }
    }
}