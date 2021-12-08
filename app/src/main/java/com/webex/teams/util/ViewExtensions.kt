//// Remove this comments when ViewUtils.java is ready to convert to ViewExtensions.kt
//@file:JvmName("ViewUtils")
//@file:Suppress("unused")
//
//package com.webex.teams.util
//
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.content.ActivityNotFoundException
//import android.content.Context
//import android.content.ContextWrapper
//import android.content.res.ColorStateList
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewGroup
//import android.view.ViewParent
//import android.view.animation.Animation
//import android.view.animation.RotateAnimation
//import android.view.inputmethod.EditorInfo
//import android.view.inputmethod.InputMethodManager
//import android.webkit.WebSettings
//import android.webkit.WebView
//import android.webkit.WebViewClient
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.activity.result.ActivityResultLauncher
//import androidx.annotation.DimenRes
//import androidx.annotation.Px
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.core.view.doOnLayout
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.findFragment
//import androidx.lifecycle.LifecycleOwner
//import androidx.navigation.NavDirections
//import androidx.navigation.findNavController
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.recyclerview.widget.SimpleItemAnimator
//import androidx.viewbinding.ViewBinding
//import com.cisco.spark.android.util.Toaster
//import com.webex.scf.commonhead.models.CallRecordingState
//import com.webex.scf.utils.CrashlyticsLogUtils
//import com.webex.teams.R
//import com.webex.teams.logging.TeamsLogger
//import com.webex.teams.ui.calls.incall.DoubleClickOnTouchListener
//import com.webex.teams.utils.LoggingTags
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.DelicateCoroutinesApi
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.ObsoleteCoroutinesApi
//import kotlinx.coroutines.channels.actor
//import kotlin.math.max
//import kotlin.math.min
//
//fun View.navigateTo(context: Context, destination: NavDirections) {
//    findNavController().navigateOrCatch(context, destination)
//}
//
//fun ViewBinding.navigateTo(context: Context, destination: NavDirections) {
//    this.root.navigateTo(context, destination)
//}
//
//fun <T> Fragment.requestActivityForResult(resultLauncher: ActivityResultLauncher<T>, input: T) {
//    try {
//        resultLauncher.launch(input)
//    } catch (e: Exception) {
//        Toaster.showLong(requireContext(), R.string.activity_launch_failure)
//        TeamsLogger.error(LoggingTags.APPLICATION_TAG, e, "startActivityForResult exception")
//
//        if (e !is ActivityNotFoundException) {
//            CrashlyticsLogUtils.logException(e, "Exception while requestActivityForResult")
//        }
//    }
//}
//
//fun Context.getActivity(): Activity? {
//    if (this is Activity) {
//        return this
//    } else if (this is ContextWrapper) {
//        return this.baseContext.getActivity()
//    }
//    return null
//}
//
//fun Context.requireActivity(): Activity {
//    if (this is Activity) {
//        return this
//    } else if (this is ContextWrapper) {
//        return this.baseContext.requireActivity()
//    }
//
//    throw IllegalArgumentException("This context is not a Activity or contains Activity, it maybe is a service, contentProvider or application, $this")
//}
//
//val View.activity get() = context.requireActivity()
//
//val View.lifecycleOwner: LifecycleOwner
//    get() {
//        return runCatching {
//            val fragment: Fragment = findFragment()
//            fragment.viewLifecycleOwner
//        }.onFailure {
//            TeamsLogger.warn(LoggingTags.UTILS_TAG, "can not find fragment, will try to find the activity")
//        }.getOrElse {
//            activity as LifecycleOwner
//        }
//    }
//
//fun <F : Fragment> View.findFragmentOrNull(): F? {
//    return runCatching {
//        val fragment: F = findFragment()
//        fragment
//    }.getOrNull()
//}
//
//fun ImageView.updateRecordingState(recordingState: CallRecordingState, isAudioOnlyMode: Boolean = false) {
//    when (recordingState) {
//        CallRecordingState.None -> {
//            this.visibility = View.GONE
//        }
//        CallRecordingState.Paused -> {
//            this.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.yellow_40, null))
//            this.visibility = View.VISIBLE
//            this.contentDescription = context.getString(R.string.recording_is_paused)
//        }
//        CallRecordingState.InProgress -> {
//            val tintColor = if (isAudioOnlyMode) R.color.red_40 else R.color.theme_text_error_normal_ldt
//            this.imageTintList = ColorStateList.valueOf(resources.getColor(tintColor, null))
//            this.visibility = View.VISIBLE
//            this.contentDescription = context.getString(R.string.recording_in_progress_accessibility)
//        }
//    }
//}
//
///**
// * Performs the given action when this view is laid out.
// *
// * @see doOnLayout
// */
//inline fun ViewParent.doOnLayout(crossinline action: (view: View) -> Unit) {
//    (this as View).doOnLayout(action)
//}
//
///**
// * Performs the given action when the root view is laid out.
// *
// * @see doOnLayout
// */
//inline fun View.doOnRootViewLayout(crossinline action: (view: View) -> Unit) {
//    (findViewById<ViewGroup>(android.R.id.content).getChildAt(0) as ViewGroup).doOnLayout(action)
//}
//
//fun View.updateMargin(
//    @Px left: Int? = null,
//    @Px top: Int? = null,
//    @Px right: Int? = null,
//    @Px bottom: Int? = null
//) {
//    val lp0 = layoutParams as? ViewGroup.MarginLayoutParams
//    val lp1 = layoutParams as? ConstraintLayout.LayoutParams
//
//    if (lp0 == null && lp1 == null) {
//        return
//    }
//
//    left?.let {
//        lp0?.leftMargin = it
//        lp1?.leftMargin = it
//    }
//    top?.let {
//        lp0?.topMargin = it
//        lp1?.topMargin = it
//    }
//
//    right?.let {
//        lp0?.rightMargin = it
//        lp1?.rightMargin = it
//    }
//
//    bottom?.let {
//        lp0?.bottomMargin = it
//        lp1?.bottomMargin = it
//    }
//
//    layoutParams = lp0 ?: lp1
//}
//
//fun View.updateSize(@Px width: Int? = null, @Px height: Int? = null) {
//    val lp0 = layoutParams ?: return
//
//    width?.let {
//        lp0.width = it
//    }
//    height?.let {
//        lp0.height = it
//    }
//
//    layoutParams = lp0
//}
//
//fun View.updateDimenSize(@DimenRes widthDimenId: Int, @DimenRes heightDimenId: Int) {
//    updateSize(resources.getDimensionPixelSize(widthDimenId), resources.getDimensionPixelSize(heightDimenId))
//}
//
///**
// * Extension method to provide show keyboard for View.
// */
//fun View.showKeyboard() {
//    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//    requestFocus()
//    // Sometimes the showSoftInput() fails to actually open the keyboard - toggleSoftInput() seems to work as expected
//    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
//}
//
///**
// * Show the soft keyboard in a forced mode.
// */
//fun View.showKeyboardForced() {
//    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//    requestFocus()
//    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
//}
//
///**
// * Extension method to provide hide keyboard for [View].
// */
//fun View.hideKeyboard() {
//    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//    imm.hideSoftInputFromWindow(windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
//}
//
///**
// * Listen for the [EditorInfo.IME_ACTION_DONE] action.
// */
//fun TextView.onClickKeyboardDoneButton(action: (view: View) -> Unit) {
//    this.setOnEditorActionListener { _, actionId, _ ->
//        when (actionId) {
//            EditorInfo.IME_ACTION_DONE -> {
//                action.invoke(this)
//                true
//            }
//            else -> false
//        }
//    }
//}
//
//fun RecyclerView.disableAnimations() {
//    (itemAnimator as? SimpleItemAnimator)?.apply {
//        supportsChangeAnimations = false
//        changeDuration = 0
//    }
//}
//
///**
// * Listen for RecyclerView itemview onInterceptTouch
// */
//fun RecyclerView.onItemInterceptTouch(action: (rv: RecyclerView, e: MotionEvent) -> Unit) {
//    this.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
//        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
//            action.invoke(rv, e)
//            return false
//        }
//    })
//}
//
//@SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
//fun WebView.initialize(webClient: WebViewClient, name: String) {
//    clearCache(true)
//    addJavascriptInterface(webClient, name)
//    webViewClient = webClient
//    settings.setSupportZoom(true)
//    settings.javaScriptEnabled = true
//    settings.domStorageEnabled = true
//    settings.allowFileAccess = false
//    settings.cacheMode = WebSettings.LOAD_NO_CACHE
//}
//
//inline fun View.onFocusChanged(crossinline action: (view: View, hasFocus: Boolean) -> Unit) {
//    onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus -> action.invoke(view, hasFocus) }
//}
//
///**
// * Send an action to the actor immediately, if it is possible, or discards the action otherwise. An offer actually returns a Boolean result which we ignore here.
// *
// * Clicking repeatedly on the View will result in sequent clicks to be just ignored while the first click event is handled.
// * This happens because the actor is busy with the first action and does not receive from its channel.
// *
// * By default, an actor's mailbox is backed by RendezvousChannel, whose offer operation succeeds only when the receive is active.
// *
// * **Note: Even though this API is marked as Obsolete, there is no replacement yet.
// *          See [issue #776](https://github.com/Kotlin/kotlinx.coroutines/issues/776) &
// *              [issue #87](https://github.com/Kotlin/kotlinx.coroutines/issues/87)**
// */
//@OptIn(ObsoleteCoroutinesApi::class, DelicateCoroutinesApi::class)
//fun View.onClickBlocking(scope: CoroutineScope = GlobalScope, action: suspend (View) -> Unit) {
//    // launch one actor
//    val eventActor = scope.actor<View>(Dispatchers.Main) {
//        for (event in channel) action(event)
//    }
//    // install a listener to activate this actor
//    setOnClickListener {
//        eventActor.trySend(it).isSuccess
//    }
//}
//
//fun View.setDoubleClickListener(doubleClick: () -> Unit) {
//    this.setOnTouchListener(DoubleClickOnTouchListener(context, doubleClick))
//}
//
///**
// * [targetPosition] to which position to scroll to
// * [smoothScroll]   whether to scroll to that position smoothly
// *
// * If not smooth scroll, then just scroll to without animation
// * If smooth scroll, then check the distance between current position and target position
// *    If distance > 15, then fast scroll to somewhere near the target position, then scroll to it smoothly
// *    If distance <= 15, then just scroll to target position smoothly
// *
// */
//fun RecyclerView.scrollToPosition(targetPosition: Int, smoothScroll: Boolean) {
//    val adapter = this.adapter
//    if (adapter == null || targetPosition < 0 || targetPosition >= adapter.itemCount) {
//        return
//    }
//    if (smoothScroll) {
//        val maxScrollDelta = 15
//        val layoutManager = this.layoutManager as LinearLayoutManager? ?: return
//        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//        val delta = targetPosition - firstVisibleItemPosition
//        val intermediatePosition: Int = if (delta > maxScrollDelta) {
//            max(0, targetPosition - maxScrollDelta)
//        } else if (delta < -maxScrollDelta) {
//            val count = layoutManager.itemCount
//            min(count - 1, targetPosition + maxScrollDelta)
//        } else {
//            -1
//        }
//        if (intermediatePosition != -1) {
//            this.scrollToPosition(intermediatePosition)
//        }
//        this.smoothScrollToPosition(targetPosition)
//    } else {
//        this.scrollToPosition(targetPosition)
//    }
//}
//
//fun View.applyRotationAnimation(duration: Long = 4000) {
//    val rotateAnimation = RotateAnimation(
//        0f, 359f,
//        Animation.RELATIVE_TO_SELF, 0.5f,
//        Animation.RELATIVE_TO_SELF, 0.5f
//    )
//    rotateAnimation.duration = duration
//    rotateAnimation.repeatCount = -1
//    this.startAnimation(rotateAnimation)
//}
