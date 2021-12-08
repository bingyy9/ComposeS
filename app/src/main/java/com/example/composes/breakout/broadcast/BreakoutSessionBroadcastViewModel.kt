package com.example.composes.breakout.broadcast

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composes.TeamsApplication
import com.example.composes.R
import com.example.composes.breakout.repo.BreakoutSession
import com.example.composes.breakout.repo.BroadcastType
import com.webex.teams.logging.TeamsLogger

class BreakoutSessionBroadcastViewModel : ViewModel() {

    companion object {
        const val TAG = "BreakoutSessionBroadcastViewModel"
        const val ALL_BREAKOUT_SESSIONS = "ALL_BREAKOUT_SESSIONS"
        const val ALL_COHOST_PRESENTER = "ALL_COHOST_PRESENTER"
        const val ALL_PARTICIPANTS = "ALL_COHOST_PRESENTER"
    }

    var breakoutSessions = MutableLiveData<MutableList<BreakoutSession>>()
    private var broadcastSessionPosition by mutableStateOf(0)
    private var broadcastGroupPosition by mutableStateOf(0)
    var broadcastGroupList: List<BroadcastType> = listOf (
        BroadcastType(ALL_COHOST_PRESENTER, TeamsApplication.applicationContext()?.resources?.getString(R.string.BO_BREAKOUT_SESSION_BROADCAST_ALL_PRESENTERS)),
        BroadcastType(ALL_PARTICIPANTS, TeamsApplication.applicationContext()?.resources?.getString(R.string.BO_BREAKOUT_SESSION_BROADCAST_ALL_PARTICIPANTS)),
    )
    private set

    var broadcastSessionsList: MutableList<BroadcastType> = mutableListOf()
    private set

    init {
        breakoutSessions.value = mutableStateListOf()
        mockData()
    }

    fun mockData(){
        var breakoutSession1 = BreakoutSession("Breakout Session 1")
        var breakoutSession2 = BreakoutSession("Breakout Session 2")
        var breakoutSession3 = BreakoutSession("Breakout Session 3")
        breakoutSessions.value.also {
            it?.add(breakoutSession1)
            it?.add(breakoutSession2)
            it?.add(breakoutSession3)
        }

        breakoutSessions.value?.forEach {
            broadcastSessionsList.add(BroadcastType(it.id.toString(), it.name))
        }

        TeamsApplication.applicationContext()?.resources?.let{
            broadcastSessionsList.add(0, BroadcastType(ALL_BREAKOUT_SESSIONS, it.getString(R.string.BO_BREAKOUT_SESSION_BROADCAST_ALL_SESSIONS)))
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun onSendBroadcast(session: BroadcastType, group: BroadcastType, message: String) {
        TeamsLogger.info(TAG, "${session.name} + ${group.name} + $message")
    }


}