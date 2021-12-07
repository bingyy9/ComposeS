package com.example.composes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.composes.ui.theme.ComposeSTheme

//定义firstBaseLineToTop是Modifier的扩展函数，return this,下面就可以直接链式调用
fun Modifier.firstBaseLineToTop(
    firstBaseLineToTop: Dp
) = this.then(
    layout{ measurable, constraints ->
        //测量元素
        val placeable = measurable.measure(constraints)
        //测量之后，获取元素的基线值
        val firstBaseLine = placeable[FirstBaseline]
        //元素新的Y左边 = 新基准线 - 旧基准线
        val placeableY = firstBaseLineToTop.roundToPx() - firstBaseLine
        //
        val height = placeable.height + placeableY
        layout(placeable.width, height){
            placeable.placeRelative(0, placeableY)
        }

    }
)

@Composable
fun TextWithPaddingToBaseLine(){
    ComposeSTheme {
        Text(text = "Hi there", Modifier.firstBaseLineToTop(24.dp).background(Color.Red))
    }
}