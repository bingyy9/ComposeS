package com.example.composes.breakout.assign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.webex.teams.ui.calls.FullScreenDialogFragment

class BreakoutSessionAssignConfigFragment : FullScreenDialogFragment() {

    private val breakoutAssignViewModel by viewModels<BreakoutSessionAssignViewModel>()

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


    @Composable
    fun ContentView(){
        BreakoutSessionAssignConfigComponent(
            breakoutAssignViewModel.assignType,
            breakoutAssignViewModel:: onAssignTypeSelected,
            breakoutAssignViewModel.waitingAssignUserCount,
            breakoutAssignViewModel::onCreateBreakoutSession,
            this::showDetailAssign,
            this::onCancel
        )
    }

    private fun showDetailAssign(){

    }

    private fun onCancel(){
        dismiss()
    }

    companion object {
        const val DIALOG_TAG = "breakout_session_assign_config_fragment"

        fun newInstance(): BreakoutSessionAssignConfigFragment {
            return BreakoutSessionAssignConfigFragment()
        }
    }
}
