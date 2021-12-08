package com.example.composed.bean

data class BreakoutUser(var displayName:String, var role:Int, var userType:Int, var associateUsers:List<BreakoutUser>?)
