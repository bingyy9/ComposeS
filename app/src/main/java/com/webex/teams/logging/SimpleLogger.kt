package com.webex.teams.logging

import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

open class SimpleLogger() {

    init {
        val formatStrategy = SimpleFormatStrategy("WBX_TEAMS")
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    private fun getFormattedMsg(msg: String): String {
        val fileMethodNameWithLineNumber = getFileMethodNameWithLineNumber()
        var msgWithFileNameLineNumber = "$fileMethodNameWithLineNumber"
        if (!msg.isNullOrBlank() && !fileMethodNameWithLineNumber.isNullOrBlank()) {
            msgWithFileNameLineNumber = "$fileMethodNameWithLineNumber:$msg"
        }
        return msgWithFileNameLineNumber
    }

    private fun getFormattedTag(tag: String): String {
        var formattedTag = ""
        if (!tag.isNullOrBlank()) {
            val tags = tag.split(" ")
            if (tags.size > 1) {
                var tagString = ""
                for (tg in tags) {
                    tagString += "[$tg]"
                }
                formattedTag = tagString
            } else {
                formattedTag = "[$tag]"
            }
        }
        return formattedTag
    }

    private fun getFileMethodNameWithLineNumber(): String {
        return Thread.currentThread().let {
            it.stackTrace.let {
                it[6].let {
                    it.fileName + "::" + it.lineNumber + " " + it.methodName
                }
            }
        }
    }

    private fun getResourceEntryName(context: Context, currentDestinationId: Int): String {
        return context.resources?.getResourceEntryName(currentDestinationId) ?: ""
    }

    fun info(tag: String = "", msg: String) {
        Logger.i("${getFormattedTag(tag)} ${getFormattedMsg(msg)}")
    }

    fun debug(tag: String = "", msg: String) {
        Logger.d("${getFormattedTag(tag)} ${getFormattedMsg(msg)}")
    }

    fun debug(tag: String = "", context: Context, currentFragmentId: Int, destFragmentId: Int, msg: String) {
        val updatedMessage = "(${getResourceEntryName(context, currentFragmentId)} --> ${getResourceEntryName(context, destFragmentId)}), $msg"
        Logger.d("${getFormattedTag(tag)} ${getFormattedMsg(updatedMessage)}")
    }

    fun error(tag: String = "", throwable: Throwable? = null, msg: String) {
        Logger.e(throwable, "${getFormattedTag(tag)} ${getFormattedMsg(msg)}")
    }

    fun warn(tag: String = "", msg: String) {
        Logger.w("${getFormattedTag(tag)} ${getFormattedMsg(msg)}")
    }

    fun verbose(tag: String = "", msg: String) {
        Logger.v("${getFormattedTag(tag)} ${getFormattedMsg(msg)}")
    }

    fun wtf(tag: String = "", msg: String) {
        Logger.wtf("${getFormattedTag(tag)} ${getFormattedMsg(msg)}")
    }

    fun json(tag: String = "", json: String) {
        Logger.json("${getFormattedTag(tag)} ${getFormattedMsg(json)}")
    }

    fun xml(tag: String = "", xml: String) {
        Logger.xml("${getFormattedTag(tag)} ${getFormattedMsg(xml)}")
    }
}
