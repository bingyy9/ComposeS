package com.webex.teams.util

import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.util.Size
import android.view.Display
import android.view.WindowManager
import com.webex.teams.OSUtils

@Suppress("DEPRECATION")
fun Context.getMetricsWidthExtensionFunc(metrics: DisplayMetrics): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as? WindowManager
    return if (OSUtils.hasAndroid11()) {
        windowManager?.currentWindowMetrics?.bounds?.right?.minus(windowManager.currentWindowMetrics.bounds.left) ?: 0
    } else {
        windowManager?.defaultDisplay?.getMetrics(metrics)
        metrics.widthPixels
    }
}

@Suppress("DEPRECATION")
fun Context.getMetricsHeightExtensionFunc(metrics: DisplayMetrics): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as? WindowManager
    return if (OSUtils.hasAndroid11()) {
        windowManager?.currentWindowMetrics?.bounds?.bottom?.minus(windowManager.currentWindowMetrics.bounds.top) ?: 0
    } else {
        windowManager?.defaultDisplay?.getMetrics(metrics)
        metrics.heightPixels
    }
}

@Suppress("DEPRECATION")
fun Context.getDisplayExtensionFunc(): Display? {
    return if (OSUtils.hasAndroid11()) {
        display
    } else {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        windowManager?.defaultDisplay
    }
}

@Suppress("DEPRECATION")
fun Context.getSizeExtensionFunc(): Size {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as? WindowManager
    return if (OSUtils.hasAndroid11()) {
        val rect = windowManager?.currentWindowMetrics?.bounds
        Size(rect?.width() ?: 0, rect?.height() ?: 0)
    } else {
        val displaySize = Point()
        windowManager?.defaultDisplay?.getSize(displaySize)
        Size(displaySize.x, displaySize.y)
    }
}
