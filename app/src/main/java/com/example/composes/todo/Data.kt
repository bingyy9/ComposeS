package com.example.composes.todo

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.composes.R
import java.util.*

data class TodoItem(
    val task: String,
    var icon: TodoIcon = TodoIcon.Default,
    val id: UUID = UUID.randomUUID()
)

enum class TodoIcon(
    val imageVector: ImageVector,
    @StringRes val contentDescriptor: Int
) {
    //使用Material Design图标
    Square(Icons.Default.CropSquare, R.string.cd_expand),
    Done(Icons.Default.Done, R.string.cd_done),
    Event(Icons.Default.Event, R.string.cd_event),
    Privacy(Icons.Default.PrivacyTip, R.string.cd_privacy),
    Trash(Icons.Default.RestoreFromTrash, R.string.cd_restore);

    companion object{
        val Default = Square
    }
}