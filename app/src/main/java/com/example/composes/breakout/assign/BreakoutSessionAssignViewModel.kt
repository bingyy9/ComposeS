package com.example.composes.breakout.assign

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composes.TeamsApplication
import com.example.composes.R
import com.example.composes.breakout.broadcast.BreakoutSessionBroadcastViewModel
import com.example.composes.breakout.repo.AssignType
import com.example.composes.breakout.repo.BreakoutSession
import com.example.composes.breakout.repo.BroadcastType
import com.webex.teams.logging.TeamsLogger

class BreakoutSessionAssignViewModel : ViewModel() {
    //monitor user change evt, trigger ui update
    var waitingAssignUserCount by mutableStateOf(0)
    //monitor sessionNum change, trigger ui update
    var sessionNum = 1
    var autoAssignCoHost = false
    var assignType by mutableStateOf(AssignType.ASSIGN_PARTICIPANTS_AUTOMATICALLY)

    companion object {
        const val TAG = "BreakoutSessionAssignViewModel"
    }

    init {
        mockData()
    }

    private fun mockData() {
        waitingAssignUserCount = 10
    }

    fun onAssignTypeSelected(assignType: AssignType){
        this.assignType = assignType
    }

    fun onCreateBreakoutSession(sessionNum: Int, assignType: AssignType, autoAssignCoHost: Boolean){
        TeamsLogger.info(TAG, "sessionNum: $sessionNum, assignType: $assignType, autoAssignCoHost: $autoAssignCoHost")
        this.autoAssignCoHost = autoAssignCoHost
        this.sessionNum = sessionNum
        this.assignType = assignType
        //invoke create breakout session interface
    }
}