package com.example.composes.breakout.anyonecanjoin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.composes.breakout.anyonecanjoin.repo.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

//data class BreakoutSessionToJoinUiState()

class BreakoutSessionToJoinViewModel(private val savedState: SavedStateHandle) : ViewModel() {
//    var uiState = mutableStateOf(BreakoutSessionToJoinUiState())


    private val _collapsedIDsIdsList = MutableStateFlow(listOf<String>())
    val collapsedIDsIdsList: StateFlow<List<String>> get() = _collapsedIDsIdsList

    // Business logic
    fun onItemClicked(breakoutSessionId: String) {
        _collapsedIDsIdsList.value = _collapsedIDsIdsList.value.toMutableList().also { list ->
            if (list.contains(breakoutSessionId)) list.remove(breakoutSessionId) else list.add(breakoutSessionId)
        }
    }

    var breakoutSessions = SampleData.breakoutSessions

    val joinBreakOutSession:(String)->Unit = {breakoutSessionId->
        println("Join breakout session $breakoutSessionId")
    }

    val onHeaderItemClick:(String)->Unit = {breakoutSessionId->
        println("onHeaderItemClick $breakoutSessionId")
        _collapsedIDsIdsList.value = _collapsedIDsIdsList.value.toMutableList().also { list ->
            if (list.contains(breakoutSessionId)) list.remove(breakoutSessionId) else list.add(breakoutSessionId)
        }
    }
}