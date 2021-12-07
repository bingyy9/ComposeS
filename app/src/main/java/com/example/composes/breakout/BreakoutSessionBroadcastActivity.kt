package com.example.composes.breakout

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

class BreakoutSessionBroadcastActivity : ComponentActivity() {
    private val breakoutViewModel by viewModels<BreakoutViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSTheme {
                BroadcastView(
                    breakoutViewModel.broadcastSessionsList,
                    breakoutViewModel.broadcastGroupList,
                    breakoutViewModel::onSendBroadcast,
                    onCancel = this::onCancel
                )
            }
        }
    }

    fun onCancel(){

    }
}
