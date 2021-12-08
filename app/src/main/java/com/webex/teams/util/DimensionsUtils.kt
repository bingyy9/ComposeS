package com.webex.teams.util

import android.content.res.Resources

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
val Float.dp: Float
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f)
