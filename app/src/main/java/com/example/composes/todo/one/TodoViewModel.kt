package com.example.composes.todo.one

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composes.todo.TodoItem

//viewmodel本身就是状态容器
class TodoViewModel : ViewModel() {
//    private var _todoItems = MutableLiveData(listOf<TodoItem>())
//    val todoItems: LiveData<List<TodoItem>> = _todoItems
//    fun addItem(item: TodoItem){
//        //只有value的值改变了，状态才算更新了。 如果往原来list里面加入数据，value值是不改变的
//        _todoItems.value = _todoItems.value!! + listOf(item)
//    }
//
//    fun removeItem(item: TodoItem){
//        _todoItems.value = _todoItems.value!!.toMutableList().also {
//            it.remove(item)
//        }
//    }

    //TodoItem 集合只读
    var todoItems = mutableStateListOf<TodoItem>()
        private set
    //正在编辑的索引
    private var currentEditPosition by mutableStateOf(-1)
    var currentEditItem: TodoItem? = null
        get() = todoItems.getOrNull(currentEditPosition)

    fun addItem(item: TodoItem){
        todoItems.add(item)
    }

    fun removeItem(item: TodoItem){
        todoItems.remove(item)
        onEditDone()
    }

    fun onEditDone(){
        currentEditPosition = -1
    }

    fun onEditItemSelected(item: TodoItem){
        currentEditPosition = todoItems.indexOf(item)
    }

    fun onEditItemChange(item: TodoItem){
        val currentItem = requireNotNull(item)
        require(currentItem.id == item.id){
            "You can only change an item with the same id as currentEditItem. "
        }
        todoItems[currentEditPosition] = item
    }
}