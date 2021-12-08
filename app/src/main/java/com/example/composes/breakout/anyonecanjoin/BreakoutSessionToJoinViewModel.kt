package com.example.composes.breakout.anyonecanjoin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.composed.bean.BreakoutUser
import com.example.composes.breakout.anyonecanjoin.repo.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import kotlin.concurrent.timerTask

//data class BreakoutSessionToJoinUiState()

class BreakoutSessionToJoinViewModel(private val savedState: SavedStateHandle) : ViewModel() {
//    var uiState = mutableStateOf(BreakoutSessionToJoinUiState())

    private val _collapsedIDsIdsList = MutableStateFlow(listOf<String>())
    val collapsedIDsIdsList: StateFlow<List<String>> get() = _collapsedIDsIdsList

    // Business logic
    var breakoutSessions = SampleData.breakoutSessions

    val joinBreakOutSession: (String) -> Unit = { breakoutSessionId ->
        println("Join breakout session $breakoutSessionId")
    }

    val onHeaderItemClick: (String) -> Unit = { breakoutSessionId ->
        println("onHeaderItemClick $breakoutSessionId")
        _collapsedIDsIdsList.value = _collapsedIDsIdsList.value.toMutableList().also { list ->
            if (list.contains(breakoutSessionId)) list.remove(breakoutSessionId) else list.add(
                breakoutSessionId
            )
        }
    }

    // simulate data change
    val timer = Timer()

    init {
        timer.schedule(timerTask {
            val generatedItem = BreakoutUser(
                "generate user",
                SampleData.ROLE_GUEST,
                SampleData.USER_TYPE_NORMAL,
                null
            )
            breakoutSessions.get(0).assignedUserList.add(generatedItem)
            breakoutSessions.get(0).totalAssignedCount.value = 5
            println("add generate item")
        }, 10000)
    }


    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}