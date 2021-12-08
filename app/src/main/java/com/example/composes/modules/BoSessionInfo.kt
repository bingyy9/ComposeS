package com.example.composed.bean

data class BoSessionInfo(var assignedUserList:List<BreakoutUser>, var sessionName:String, var sessionUUID:String, var currentJoinedCount:Int, var totalAssignedCount:Int)
