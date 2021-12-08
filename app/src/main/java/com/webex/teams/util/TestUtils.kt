package com.webex.teams.util

import android.os.Build
import com.cisco.webex.teams.utils.LoggingTags.UTILS_TAG
import com.webex.teams.logging.TeamsLogger

object TestUtils {
    private const val INSTRUMENTATION_TEST_CLASS = "com.webex.TeamsTest"
    private const val AUTOMATED_TEST_PROP = "debug.teams.is_automated_test"
    private const val AUTOMATED_UI_TEST_PROP = "debug.teams.is_automated_ui_test"
    private const val DUMPFILE_PROP = "debug.teams.generate_dump_file"
    private const val DUMPFILE_CRASH = "crash"
    private const val DUMPFILE_WRITE = "write"
    private const val FORCED_UI_MODE_PROP = "debug.teams.forced_ui_mode"
    private const val FORCED_UI_MODE_PHONE = "phone"
    private const val FORCED_UI_MODE_TABLET = "tablet"
    private const val USE_LOGINVIEWMODEL = "debug.teams.useloginviewmodel"
    var isInUT: Boolean = false
    private val useOldLoginInFlow = false//SystemInfoUtils.getSystemPropBoolean(USE_LOGINVIEWMODEL, false)
    private val forcedUIMode = ""//SystemInfoUtils.getSystemProp(FORCED_UI_MODE_PROP, "")
    private val dumpfile = ""//SystemInfoUtils.getSystemProp(DUMPFILE_PROP, "")
    private val automatedUi = false//SystemInfoUtils.getSystemPropBoolean(AUTOMATED_UI_TEST_PROP, false)
    private val automation = false//SystemInfoUtils.getSystemPropBoolean(AUTOMATED_TEST_PROP, false)
    private val instrumentation = try {
        Class.forName(INSTRUMENTATION_TEST_CLASS)
        !automation
    } catch (exception: Exception) {
        false
    }

    init {
        TeamsLogger.info(UTILS_TAG, "Forced UI mode: $forcedUIMode, Dumpfile type: $dumpfile, isAutomatedUi: $automatedUi, isAutomation: $automation, isInstrumentation: $instrumentation")
    }

    enum class DumpType {
        NONE,
        CRASH,
        WRITE
    }

    enum class ForcedUIMode {
        NONE,
        PHONE,
        TABLET
    }

    fun isTest(): Boolean = isUnitTest() || isUITest() || isAutomation()

    fun isInstrumentation(): Boolean = instrumentation

    // The property can be set before the app is launched as such:
    // adb shell setprop debug.teams.is_automated_test true
    fun isAutomation(): Boolean = automation

    fun isAutomatedUi(): Boolean = automatedUi

    fun isUnitTest(): Boolean = isInUT && !isAutomation()

    fun isUITest(): Boolean = isInstrumentation()

    fun isDumpFileEnabled(): DumpType = when (dumpfile) {
        DUMPFILE_CRASH -> {
            DumpType.CRASH
        }
        DUMPFILE_WRITE -> {
            DumpType.WRITE
        }
        else -> {
            DumpType.NONE
        }
    }

    fun getForcedUIMode(): ForcedUIMode = when (forcedUIMode) {
        FORCED_UI_MODE_PHONE -> {
            ForcedUIMode.PHONE
        }
        FORCED_UI_MODE_TABLET -> {
            ForcedUIMode.TABLET
        }
        else -> {
            ForcedUIMode.NONE
        }
    }

    fun isRobolectricUnitTest(): Boolean = "robolectric" == Build.FINGERPRINT

    fun useOldLoginInFlow(): Boolean = useOldLoginInFlow
}
