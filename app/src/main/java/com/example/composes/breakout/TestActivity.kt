package com.example.composes.breakout

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.fragment.app.FragmentManager
import com.cisco.webex.teams.utils.LoggingTags.CALLING_TAG
import com.example.composes.breakout.anyonecanjoin.BreakoutSessionJoinComponent
import com.example.composes.breakout.anyonecanjoin.BreakoutSessionToJoinViewModel
import com.example.composes.breakout.assign.BreakoutSessionAssignConfigFragment
import com.example.composes.breakout.broadcast.BreakoutSessionBroadcastFragment
import com.example.composes.breakout.broadcast.BreakoutSessionBroadcastViewModel
import com.example.composes.ui.theme.ComposeSTheme
import com.webex.teams.OSUtils
import com.webex.teams.logging.TeamsLogger

class TestActivity : AppCompatActivity() {
    private val breakoutViewModel by viewModels<BreakoutSessionBroadcastViewModel>()
    val breakoutJoinViewModel : BreakoutSessionToJoinViewModel by viewModels()
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeSTheme {
                ContentView()
            }
        }
    }

    @ExperimentalComposeUiApi
    @Composable
    fun ContentView(){
        val openDialog = remember { mutableStateOf(false) }
        val onShowJoinBreakoutViewClick:()->Unit = {
            openDialog.value = true
        }
        val onBackPressed:()->Unit = {
            openDialog.value = false
        }
        val onPopUp:()->Unit = {
            openDialog.value = false
        }
        val onClose:()->Unit = {
            openDialog.value = false
        }

        if (openDialog.value) {
            Dialog(onDismissRequest = { openDialog.value = false }, properties = DialogProperties(usePlatformDefaultWidth = false),) {
                Card(
                    backgroundColor = MaterialTheme.colors.background
                ) {
                    BreakoutSessionJoinComponent().SessionToJoinPanel(breakoutSessionToJoinViewModel = breakoutJoinViewModel, onBackPressed, onPopUp, onClose)
                }
            }
        }

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

            Button(
                onClick = { onShowJoinBreakoutViewClick() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White,
                    disabledBackgroundColor = Color.Black,
                    disabledContentColor = Color.Black
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Join breakout view",
                    fontSize = 16.sp,
                    modifier = Modifier.background(Color.Black),
                )
            }

            Button(
                onClick = { showBreakoutSessionAssignConfig() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White,
                    disabledBackgroundColor = Color.Black,
                    disabledContentColor = Color.Black
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Broadcast Assign Config",
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

    fun showBreakoutSessionAssignConfig(){
        val isDualPane = OSUtils.isDualPane(this)
        TeamsLogger.info(CALLING_TAG, "isDualPane $isDualPane")
        var fragmentManager: FragmentManager = this.supportFragmentManager
//        if (isDualPane) {
        BreakoutSessionAssignConfigFragment.newInstance().show(fragmentManager, BreakoutSessionAssignConfigFragment.DIALOG_TAG)
//        } else {
////            startNavigation(NavigationTo.CallStatistics)
//        }
    }
}
