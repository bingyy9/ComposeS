package com.example.composes.breakout.anyonecanjoin.repo

import com.example.composed.bean.BoSessionInfo
import com.example.composed.bean.BreakoutUser

object SampleData {
    const val ROLE_GUEST = 0
    const val ROLE_PANELIST = 1
    const val ROLE_COHOST = 2
    const val ROLE_HOST = 3
    const val ROLE_PRESENTER = 4

    const val USER_TYPE_DEVICE = 1
    const val USER_TYPE_NORMAL = 0

    val breakoutSessions = listOf(
        BoSessionInfo(
            listOf(BreakoutUser("user name", ROLE_GUEST, USER_TYPE_NORMAL, null)),
            "breakout session1",
            "UUID_1",
            3,
            4
        ),
        BoSessionInfo(
            listOf(BreakoutUser("user name", ROLE_PANELIST, USER_TYPE_NORMAL, null)),
            "breakout session2",
            "UUID_2",
            3,
            4
        ),
        BoSessionInfo(
            listOf(BreakoutUser("user name", ROLE_COHOST, USER_TYPE_NORMAL, null)),
            "breakout session3",
            "UUID_3",
            3,
            4
        ),
        BoSessionInfo(
            listOf(BreakoutUser("Device name", ROLE_HOST, USER_TYPE_DEVICE, listOf(BreakoutUser("user paired device", ROLE_PRESENTER, USER_TYPE_NORMAL, null)))),
            "breakout session4",
            "UUID_4",
            3,
            4
        ),
    )
}