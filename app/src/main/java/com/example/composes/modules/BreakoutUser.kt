package com.example.composed.bean

import androidx.compose.runtime.MutableState

data class BreakoutUser(var displayName:String, var role:Int, var userType:Int, var associateUsers:MutableState<MutableList<BreakoutUser>>?)
