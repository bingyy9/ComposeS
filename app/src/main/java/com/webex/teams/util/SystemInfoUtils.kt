//package com.webex.teams.util
//
//import android.app.ActivityManager
//import android.app.KeyguardManager
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.Context
//import android.content.pm.ApplicationInfo
//import android.content.pm.PackageManager
//import android.media.AudioManager
//import android.net.ConnectivityManager
//import android.os.Build
//import android.os.PowerManager
//import androidx.annotation.RequiresApi
//import androidx.biometric.BiometricManager
//import androidx.core.content.ContextCompat
//import com.webex.scf.CoreFramework
//import com.webex.scf.NativeUtilities
//import com.webex.scf.commonhead.models.FeatureSetting
//import com.webex.scf.commonhead.models.SettingType
//import com.webex.teams.BuildConfig
//import com.webex.teams.logging.TeamsLogger
//import com.webex.teams.notifications.PushNotificationChannelManager.NotificationChannelGroups
//import com.webex.teams.ui.settings.FeedbackViewModel
//import com.webex.teams.ui.settings.Settings
//import com.webex.teams.utils.LoggingTags.FEEDBACK_TAG
//import com.webex.teams.utils.LoggingTags.SYSTEM_INFO_TAG
//import org.json.JSONArray
//import org.json.JSONObject
//import java.io.File
//import java.io.FileNotFoundException
//import java.io.FileOutputStream
//import java.io.PrintWriter
//import java.lang.Boolean.parseBoolean
//import java.text.DecimalFormat
//import java.util.Arrays
//import java.util.Date
//import java.util.Locale
//import java.util.TimeZone
//
//class SystemInfoUtils {
//    companion object {
//
//        private const val APP_INFO_FILE_NAME = "sysinfo.txt"
//        private const val DEVICE_INFO_FILE_NAME = "deviceinfo.txt"
//        private const val ONE_MEGABYTE = (1024 * 1024).toFloat()
//        private const val USER_TOGGLE_MENTION_NOTIFICATIONS = "mention-notifications"
//        private const val USER_TOGGLE_GROUP_MESSAGE_NOTIFICATIONS = "group-message-notifications"
//
//        /**
//         * Hidden variable in AudioManager, but we need this one to adjust BT in-call volume
//         * The definition in OS source code is:
//         * public static final int STREAM_BLUETOOTH_SCO = AudioSystem.STREAM_BLUETOOTH_SCO;
//         */
//        private const val STREAM_BLUETOOTH_SCO = 6
//
//        fun generateDeviceInfoFile(
//            logLocation: File,
//            context: Context,
//            feedbackVM: FeedbackViewModel
//        ): File? {
//            var versionFile: File? = null
//            try {
//                versionFile = File(logLocation.toString() + File.separator + DEVICE_INFO_FILE_NAME)
//                val writer = PrintWriter(FileOutputStream(versionFile, false))
//                generateDeviceInfo(writer, context, feedbackVM)
//                writer.close()
//                return versionFile
//            } catch (e: FileNotFoundException) {
//                TeamsLogger.error(FEEDBACK_TAG, e, "Error writing to $DEVICE_INFO_FILE_NAME")
//            }
//            return versionFile
//        }
//
//        fun generateSysInfoFile(
//            logLocation: File,
//            context: Context,
//            feedbackViewModel: FeedbackViewModel,
//            settings: Settings,
//            coreFramework: CoreFramework
//        ): File? {
//            var versionFile: File? = null
//            try {
//                versionFile = getSysInfoFile(logLocation)
//
//                val writer = PrintWriter(FileOutputStream(versionFile, false))
//                generateUserInfo(writer, coreFramework, feedbackViewModel)
//                generateAppInfo(writer, logLocation, context, feedbackViewModel)
//                generateSystemInfo(writer, context)
//                generatePermissionsInfo(writer, context)
//                generateUserSettingsInfo(writer, settings, feedbackViewModel)
//                try {
//                    generateNotificationsInfo(writer, context, feedbackViewModel)
//                } catch (e: Exception) {
//                    writer.println("Exception while capturing notification settings: ${e.message}")
//                }
//
//                writer.close()
//                return versionFile
//            } catch (e: FileNotFoundException) {
//                TeamsLogger.error("$FEEDBACK_TAG $SYSTEM_INFO_TAG", e, "Error writing to $APP_INFO_FILE_NAME")
//            }
//
//            return versionFile
//        }
//
//        fun getSysInfoFile(logLocation: File): File {
//            return File(logLocation.toString() + File.separator + APP_INFO_FILE_NAME)
//        }
//
//        private fun generateDeviceInfo(writer: PrintWriter, context: Context, feedbackViewModel: FeedbackViewModel) {
//            writer.println("platform: Android")
//            writer.println("Teams Version: ${AppInfoUtils.getVersionFromPackage(context)}")
//            writer.println("WME version: ${feedbackViewModel.getWMEVersion()}")
//            writer.println("OS version: ${Build.VERSION.RELEASE}")
//            writer.println("Manufacturer: ${Build.MANUFACTURER}")
//            writer.println("Device model: ${Build.MODEL}")
//        }
//
//        private fun generateUserInfo(writer: PrintWriter, coreFramework: CoreFramework, feedbackViewModel: FeedbackViewModel/*, tokenProvider: ApiTokenProvider, deviceRegistration: DeviceRegistration, diagnosticManager: DiagnosticManager*/) {
//            var deviceId = feedbackViewModel.getDeviceID()
//            var deviceUrl = feedbackViewModel.getDeviceUrl()
//            var mercuryUri = "TBD"
//            var clientSecurityPolicy = "TBD"
//            var dynamicLoggingPIN = "TBD"
//
//            writer.println("User Information:")
//            writer.println("-----------------")
//            writer.println("Installation ID: ${coreFramework.getInstallationId()}")
//            writer.println("UUID           : ${feedbackViewModel.getContactId()}")
//            writer.println("Device ID      : $deviceId")
//            writer.println("Device URL     : $deviceUrl")
//            writer.println("Mercury URI    : $mercuryUri")
//            writer.println("Security Policy: $clientSecurityPolicy")
//            writer.println("Server PIN     : $dynamicLoggingPIN")
//            writer.println("")
//            writer.println("Customer/Partner branding:")
//            writer.println("-----------------")
//
//            val brandingInfo = feedbackViewModel.getBrandingInfo()
//
//            writer.println("Show support text          : TBD")
//            writer.println("Reporting site             : ${brandingInfo.reportingSiteUrl}")
//            writer.println("Custom help site           : ${brandingInfo.helpUrl}")
//
//            writer.println("Partner company            : ${brandingInfo.partnerCompanyName}")
//            writer.println("Partner logo url           : ${brandingInfo.partnerLogoUrl}")
//
//            writer.println("Customer name              : ${brandingInfo.customerCompanyName}")
//            writer.println("Customer logo url          : ${brandingInfo.customerLogoUrl}")
//
//            writer.println("Support Provider name      : ${brandingInfo.supportProviderCompanyName}")
//            writer.println("Support provider logo url  : ${brandingInfo.supportProviderLogoUrl}")
//
//            writer.println("")
//        }
//
//        private fun generateAppInfo(writer: PrintWriter, logLocation: File, context: Context, feedbackViewModel: FeedbackViewModel) {
//            try {
//                val packageName = context.packageName
//                val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
//                val processId = getAppProcessId(context, packageInfo.applicationInfo.processName)
//
//                val applicationInfo: ApplicationInfo = context.packageManager.getApplicationInfo(packageName, 0)
//                val appDir = applicationInfo.publicSourceDir
//                val dateFormat = DateUtils.buildIso8601Format()
//
//                val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
//                var batteryOptimization = "Enabled"
//
//                if (powerManager.isIgnoringBatteryOptimizations(packageName)) {
//                    batteryOptimization = "Disabled"
//                }
//
//                writer.println("Application Information:")
//                writer.println("------------------------")
//                writer.println("Tracking Id base     : TBD")
//                writer.println("Package name         : $packageName")
//                writer.println("Process name         : ${packageInfo.applicationInfo.processName}")
//                writer.println("Process ID           : ${Integer.toString(processId)}")
//                writer.println("Version              : ${packageInfo.versionName}")
//                writer.println("WME version          : ${feedbackViewModel.getWMEVersion()}")
//                writer.println("Commit tag           : ${BuildConfig.GIT_COMMIT_SHA}")
//                writer.println("Installed            : ${dateFormat.format(packageInfo.firstInstallTime)}")
//                writer.println("Last update          : ${dateFormat.format(packageInfo.lastUpdateTime)}")
//                writer.println("In work profile      : " + if (IntentUtils.getAppInWorkProfile(context)) "Yes" else "No")
//                writer.println("Log location         : $logLocation")
//                writer.println("Content loc          : TBD")
//                writer.println("Memory usage         : ${getProcessMemoryInfo(context, processId)}")
//                writer.println("Apk Size             : ${sizeToString(File(appDir).length().toDouble())}")
//                writer.println("Disk usage           : ${getAppStorageInfo(context)}")
//                writer.println("Battery Optimization : $batteryOptimization")
//                writer.println("")
//            } catch (e: PackageManager.NameNotFoundException) {
//                TeamsLogger.error(FEEDBACK_TAG, e, "Error getting package info.")
//            } catch (e: SecurityException) {
//                TeamsLogger.error(FEEDBACK_TAG, e, "No permission getting the file info.")
//            }
//        }
//
//        private fun generateSystemInfo(writer: PrintWriter, context: Context) {
//            writer.println("System Information:")
//            writer.println("-------------------")
//            writer.println("Android version    : ${Build.VERSION.RELEASE}")
//            writer.println("Android build      : ${Build.DISPLAY}")
//            writer.println("Android SDK version: ${Build.VERSION.SDK_INT}")
//            writer.println("Manufacturer       : ${Build.MANUFACTURER}")
//            writer.println("Device model       : ${Build.MODEL}")
//            writer.println("Device time        : ${getDeviceTimeInfo()}")
//            writer.println("Device timezone    : ${TimeZone.getDefault().getDisplayName(Locale.US)}")
//            writer.println("Application locale : ${AppInfoUtils.getOsLanguage()}")
//            writer.println("CPU instruction set: ${NativeUtilities.getCurrentABI()}")
//            writer.println("Supported ABIs     : ${getSupportedABIs()}")
//            writer.println("Number of cores    : ${Runtime.getRuntime().availableProcessors()}")
//            writer.println("Has lockscreen     : " + if (hasLockScreen(context)) "Yes" else "No")
//            writer.println("isSecureDevice     : " + if (isDeviceSecure(context)) "Yes" else "No")
//            writer.println("isKeyguardSecure   : " + if (isKeyguardSecure(context)) "Yes" else "No")
//
//            writer.println("Network status     : ${getNetworkStatus(context)}")
//
//            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//            writer.println(
//                String.format(
//                    "VoiceCall Volume   : %s(%s)",
//                    audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL),
//                    audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)
//                )
//            )
//            writer.println(
//                String.format(
//                    "System Volume      : %s(%s)",
//                    audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM),
//                    audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)
//                )
//            )
//            writer.println(
//                String.format(
//                    "Music Volume       : %s(%s)",
//                    audioManager.getStreamVolume(AudioManager.STREAM_MUSIC),
//                    audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
//                )
//            )
//            writer.println(
//                String.format(
//                    "Bluetooth Volume   : %s(%s)",
//                    audioManager.getStreamVolume(STREAM_BLUETOOTH_SCO),
//                    audioManager.getStreamMaxVolume(STREAM_BLUETOOTH_SCO)
//                )
//            )
//            writer.println("")
//        }
//
//        private fun generatePermissionsInfo(writer: PrintWriter, context: Context) {
//            writer.println("Permissions:")
//            writer.println("------------")
//            val permissionList = getPermissionFromManifest(context)?.asList()?.sorted()
//            permissionList?.let {
//                for (permission in permissionList) {
//                    printPermission(writer, context, permission)
//                }
//            }
//            writer.println("")
//        }
//
//        private fun printPermission(writer: PrintWriter, context: Context, permission: String) {
//            val hasPermission = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
//            writer.println(String.format(Locale.US, "%-45s: %s", permission, hasPermission))
//        }
//
//        private fun getPermissionFromManifest(context: Context): Array<out String>? {
//            return context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS).requestedPermissions
//        }
//
//        operator fun JSONArray.iterator(): Iterator<JSONObject> =
//            (0 until length()).asSequence().map { get(it) as JSONObject }.iterator()
//
//        private fun generateUserSettingsInfo(writer: PrintWriter, settings: Settings, feedbackViewModel: FeedbackViewModel) {
//            writer.println("User settings:")
//            writer.println("--------------")
//            writer.println(String.format("Share Location Toggle: TBD (feature enabled = TBD)"))
//            writer.println(String.format("Proximity Disabled:  ${!settings.isProximityEnabled}"))
//            writer.println("")
//            val featureSettings: MutableList<FeatureSetting>? = feedbackViewModel.getAllFeatureSettings()
//
//            featureSettings?.sortBy { it.mName }
//            for (type in SettingType.values()) {
//                val settingGroup = featureSettings?.filter { featureSettingsFilterFunction(it, type) }
//
//                if (!settingGroup.isNullOrEmpty()) {
//                    writer.println("  ${type.name}")
//                    writer.println("  -------------")
//                    for (item in settingGroup.indices) {
//                        writer.println("  ${settingGroup[item].mName} : ${settingGroup[item].mValue}")
//                    }
//                    writer.println("")
//                }
//            }
//
//            writer.println("")
//        }
//
//        private fun generateNotificationsInfo(writer: PrintWriter, context: Context, feedbackViewModel: FeedbackViewModel) {
//            writer.println("---------------------------")
//            writer.println("START NOTIFICATION SETTINGS\n")
//
//            val notificationManager: NotificationManager? = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            if (notificationManager == null) {
//                writer.println("NotificationManager was null\n")
//                return
//            }
//
//            // //////////////////////
//            // OS level settings
//
//            // Did the user turn off notifications for the app?
//            if (notificationManager.areNotificationsEnabled()) {
//                writer.println("App notifications enabled : True")
//            } else {
//                writer.println("App notifications enabled : False <<<<< USER TURNED OFF NOTIFICATIONS FOR THE APP AT OS LEVEL <<")
//            }
//
//            // Get info from OS level starting at 'group' level
//            if (OSUtils.hasPie()) {
//                val groupIds = arrayOf(
//                    NotificationChannelGroups.MESSAGES.groupId, NotificationChannelGroups.CALLS.groupId,
//                    NotificationChannelGroups.MEETINGS.groupId, NotificationChannelGroups.GENERAL.groupId
//                )
//                try {
//                    groupIds.forEach { groupId ->
//                        captureNotificationGroup(writer, groupId, notificationManager, feedbackViewModel)
//                    }
//                } catch (e: Exception) {
//                    writer.println("Exception while trying to capture channel group settings: $e")
//                }
//            } else if (OSUtils.hasOreo()) {
//                // We can still try to get the channel info on it's own
//                feedbackViewModel.getAppNotificationChannelsInfo()?.forEach { channel ->
//                    captureNotificationChannel(writer, channel)
//                }
//            } else {
//                writer.println("API level is less than Oreo. Unable to get further info from OS.")
//            }
//
//            // //////////////////////
//            // App level settings
//
//            // These are settings specific to our app, via user toggles
//            // Note: Per space notification settings would have to be retrieved one by one for every space via conversation service. Not currently implemented
//            val featureSettings: MutableList<FeatureSetting>? = feedbackViewModel.getAllFeatureSettings()
//
//            // This refers to the 'All Messages' radio button.
//            // 'True' means "I want all Webex message notifications". Can still be overridden per space.
//            val allMessages = featureSettings?.find { it.mName.compareTo(USER_TOGGLE_GROUP_MESSAGE_NOTIFICATIONS, true) == 0 }
//            var lastModifiedDate: String = getFeatureSettingLastModifiedDate(featureSettings, USER_TOGGLE_GROUP_MESSAGE_NOTIFICATIONS)
//            if (allMessages != null) {
//                writer.println(String.format("All Messages: ${allMessages.mValue} ($lastModifiedDate)"))
//            } else {
//                writer.println(String.format("All Messages: null"))
//            }
//
//            // This is the '@Mentions only' radio button.
//            // 'True' means should only be getting notifications if user was mentioned or @all
//            val mentionsOnly = featureSettings?.find { it.mName.compareTo(USER_TOGGLE_MENTION_NOTIFICATIONS, true) == 0 }
//            lastModifiedDate = getFeatureSettingLastModifiedDate(featureSettings, USER_TOGGLE_MENTION_NOTIFICATIONS)
//            if (mentionsOnly != null) {
//                writer.println(String.format("@Mentions only: ${mentionsOnly.mValue} ($lastModifiedDate)"))
//            } else {
//                writer.println(String.format("@Mentions only: null"))
//            }
//
//            // If both 'all messages' and 'mentions only' are 'False', then essentially notifications have been turned off by user selecting 'Off' radio button.
//            // (Will still get 1:1 by design). This case should be noted.
//            if (allMessages != null && mentionsOnly != null && allMessages.mValue.lowercase(Locale.US) == "false" && mentionsOnly.mValue.lowercase(Locale.US) == "false") {
//                writer.println(" <<<<<<<<<<<<<<<<<<  Webex message notifications are turned off <<<<< ")
//            }
//            writer.println("")
//
//            writer.println("END NOTIFICATION SETTINGS\n-------------------------\n")
//        }
//
//        @RequiresApi(Build.VERSION_CODES.P)
//        private fun captureNotificationGroup(writer: PrintWriter, groupID: String, notificationManager: NotificationManager, feedbackViewModel: FeedbackViewModel) {
//            val group = notificationManager.getNotificationChannelGroup(groupID)
//            var groupEnabledInfo: String
//            if (group.isBlocked) {
//                groupEnabledInfo = "  Enabled      : False  <<<<<< ENTIRE GROUP IS MUTED <<"
//            } else {
//                groupEnabledInfo = "  Enabled      : True"
//            }
//
//            writer.println(
//                "\n----------\nGroup Name : ${group.name}\n$groupEnabledInfo\n"
//            )
//
//            var anyOneChannelImportanceChanged = false
//            // Drill down into each group
//            val allChannels = feedbackViewModel.getAppNotificationChannelsInfo()
//            // Scope to channels just for this group
//            val channelsForGroup = allChannels?.filter { it.group == groupID }
//            channelsForGroup?.forEach {
//                anyOneChannelImportanceChanged = captureNotificationChannel(writer, it) || anyOneChannelImportanceChanged
//            }
//
//            if (anyOneChannelImportanceChanged) {
//                writer.println(" <<<<<< Some channel's importance was changed in this last group by the user.  It may have been put back to original.  <<")
//            }
//        }
//
//        @RequiresApi(Build.VERSION_CODES.O)
//        private fun captureNotificationChannel(writer: PrintWriter, notificationChannel: NotificationChannel): Boolean {
//
//            var anyOneChannelImportanceChanged = false
//            var importanceExtraCallout = ""
//            if (notificationChannel.importance == 0) {
//                importanceExtraCallout = " <<<<< CHANNEL IS MUTED <<"
//            }
//            writer.println(
//                "Channel Group long name  : ${notificationChannel.group}\n" +
//                    "Channel Name             : ${notificationChannel.name}\n" +
//                    "Channel Id               : ${notificationChannel.id}\n" +
//                    " Importance level        : ${notificationChannel.importance} $importanceExtraCallout\n" +
//                    " Sound                   : ${notificationChannel.sound}\n" +
//                    " Vibrate                 : ${notificationChannel.shouldVibrate()}\n" +
//                    " Lockscreen visibility   : ${notificationChannel.lockscreenVisibility}\n" +
//                    " Can show badge          : ${notificationChannel.canShowBadge()}\n" +
//                    " Can bypass DND          : ${notificationChannel.canBypassDnd()}"
//            )
//
//            if (OSUtils.hasAndroid10()) {
//                importanceExtraCallout = ""
//                if (notificationChannel.hasUserSetImportance()) {
//                    anyOneChannelImportanceChanged = true
//                    importanceExtraCallout = " <<<<< USER CHANGED IMPORTANCE LEVEL AT SOME POINT <<"
//                }
//                writer.println(
//                    " User changed importance : ${notificationChannel.hasUserSetImportance()} $importanceExtraCallout\n" +
//                        " Can bubble              : ${notificationChannel.canBubble()}"
//                )
//
//                val audioAttributes = notificationChannel.audioAttributes
//                audioAttributes?.let {
//                    writer.println(
//                        " Audio Attributes\n" +
//                            " Haptics muted : ${audioAttributes.areHapticChannelsMuted()}\n" +
//                            " Flags         : ${audioAttributes.flags}"
//                    )
//                }
//            }
//
//            writer.println("")
//            return anyOneChannelImportanceChanged
//        }
//
//        private fun featureSettingsFilterFunction(featureSetting: FeatureSetting, type: SettingType): Boolean {
//            val allowList = arrayListOf("client-product-mode")
//            return (featureSetting.mSettingType == type && (parseBoolean(featureSetting.mValue) || (featureSetting.mName in allowList)))
//        }
//
//        private fun getFeatureSettingLastModifiedDate(featureSettings: MutableList<FeatureSetting>?, featureName: String): String {
//            val featureSettingsLastModified = featureSettings?.find { it.mName.compareTo("devices", true) == 0 }
//
//            var json: JSONArray = JSONArray()
//            try {
//                if (featureSettingsLastModified != null)
//                    json = JSONObject(featureSettingsLastModified.mValue).getJSONObject("features").getJSONArray("user")
//            } catch (e: Exception) {
//                TeamsLogger.warn(tag = FEEDBACK_TAG, msg = "${e.message}")
//            }
//
//            var lastModifiedDate: String = "null"
//            try {
//                for (item in json) {
//                    if (item.getString("key").compareTo(featureName) == 0) {
//                        lastModifiedDate = item.getString("lastModified")
//                    }
//                }
//            } catch (e: Exception) {
//                TeamsLogger.warn(tag = FEEDBACK_TAG, msg = "${e.message}")
//            }
//            return lastModifiedDate
//        }
//
//        private fun getDeviceTimeInfo(): String {
//            val dateFormat = DateUtils.buildIso8601Format()
//            return dateFormat.format(Date())
//        }
//
//        @Suppress("Deprecation")
//        private fun getNetworkStatus(context: Context): String {
//            var networkStatus: String = "unknown"
//
//            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            val activeNetworkInfo = connectivityManager.activeNetworkInfo
//
//            activeNetworkInfo?.let { networkInfo ->
//                // sdk level >= 28
//                if (OSUtils.hasPie()) {
//                    val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
//                    if (networkCapabilities != null) {
//                        val typeName = if (networkCapabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI)) "WIFI"
//                        else (if (networkCapabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR)) "CELLULAR" else "unknown")
//                        if (networkInfo.isConnected) {
//                            networkStatus = "connected ($typeName)"
//                        } else {
//                            networkStatus = "no connection (state = ${networkInfo.detailedState}"
//                        }
//                    } else {
//                        networkStatus = "failed getting active network information"
//                    }
//                    // sdk level < 28
//                } else {
//                    networkStatus = when (networkInfo.isConnectedOrConnecting) {
//                        true -> "connected (${networkInfo.typeName})"
//                        false -> "no connection (state = ${networkInfo.detailedState}"
//                    }
//                }
//            } ?: run {
//                networkStatus = "failed getting active network information"
//            }
//
//            return networkStatus
//        }
//
//        fun isDeviceSecure(context: Context): Boolean {
//            val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
//            return keyguardManager.isDeviceSecure
//        }
//
//        fun supportsBiometricAuthentication(context: Context): Boolean {
//            val canAuthenticate = BiometricManager.from(context).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
//            TeamsLogger.info(SYSTEM_INFO_TAG, msg = "canAuthenticate: $canAuthenticate")
//
//            return when (canAuthenticate) {
//                BiometricManager.BIOMETRIC_SUCCESS,
//                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE, // The hardware is currently unavailable.
//                BiometricManager.BIOMETRIC_STATUS_UNKNOWN, // According to canAuthenticateWithFingerprintOrUnknownBiometric(), if there is no biometrics enrolled, the status will be BIOMETRIC_STATUS_UNKNOWN
//                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> true
//                else -> false
//            }
//        }
//
//        fun isKeyguardSecure(context: Context): Boolean {
//            val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
//            return keyguardManager.isKeyguardSecure
//        }
//
//        fun hasLockScreen(context: Context): Boolean {
//            return isDeviceSecure(context)
//        }
//
//        fun isScreenLocked(context: Context): Boolean {
//            return hasLockScreen(context) && (context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager).isKeyguardLocked
//        }
//
//        private fun getAppProcessId(context: Context, processName: String): Int {
//            var processId = 0
//
//            val activityManager: ActivityManager? = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//            val processList: List<ActivityManager.RunningAppProcessInfo>? = activityManager?.runningAppProcesses
//
//            processList?.let { processes ->
//                for (currentProcess in processes) {
//                    if (currentProcess.processName == processName) {
//                        processId = currentProcess.pid
//                        break
//                    }
//                }
//                return processId
//            }
//            return -1
//        }
//
//        private fun getProcessMemoryInfo(context: Context, processId: Int): String {
//            var result: String
//            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//
//            if (processId > 0) {
//                val pids = intArrayOf(processId)
//                val mi = activityManager.getProcessMemoryInfo(pids)
//                if (mi != null && mi.size == 1) {
//                    result = "total PSS = ${DecimalFormat("#,###.##").format(mi[0].totalPss.toLong())} kB, "
//                    result += "total shared dirty = ${DecimalFormat("#,###.##").format(mi[0].totalPrivateDirty.toLong())} kB, "
//                    result += "total private dirty = ${DecimalFormat("#,###.##").format(mi[0].totalSharedDirty.toLong())} kB"
//                } else {
//                    result = "error retrieving memory information"
//                }
//            } else {
//                result = "invalid process ID specified (${Integer.toString(processId)})"
//            }
//
//            return result
//        }
//
//        private fun getAppStorageInfo(context: Context): String {
//            // Get the parent folders for where internal and external "files" would be and
//            // count size of all data under those folders
//            val intFileSize = getFolderSize(context.filesDir?.parentFile).toDouble()
//            val extFileSize = getFolderSize((context.getExternalFilesDir(null))?.parentFile).toDouble()
//            val intMB = sizeToString(intFileSize)
//            val extMB = sizeToString(extFileSize)
//            return String.format("internal = $intMB, external = $extMB")
//        }
//
//        private fun sizeToString(fileSize: Double): String {
//            try {
//                return if (fileSize > 0) {
//                    DecimalFormat("#,###.##").format((fileSize / ONE_MEGABYTE)).toString() + " MB"
//                } else {
//                    "0 MB"
//                }
//            } catch (e: Exception) {
//                TeamsLogger.warn(FEEDBACK_TAG, "Cannot determinate file size : ${e.message}")
//            }
//            return "unknown"
//        }
//
//        private fun getFolderSize(directory: File?): Long {
//            var length: Long = 0
//            directory?.let {
//                if (it.isDirectory) {
//                    it.listFiles()?.forEach { file ->
//                        length += if (file.isFile)
//                            file.length()
//                        else
//                            getFolderSize(file)
//                    }
//                }
//            }
//            return length
//        }
//
//        fun getSystemProp(key: String, default: String? = null): String? {
//            try {
//                val c = Class.forName("android.os.SystemProperties")
//
//                val method = c.getDeclaredMethod("get", String::class.java)
//
//                val value = method.invoke(null, key) as String
//
//                return if (value.isBlank()) {
//                    default
//                } else {
//                    value
//                }
//            } catch (e: Exception) {
//                TeamsLogger.error(FEEDBACK_TAG, e, "Caught exception")
//            }
//
//            return default
//        }
//
//        fun getSystemPropBoolean(key: String, default: Boolean): Boolean {
//            try {
//                val c = Class.forName("android.os.SystemProperties")
//
//                val method = c.getDeclaredMethod("getBoolean", String::class.java, Boolean::class.java)
//
//                return method.invoke(null, key, default) as Boolean
//            } catch (e: Exception) {
//                TeamsLogger.error(FEEDBACK_TAG, e, "Caught exception")
//            }
//
//            return default
//        }
//
//        fun getSupportedABIs(): String {
//            return Arrays.toString(Build.SUPPORTED_ABIS)
//        }
//    }
//}
