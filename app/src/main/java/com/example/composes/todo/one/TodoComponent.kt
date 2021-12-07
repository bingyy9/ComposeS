package com.example.composes.todo.one

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.composes.todo.TodoIcon
import com.example.composes.todo.TodoItem

@Composable
fun TodoItemInlineEditor(
    item: TodoItem,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit,
    onRemoveItem: () -> Unit,
){
    TodoItemInput(
        text = item.task,
        onTextChange = { onEditItemChange(item.copy(task = it)) },
        icon = item.icon,
        onIconChange = { onEditItemChange(item.copy(icon = it)) },
        submit = { onEditDone() },
        iconsVisible = true,
        buttonSlot = {
            //保存和删除
            Row {
                val shrinkButtons = Modifier.widthIn(20.dp)
                TextButton(onClick = { onEditDone() }, modifier = shrinkButtons) {
                    Text(
                        text = "\uD83D\uDCBE",  //Emoji符号
                        textAlign = TextAlign.End,
                    )
                }
                TextButton(onClick = { onRemoveItem() }, modifier = shrinkButtons) {
                    Text(
                        text = "X",  //Emoji符号
                        textAlign = TextAlign.End,
                    )
                }
            }

        }
    )
}

@Composable
fun TodoItemInputBackground(
    elevate: Boolean,  //阴影控制
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
){
    //帧动画的形式展现Surface底部的阴影
    val animatedElevation by animateDpAsState(if (elevate) 1.dp else 0.dp, TweenSpec(500))
    Surface(
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f),
        elevation = animatedElevation, //阴影
        shape = RectangleShape,

    ) {
        Row(
            modifier = modifier.animateContentSize(animationSpec = TweenSpec(300)),
            content = content
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TodoInputText(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onImeAction: () -> Unit = {} //带默认值
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = text,
        onValueChange = onTextChange,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        maxLines = 1,
        modifier = modifier,
        //配置软键盘
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction()
            keyboardController?.hide() //隐藏键盘
        })
    )
}

@Composable
fun TodoEditButton(
    onClick: ()->Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = { onClick() },
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(),
        modifier = modifier,
        enabled = enabled
    ) {
        Text(text = text)
    }
}

//一排图标，根据文本框是否有内容，自动弹出或收起，带动画效果
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedIconRow(
    icon: TodoIcon,
    onIconChange: (TodoIcon) -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
){
    val enter = remember { fadeIn(animationSpec = TweenSpec(300, easing = FastOutLinearInEasing)) }
    val exit = remember { fadeOut(animationSpec = TweenSpec(100, easing = FastOutSlowInEasing)) }
    //最小高度
    Box(modifier.defaultMinSize(16.dp)){
        AnimatedVisibility(
            visible = visible,
            enter = enter,
            exit = exit
        ) {
            IconRow(icon = icon, onIconChange = onIconChange)
        }
    }
}

@Composable
fun IconRow(
    icon: TodoIcon,
    onIconChange: (TodoIcon) -> Unit,
    modifier: Modifier = Modifier,
){
    Row(modifier){
        for(todoIcon in TodoIcon.values()){
            SelectableIconButton(
                icon = todoIcon.imageVector,
                iconContentDes = todoIcon.contentDescriptor,
                onIconSelected = { onIconChange(todoIcon) },
                isSelected = (todoIcon == icon),
                modifier
            )
        }
    }
}

@Composable
fun SelectableIconButton(
    icon: ImageVector,
    iconContentDes: Int,
    onIconSelected: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    //图标选中和未选中的颜色
    val tint = if(isSelected){
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
    }

    TextButton(
        onClick = { onIconSelected() },
        shape = CircleShape,
        modifier = modifier
    ) {
        Column {
            Icon(
                imageVector = icon,
                tint = tint, //颜色
                contentDescription = stringResource(id = iconContentDes),
            )
            if(isSelected){
                Box(
                    Modifier
                        .padding(top = 3.dp)
                        .width(icon.defaultWidth)
                        .height(1.dp)
                        .background(tint)
                )
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun TodoItemEntryInput(
    onItemComplete: (TodoItem) -> Unit
){
    //通过它，可以完成智能重组，只要text改变，就能智能重组
    val (text, setText) = remember{ mutableStateOf("")}
    val (icon, setIcon) = remember{ mutableStateOf(TodoIcon.Default) }
    val iconsVisible = text.isNotBlank()
    val submit = {
        onItemComplete(TodoItem(text, icon))
        setIcon(TodoIcon.Default)
        setText("")
    }
    TodoItemInput(
        text = text,
        onTextChange = setText,
        icon = icon,
        onIconChange = setIcon,
        submit = submit,
        iconsVisible = iconsVisible
    ) {
        //button weight是0，先放button，剩下是输入框weight 1
        TodoEditButton(
            onClick = submit,
            text = "Add",
            enabled = text.isNotBlank()
        )
    }
}

@Composable
fun TodoItemInput(
    text: String,
    onTextChange: (String) -> Unit,
    icon: TodoIcon,
    onIconChange: (TodoIcon) -> Unit,
    submit: () -> Unit,
    iconsVisible: Boolean,
    //最右侧的图标，在编辑时会有删除和保存，添加时是Add按钮
    buttonSlot: @Composable ()->Unit
){
    Column() {
        Row(
            Modifier
                //水平和顶部16dp
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            TodoInputText(
                text = text,
                onTextChange = onTextChange,
                modifier = Modifier
                    .weight(1f) //
                    .padding(end = 8.dp),
                onImeAction = submit
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.align(Alignment.CenterVertically)){
                buttonSlot()
            }
//            //button weight是0，先放button，剩下是输入框weight 1
//            TodoEditButton(
//                onClick = submit,
//                text = "Add2",
//                modifier = Modifier.align(Alignment.CenterVertically),
//                enabled = text.isNotBlank()
//            )
        }
        if(iconsVisible) {
            AnimatedIconRow(
                icon,
                onIconChange,
                Modifier.padding(top = 8.dp),
                iconsVisible
            )
        } else {
            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}