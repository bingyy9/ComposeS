package com.example.composes.breakout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.webex.teams.ui.calls.FullScreenDialogFragment

class BreakoutSessionBroadcastFragment : FullScreenDialogFragment() {

    private val breakoutViewModel by viewModels<BreakoutViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                ContentView()
            }
        }
    }

    fun onCancel(){

    }

    @Composable
    fun ContentView(){
        BroadcastView(
            breakoutViewModel.broadcastSessionsList,
            breakoutViewModel.broadcastGroupList,
            breakoutViewModel::onSendBroadcast,
            onCancel = this::onCancel
        )
    }

    companion object {
        const val DIALOG_TAG = "breakout_session_broadcast_fragment"

        fun newInstance(): BreakoutSessionBroadcastFragment {
            return BreakoutSessionBroadcastFragment()
        }
    }
}
