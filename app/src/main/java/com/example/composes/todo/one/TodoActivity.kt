package com.example.composes.todo.one

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.example.composes.breakout.BreakoutViewModel
import com.example.composes.breakout.BroadcastView
import com.example.composes.todo.TodoItem
import com.example.composes.ui.theme.ComposeSTheme

class TodoActivity : ComponentActivity() {
    private val todoViewModel by viewModels<TodoViewModel>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSTheme {
//                TodoItemInput({ item -> {
//                    Log.d("TAG", item.task)
//                }
//                }, text, icon, submit, iconsVisible, setIcon)
                showTodoScreen()
            }
        }
    }

    @Composable
    private fun showTodoScreen(){
        //observeAsState list转成state对象
        //这里使用委托后，只要items列表改变，viewmodel里面的列表会自动刷新
//        val items: List<TodoItem> by todoViewModel.todoItems
        TodoScreen(
            todoViewModel.todoItems,
            todoViewModel.currentEditItem,
            onAddItem = todoViewModel::addItem,
            onRemoveItem = todoViewModel::removeItem,
            onStartEdit = todoViewModel::onEditItemSelected,
            onEditItemChange = todoViewModel::onEditItemChange,
            onEditDone = todoViewModel::onEditDone
        )
    }

}
