package com.example.composes.util2

import com.example.composes.todo.TodoIcon
import com.example.composes.todo.TodoItem

fun generateRandomTodoItem(): TodoItem{
    val message = listOf(
        "Learn compose",
        "Learn state",
        "Build dynamic UIs",
        "Learn Unidirectional Data Flow",
        "Integrate LiveData",
    ).random()
    val icon = TodoIcon.values().random()
    return TodoItem(message, icon)
}