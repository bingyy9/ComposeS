package com.example.composes

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension

@Composable
fun ConstrainLayoutContent(){
    ConstraintLayout {
        //通过createRefs创建引用，ConstrainLayout中的每个元素都需要关联一个
        val (button, text) = createRefs()
        Button(
            onClick = { },
            //使用Modifier.constrainAs来提供约束，引用作为它的第一个参数
            //在lambda中指定约束
            modifier = Modifier.constrainAs(button){
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text(text = "Button")
        }

        Text(
            text = "Text",
            modifier = Modifier.constrainAs(text){
                top.linkTo(button.bottom, margin = 16.dp)
                centerHorizontallyTo(parent)
            }
        )
    }
}

@Composable
fun ConstrainLayoutContent2(){
    ConstraintLayout {
        //通过createRefs创建引用，ConstrainLayout中的每个元素都需要关联一个
        val (button1, button2, text) = createRefs()
        Button(
            onClick = { },
            modifier = Modifier.constrainAs(button1){
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text(text = "Button 1")
        }

        Text(
            text = "Text",
            modifier = Modifier.constrainAs(text){
                top.linkTo(button1.bottom, margin = 16.dp)
                //text的中间线和button1的end对齐
                centerAround(button1.end)
            }
        )

        //将button1和text组合起来，建立屏障barrier
        val barrier = createEndBarrier(button1, text)
        Button(
            onClick = { },
            modifier = Modifier.constrainAs(button1){
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(barrier)
            }
        ) {
            Text(text = "Button 2")
        }
    }
}

@Composable
fun LargeConstrainLayoutContent(){
    ConstraintLayout {
        val (text) = createRefs()
        val guideline = createGuidelineFromStart(fraction = 0.5f)
        Text(
            text = "Text11111231231231232131321312321312321312312313123",
            modifier = Modifier.constrainAs(text){
                start.linkTo(guideline)
                end.linkTo(parent.end)
                width = Dimension.preferredWrapContent
            }
        )
    }
}

@Composable
fun DecoupleConstrainLayoutContent(){
    val margin = 16.dp
    ConstraintLayout() {
        val (button, text) = createRefs()
        Button(
            onClick = { },
            modifier = Modifier.constrainAs(button){
                top.linkTo(parent.top, margin = margin)
            }
        ) {
            Text(text = "Button 1")
        }

        Text(
            text = "Text",
            modifier = Modifier.constrainAs(text){
                top.linkTo(button.bottom, margin = margin)
                centerHorizontallyTo(button)
            }
        )
    }
}

@Composable
fun DecoupleConstrainLayoutContent2(){
    BoxWithConstraints() {
        val constrains = if(maxWidth < maxHeight){
            //portrait
            decoupledConstraints(16.dp)
        } else {
            //landscape
            decoupledConstraints(160.dp)
        }
        ConstraintLayout(constrains) {
            Button(
                onClick = { },
                modifier = Modifier.layoutId("button")
            ) {
                Text(text = "Button 1")
            }

            Text(
                text = "Text",
                modifier = Modifier.layoutId("text")
            )

        }
    }
}

private fun decoupledConstraints(margin: Dp): ConstraintSet{
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")
        constrain(button){
            top.linkTo(parent.top, margin = margin)
        }
        constrain(text){
            top.linkTo(button.bottom, margin = margin)
        }
    }
}