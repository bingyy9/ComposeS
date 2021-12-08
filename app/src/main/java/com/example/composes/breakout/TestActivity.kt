package com.example.composes.breakout

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentManager
import com.cisco.webex.teams.utils.LoggingTags.CALLING_TAG
import com.example.composes.breakout.broadcast.BreakoutSessionBroadcastFragment
import com.example.composes.breakout.broadcast.BreakoutSessionBroadcastViewModel
import com.example.composes.ui.theme.ComposeSTheme
import com.webex.teams.OSUtils
import com.webex.teams.logging.TeamsLogger

class TestActivity : AppCompatActivity() {
    private val breakoutViewModel by viewModels<BreakoutSessionBroadcastViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSTheme {
                ContentView()
            }
        }
    }

    @Composable
    fun ContentView(){
        Column() {
            Button(
                onClick = { showBroadcast() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White,
                    disabledBackgroundColor = Color.Black,
                    disabledContentColor = Color.Black
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Broadcast",
                    fontSize = 16.sp,
                    modifier = Modifier.background(Color.Black),
                )
            }
        }
    }

    fun showBroadcast(){
        val isDualPane = OSUtils.isDualPane(this)
        TeamsLogger.info(CALLING_TAG, "isDualPane $isDualPane")
        var fragmentManager: FragmentManager = this.supportFragmentManager
//        if (isDualPane) {
            BreakoutSessionBroadcastFragment.newInstance().show(fragmentManager, BreakoutSessionBroadcastFragment.DIALOG_TAG)
//        } else {
////            startNavigation(NavigationTo.CallStatistics)
//        }
    }
}
