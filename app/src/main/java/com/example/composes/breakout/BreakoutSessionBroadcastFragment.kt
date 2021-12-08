package com.example.composes.breakout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.example.composes.breakout.repo.BroadcastType
import com.webex.teams.ui.calls.FullScreenDialogFragment

class BreakoutSessionBroadcastFragment : FullScreenDialogFragment() {

    private val breakoutViewModel by viewModels<BreakoutSessionBroadcastViewModel>()

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

    private fun onSendBroadcast(session: BroadcastType, group: BroadcastType, message: String) {
        dismiss()
        breakoutViewModel.onSendBroadcast(session, group, message)
    }

    private fun onCancel(){
        dismiss()
    }

    @Composable
    fun ContentView(){
        BroadcastView(
            breakoutViewModel.broadcastSessionsList,
            breakoutViewModel.broadcastGroupList,
            this::onSendBroadcast,
            this::onCancel
        )
    }

    companion object {
        const val DIALOG_TAG = "breakout_session_broadcast_fragment"

        fun newInstance(): BreakoutSessionBroadcastFragment {
            return BreakoutSessionBroadcastFragment()
        }
    }
}
