package com.example.composes.breakout.repo

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.composes.R
import java.util.*

data class BreakoutSession(
    val name: String,
    val id: UUID = UUID.randomUUID()
)
