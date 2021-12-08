package com.webex.teams.logging

import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.LogcatLogStrategy

/**
 *
 * WBX_TEAMS: Log message
 *     OR
 * WBX_TEAMS: Tag: Log message
 *
</pre> *
 */
class SimpleFormatStrategy(tag: String = "") : FormatStrategy {
    private var logStrategy = LogcatLogStrategy()
    private var globalTag = ""

    init {
        globalTag = tag
    }

    override fun log(priority: Int, tag: String?, message: String) {
        var mergedTag = globalTag
        if (!tag.isNullOrEmpty()) {
            mergedTag = globalTag + ": " + tag
        }
        logStrategy.log(priority, mergedTag, message)
    }
}
