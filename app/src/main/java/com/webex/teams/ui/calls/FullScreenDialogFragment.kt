package com.webex.teams.ui.calls

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Size
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.webex.teams.util.UIUtils
import com.webex.teams.util.dp
import com.webex.teams.util.getMetricsHeightExtensionFunc
import com.webex.teams.util.getMetricsWidthExtensionFunc

open class FullScreenDialogFragment : DialogFragment() {

    /** The system calls this only when creating the layout in a dialog. */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        if (showsDialog && dialog != null) {
            val size = getDialogSize()
            dialog!!.window?.setLayout(size.width, size.height)
        }
    }

    private fun getDialogSize(): Size {
        val metrics = DisplayMetrics()
        val heightPixels = requireContext().getMetricsHeightExtensionFunc(metrics) - UIUtils.getNavigationBarHeight(requireContext()) - UIUtils.getStatusBarHeight(requireContext())
        val widthPixels = requireContext().getMetricsWidthExtensionFunc(metrics)
        val minSize = minOf(widthPixels, heightPixels)
        val newWidth = minSize * 3 / 4
        val newHeight = minSize - 24.dp
        return Size(newWidth, newHeight)
    }
}
