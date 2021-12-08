package com.example.composed.bean

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList

data class BoSessionInfo(var assignedUserList:SnapshotStateList<BreakoutUser>, var sessionName:String, var sessionUUID:String, var currentJoinedCount:MutableState<Int>, var totalAssignedCount:MutableState<Int>)
