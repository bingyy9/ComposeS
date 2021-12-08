package com.webex.teams.logging

import android.content.Context

object TeamsLogger {
    var isTraceLoggingEnabled = false
    private val teamsLogger = SimpleLogger()

    fun info(tag: String, msg: String) {
        try {
            teamsLogger.info(tag, msg)
        } catch (e: Exception) { }
    }

    // Use trace logs if logs are relevant only during debugging or in debug builds.
    fun trace(tag: String, msg: String) {
        try {
            if (isTraceLoggingEnabled) {
                teamsLogger.debug(tag, msg)
            }
        } catch (e: Exception) { }
    }

    fun debug(tag: String = "", context: Context, currentFragmentId: Int, destFragmentId: Int, msg: String) {
        try {
            teamsLogger.debug(tag, context, currentFragmentId, destFragmentId, msg)
        } catch (e: Exception) { }
    }

    fun debug(context: Context, currentFragmentId: Int, destFragmentId: Int, msg: String) {
        try {
            teamsLogger.debug("", context, currentFragmentId, destFragmentId, msg)
        } catch (e: Exception) { }
    }

    fun debug(tag: String = "", msg: String) {
        try {
            teamsLogger.debug(tag, msg)
        } catch (e: Exception) { }
    }

    fun error(tag: String, throwable: Throwable? = null, msg: String) {
        try {
            teamsLogger.error(tag, throwable, msg)
        } catch (e: Exception) { }
    }

    fun warn(tag: String, msg: String) {
        try {
            teamsLogger.warn(tag, msg)
        } catch (e: Exception) { }
    }

    fun verbose(tag: String = "", msg: String) {
        try {
            teamsLogger.verbose(tag, msg)
        } catch (e: Exception) { }
    }

    fun wtf(tag: String, msg: String) {
        try {
            teamsLogger.wtf(tag, msg)
        } catch (e: Exception) { }
    }

    fun json(tag: String, json: String) {
        try {
            teamsLogger.json(tag, json)
        } catch (e: Exception) { }
    }

    fun xml(tag: String = "", xml: String) {
        try {
            teamsLogger.xml(tag, xml)
        } catch (e: Exception) { }
    }

    @JvmStatic // Used by LogHelper.java
    fun traceLoggingEnabled(): Boolean = isTraceLoggingEnabled
}
