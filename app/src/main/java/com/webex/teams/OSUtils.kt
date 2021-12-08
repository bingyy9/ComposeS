package com.webex.teams

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application.getProcessName
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.hardware.camera2.CameraManager
import android.os.Build
import android.provider.Settings
import com.cisco.webex.teams.utils.LoggingTags.APPLICATION_TAG
import com.example.composes.R
import com.example.composes.TeamsApplication
import com.webex.teams.logging.TeamsLogger
import com.webex.teams.util.TestUtils
import java.util.Locale

object OSUtils {
    private val isDeX: Boolean by lazy {
        try {
            val config: Configuration = TeamsApplication.applicationContext().resources.configuration
            val configClass: Class<*> = config.javaClass

            val isDexDevice = configClass.getField("SEM_DESKTOP_MODE_ENABLED").getInt(configClass) == configClass.getField("semDesktopModeEnabled").getInt(config)
            TeamsLogger.info(APPLICATION_TAG, "isDeX is $isDexDevice ")

            isDexDevice
        } catch (e: Exception) {
            TeamsLogger.info(APPLICATION_TAG, "isDeX is false. Message: ${e.message}.")
            false
        }
    }

    private val isChromeOSPrivate: Boolean by lazy {
        val isChromeDevice = TeamsApplication.applicationContext().packageManager?.hasSystemFeature("org.chromium.arc.device_management")
            ?: false
        TeamsLogger.info(APPLICATION_TAG, "isChromeOS is $isChromeDevice ")

        isChromeDevice
    }

    fun getPid(): Int {
        return android.os.Process.myPid()
    }

    fun hasOreo(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    fun hasPie(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

    fun hasAndroid10(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    fun hasAndroid11(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    fun autoRotationEnabled(context: Context): Boolean {
        return Settings.System.getInt(context.contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 1
    }

    fun isResizeableDevice(): Boolean {
        return isDeX || isChromeOSPrivate
    }

    /**
     * This function is using the application context to fetch resources which return the device type, NOT the layout type.
     * This function should only be used to make decisions about the underlying hardware, NOT to make layout (single or dual pane) decisions.
     *
     * @see isDualPane
     * @see [Resources](https://developer.android.com/reference/kotlin/android/content/res/Resources)
     */
    fun isTabletDevice(): Boolean {
        return TeamsApplication.applicationContext().resources.getBoolean(R.bool.tablet)
    }

    fun isChromeOS(): Boolean {
        return isChromeOSPrivate
    }

    /**
     * This function is meant to return the layout type (single or dual pane) of the activity by passing the activity's context.
     * This function should only be used to make decisions regarding layout (single or dual pane), NOT to make decisions regarding the underlying hardware.
     * @param activityContext : make sure to pass the activity's context, NOT the application's context.
     */
    fun isDualPane(activityContext: Context?): Boolean {
        return when {
            TestUtils.getForcedUIMode() == TestUtils.ForcedUIMode.TABLET -> {
                true
            }
            TestUtils.getForcedUIMode() == TestUtils.ForcedUIMode.PHONE -> {
                false
            }
            activityContext == null -> {
                false
            }
//            activityContext.getActivity() == null -> {
//                throw IllegalArgumentException("activityContext must be Activity's context")
//            }
            else -> {
                activityContext.resources?.getBoolean(R.bool.tablet) ?: false
            }
        }
    }

    fun getAndroidDeviceTypeForTelemetry(): String {
        return when {
            isDeX -> "Dex"
            isChromeOS() -> "ChromeOS"
            isTabletDevice() -> "Tablet"
            else -> "Phone"
        }
    }

    fun hasCallCapability(): Boolean {
        return TeamsApplication.applicationContext().packageManager?.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) ?: false
    }

    fun removeTransition(activity: Activity?) {
        activity?.overridePendingTransition(0, 0)
    }

    fun shouldDeviceEnableCamera2(): Boolean {
        return (
            Build.MODEL.equals("Pixel", ignoreCase = true) ||
                Build.MODEL.equals("Pixel XL", ignoreCase = true) ||
                Build.MODEL.equals("Pixel 2", ignoreCase = true) ||
                Build.MODEL.equals("Pixel 2 XL", ignoreCase = true)
            )
    }

    fun isCanary(): Boolean {
        return currentProcessName().lowercase(Locale.US).contains("canary")
    }

    @SuppressLint("NewApi")
    fun currentProcessName(): String {
        var processName = "undetermined"
        if (hasPie()) {
            processName = getProcessName()
        } else {
            val myPid = android.os.Process.myPid()
            val activityManager = TeamsApplication.applicationContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.runningAppProcesses?.forEach { process ->
                if (myPid == process.pid) {
                    processName = process.processName
                    return@forEach
                }
            }
        }

        return processName
    }

//    fun hasMoreThanOneCamera(context: Context): Boolean {
//        return context.getSystemService(CameraManager::class.java)?.cameraIdList?.size ?: 0 >= 2
//    }

    fun isDualPaneOrChromeBook(context: Context): Boolean {
        return isDualPane(context) || isChromeOS()
    }
}
