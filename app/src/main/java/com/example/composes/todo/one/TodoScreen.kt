package com.example.composes.todo.one

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.composes.todo.TodoItem
import com.example.composes.util2.generateRandomTodoItem
import kotlin.random.Random

@Composable
fun TodoScreen(
    items: List<TodoItem>,
    currentEditing: TodoItem?,
    onAddItem: (TodoItem) -> Unit,
    onRemoveItem: (TodoItem) -> Unit,
    onStartEdit: (TodoItem) -> Unit,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit,
){
    Column() {
        TodoItemInputBackground(
            elevate = true,
        ) {
            val enableTopSection = currentEditing == null
            if(enableTopSection) {
                TodoItemEntryInput(onItemComplete = onAddItem)
            } else {
                Text(
                    text = "Editing Item",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(16.dp)
                        .fillMaxWidth() //填充父容器
                )
            }
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(8.dp)
        ){
            items(items){ todo ->
                if(currentEditing?.id == todo.id){
                    TodoItemInlineEditor(
                        item = currentEditing,
                        onEditItemChange = onEditItemChange,
                        onEditDone = onEditDone,
                        onRemoveItem = { onRemoveItem(todo) })
                } else {
                    TodoRow(
                        todo = todo,
                        onItemClicked = { onStartEdit(todo) },
                        modifier = Modifier.fillParentMaxWidth(),
                    )
                }
            }
        }
        
        Button(
            onClick = { onAddItem(generateRandomTodoItem()) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "add random item")
        }
    }
}

@Composable
fun TodoRow(
    todo: TodoItem,
    onItemClicked: (TodoItem) -> Unit,
    modifier: Modifier
){
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onItemClicked(todo) },
        //子元素水平均匀分布
        horizontalArrangement = Arrangement.SpaceBetween,
    ){
        Text(text = todo.task)
        //保存key todo.id --> value iconAlpha
        val iconAlpha: Float = remember(todo.id) { randomTint() }
        Icon(
            imageVector = todo.icon.imageVector,
            //Icon着色的值，alpha设置随机透明度。
            //LocalContentColor拿到当前的值，然后copy，仅仅修改alpha
            tint = LocalContentColor.current.copy(alpha = iconAlpha),
            contentDescription = stringResource(id = todo.icon.contentDescriptor)
        )
    }
}

private fun randomTint(): Float{
    return Random.nextFloat().coerceIn(0.3f, 0.9f)
}