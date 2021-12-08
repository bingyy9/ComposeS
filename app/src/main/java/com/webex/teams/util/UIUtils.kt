package com.webex.teams.util

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.GestureDetector
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.RESULT_UNCHANGED_SHOWN
import android.widget.PopupWindow
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.graphics.Insets
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.core.view.children
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import com.webex.teams.logging.TeamsLogger
import kotlin.math.roundToInt

object UIUtils {
    private var mLastClickTime = 0L

//    fun showPopUpMenu(context: Context, mobileSettingsViewModel: MobileSettingsViewModel) =
//        OSUtils.isDualPane(context) || mobileSettingsViewModel.isPopUpMenuActionsEnabled

    fun hideSoftKeyboard(activity: Activity) {
        if (activity.currentFocus != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }

    fun showSoftKeyboard(@NonNull context: Context, @NonNull view: View) {
        view.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideSoftKeyboard(@NonNull context: Context, @NonNull view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, RESULT_UNCHANGED_SHOWN)
    }

    fun getFullscreenSize(context: Context): Point {
        val size = Point()
        context.getDisplayExtensionFunc()?.getRealSize(size)
        return size
    }

    fun getScreenWidth(context: Context): Int {
        val metrics = DisplayMetrics()
        return context.getMetricsWidthExtensionFunc(metrics)
    }

    fun getScreenHeight(context: Context): Int {
        val metrics = DisplayMetrics()
        return context.getMetricsHeightExtensionFunc(metrics)
    }

    fun hasNavigationBar(context: Context): Boolean {
        val resources = context.resources
        val resourceId = resources.getIdentifier("config_showNavigationBar", "bool", "android")
        if (resourceId > 0) {
            return resources.getBoolean(resourceId)
        }
        // Check for keys
        val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        return !hasMenuKey && !hasBackKey
    }

    fun getNavigationBarSize(context: Context): Point {
        val appUsableSize = getAppUsableScreenSize(context)
        val realScreenSize = getRealScreenSize(context)

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            return Point(realScreenSize.x - appUsableSize.x, appUsableSize.y)
        }

        // navigation bar at the bottom
        return if (appUsableSize.y < realScreenSize.y) {
            Point(appUsableSize.x, realScreenSize.y - appUsableSize.y)
        } else Point()
    }

    fun onViewTouchListener(parent: ViewGroup, gestureDetector: GestureDetector?, callback: () -> Unit): OnTouchListener {
        return object : OnTouchListener {
            private var relativeRawX = 0F
            private var relativeRawY = 0F

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                if (gestureDetector != null && gestureDetector.onTouchEvent(event)) {
                    return true
                }

                val parentHeight = parent.height
                val parentWidth = parent.width

                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        callback()
                        this.relativeRawX = event.rawX - view.x
                        this.relativeRawY = event.rawY - view.y
                    }
                    MotionEvent.ACTION_MOVE -> {
                        var newViewX = event.rawX - this.relativeRawX
                        newViewX = Math.max(0F, newViewX)
                        newViewX = Math.min((parentWidth - view.width).toFloat(), newViewX)
                        view.x = newViewX

                        var newViewY = event.rawY - this.relativeRawY
                        newViewY = Math.max(0F, newViewY)
                        newViewY = Math.min((parentHeight - view.height).toFloat(), newViewY)
                        view.y = newViewY
                    }
                    MotionEvent.ACTION_UP -> {
                        return true
                    }
                    else -> return false
                }
                return true
            }
        }
    }

    fun getAppUsableScreenSize(context: Context): Point {
        val size = context.getSizeExtensionFunc()
        return Point(size.width, size.height)
    }

    fun getRealScreenSize(context: Context): Point {
        val size = Point()
        context.getDisplayExtensionFunc()?.getRealSize(size)

        return size
    }

    fun getContentView(window: Window): View? {
        return window.findViewById<ViewGroup>(android.R.id.content)
    }

    @Suppress("DEPRECATION")
    fun addUnlockFlags(activity: Activity) {
        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
    }

    fun isPortraitMode(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    fun isInLandscapeMode(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    fun isInDarkMode(context: Context): Boolean {
        return context.resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
    }

//    fun canDrawOnTopOfApps(context: Context): Boolean {
//        return Settings.canDrawOverlays(context)
//    }

    fun dpToPx(res: Resources, dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.displayMetrics).toInt()
    }

    fun spToPx(res: Resources, sp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, res.displayMetrics).toInt()
    }

    fun spToPx(context: Context, sp: Float): Int {
        return spToPx(context.resources, sp)
    }

//    fun updateStatusBarColor(activity: Activity, color: String) {
//        val window = activity.window
//
//        window.setTranslucentStatusBar()
//
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = Color.parseColor(color)
//    }

//    fun setDarkStatusAndNavigationBar(activity: Activity) {
//        val window = activity.window
//        window.clearTranslucentStatusBar()
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = activity.resources.getColor(R.color.gray_95, null)
//        window.navigationBarColor = activity.resources.getColor(R.color.gray_95, null)
//        window.setStatusAndNavigationBar(true)
//    }

//    @SuppressLint("ResourceType")
//    fun restoreStatusAndNavigationBar(activity: Activity) {
//        val theme = activity.theme
//        //
//        val attrs = intArrayOf(android.R.attr.windowDrawsSystemBarBackgrounds, android.R.attr.windowTranslucentStatus, android.R.attr.statusBarColor, android.R.attr.navigationBarColor)
//        val typedValue = theme.obtainStyledAttributes(attrs)
//        val drawSystemBar = typedValue.getBoolean(0, false)
//        val translucentStatus = typedValue.getBoolean(1, false)
//        val color = typedValue.getColor(2, ContextCompat.getColor(activity, R.color.transparent))
//        val navigationBarColor = typedValue.getColor(3, ContextCompat.getColor(activity, R.color.transparent))
//        typedValue.recycle()
//
//        val window = activity.window
//        if (drawSystemBar) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        } else {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        }
//
//        if (translucentStatus) {
//            window.setTranslucentStatusBar()
//        } else {
//            window.clearTranslucentStatusBar()
//        }
//        window.statusBarColor = color
//        window.navigationBarColor = navigationBarColor
//
//        window.setStatusAndNavigationBar(isInDarkMode(activity))
//    }

//    @SuppressLint("ResourceType")
//    fun restoreStatusBarColorFromTheme(activity: Activity?) {
//        if (activity == null) {
//            return
//        }
//        val theme = activity.theme
//
//        val attrs = intArrayOf(android.R.attr.windowDrawsSystemBarBackgrounds, android.R.attr.windowTranslucentStatus, android.R.attr.statusBarColor)
//        val typedValue = theme.obtainStyledAttributes(attrs)
//        val drawSystemBar = typedValue.getBoolean(0, false)
//        val translucentStatus = typedValue.getBoolean(1, false)
//        val color = typedValue.getColor(2, ContextCompat.getColor(activity, R.color.transparent))
//        typedValue.recycle()
//
//        val window = activity.window
//        if (drawSystemBar) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        } else {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        }
//
//        if (translucentStatus) {
//            window.setTranslucentStatusBar()
//        } else {
//            window.clearTranslucentStatusBar()
//        }
//        window.statusBarColor = color
//    }

    fun clearExternalShareExtras(activity: Activity) {
        val intent = activity.intent
        intent?.let {
            if (intent.hasExtra(Intent.EXTRA_TEXT))
                intent.removeExtra(Intent.EXTRA_TEXT)
            if (intent.hasExtra(Intent.EXTRA_STREAM))
                intent.removeExtra(Intent.EXTRA_STREAM)

            intent.action = null
        }
    }

    fun clearCrossLaunchIntent(activity: Activity?) {
        val intent = activity?.intent
        intent?.let {
            it.data = null
            it.action = null
        }
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getActionBarHeight(context: Context): Int {
        var actionBarHeight = 0
        val tv = TypedValue()
        if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight =
                TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        }

        return actionBarHeight
    }

    fun getNavigationBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun copyToClipboard(context: Context, text: String?, label: String? = null) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
    }

    /**
     * Check if the provided [view] has the required space([minHeight] for drawing it's content vertically.
     * The vertical margins of the view are taken into consideration when computing the available space.
     */
    fun isEnoughSpaceVerticalForViewToDraw(view: View, minHeight: Int): Boolean {
        // if the min height is computed dynamically, this method could be called before the
        // views are laid out, thus this sanity check could avoid issues
        if (minHeight == 0) return true
        val container = view.parent as ViewGroup
        var availableHeight = container.height - (view.marginTop + view.marginBottom)
        for (v in container.children) {
            if (v.id != view.id) {
                availableHeight -= v.height
                availableHeight -= v.marginTop + v.marginBottom
            }
        }
        TeamsLogger.debug("EmptyView", "Computing space: available=$availableHeight, minHeight=$minHeight")
        return availableHeight >= minHeight
    }

    fun updateViewMaxHeight(parent: ConstraintLayout, drawable: Drawable?, viewId: Int, @FloatRange(from = 1.0) multiplyBy: Double) {
        val drawableHeight = drawable?.intrinsicHeight ?: 0
        val set = ConstraintSet()
        set.clone(parent)
        set.constrainMaxHeight(viewId, (drawableHeight * multiplyBy).roundToInt())
        set.applyTo(parent)
    }

    fun isClickTooFast(): Boolean {
        return isClickTooFast(300)
    }

    fun isClickTooFast(interval: Int): Boolean {
        val last: Long = mLastClickTime
        val now = SystemClock.elapsedRealtime()
        mLastClickTime = now
        return now - last < interval
    }

    fun getAnimationDurationAdjustedToScale(contentResolver: ContentResolver, duration: Long): Long {
        val animScale = Settings.Global.getFloat(contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f)
        return (duration / animScale).toLong()
    }

//    fun applyWindowTopInsets(targetView: View, insets: WindowInsetsCompat): WindowInsetsCompat {
//        // Use top insets and apply the top padding to the view.
//        val lp0 = targetView.layoutParams as? ViewGroup.MarginLayoutParams
//        val lp1 = targetView.layoutParams as? ConstraintLayout.LayoutParams
//
//        lp0?.topMargin = insets.getTopSystemWindowInset()
//        lp1?.topMargin = insets.getTopSystemWindowInset()
//
//        // Change the top insets to zero as it is consumed and create new insets
//        // Send the rest of the insets for the view to apply default policy.
//        return ViewCompat.onApplyWindowInsets(targetView, consumeSystemTopWindowInsets(insets))
//    }

//    fun consumeSystemTopWindowInsets(insets: WindowInsetsCompat): WindowInsetsCompat {
//        return insets.setSystemWindowInsetsCompat(
//            Insets.of(
//                insets.getLeftSystemWindowInset(),
//                0,
//                insets.getRightSystemWindowInset(),
//                insets.getBottomSystemWindowInset()
//            )
//        )
//    }

//    fun consumeSystemBottomWindowInsets(insets: WindowInsetsCompat): WindowInsetsCompat {
//        return insets.setSystemWindowInsetsCompat(
//            Insets.of(
//                insets.getLeftSystemWindowInset(),
//                insets.getTopSystemWindowInset(),
//                insets.getRightSystemWindowInset(),
//                0
//            )
//        )
//    }
//
//    fun updateStatusBarColor(context: Context, activity: Activity, @ColorRes color: Int) {
//        if (!OSUtils.isChromeOS()) {
//            val window = activity.window
//            window.clearTranslucentStatusBar()
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window.statusBarColor = context.resources.getColor(color, null)
//        }
//    }
}

fun updateAlphaAnimation(contextResolver: ContentResolver, views: ArrayList<View>, from: Float, to: Float, completion: () -> Unit, duration: Long = 500): ValueAnimator {
    val animator: ValueAnimator
    val calculatedDuration = UIUtils.getAnimationDurationAdjustedToScale(contextResolver, duration)

    ValueAnimator.ofFloat(from, to).apply {
        animator = this
        this.duration = calculatedDuration
        interpolator = LinearInterpolator()

        addUpdateListener {
            val value = it.animatedValue as Float
            for (view in views) {
                if ((view.alpha < value && from < to) || (view.alpha > value && from > to)) {
                    view.alpha = value
                }
            }
        }

        addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                completion()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }.start()
    return animator
}

fun updateGuideLineValue(contextResolver: ContentResolver, guidelines: ArrayList<Guideline>, from: Float, to: Float, completion: () -> Unit, duration: Long = 500) {
    val calculatedDuration = UIUtils.getAnimationDurationAdjustedToScale(contextResolver, duration)

    ValueAnimator.ofFloat(from, to).apply {
        this.duration = calculatedDuration

        addUpdateListener {
            val value = it.animatedValue as Float
            for (guideLine in guidelines) {
                guideLine.setGuidelinePercent(value)
            }
        }

        doOnEnd {
            completion()
        }
    }.start()
}

//fun View.setAccessibilityAction(@StringRes accessibilityActionString: Int) {
//    if (accessibilityActionString == ResourceUtils.NO_RESOURCES_ID) return
//    ViewCompat.setAccessibilityDelegate(
//        this,
//        object : AccessibilityDelegateCompat() {
//            override fun onInitializeAccessibilityNodeInfo(host: View?, info: AccessibilityNodeInfoCompat?) {
//                super.onInitializeAccessibilityNodeInfo(host, info)
//                val description = host?.resources?.getText(accessibilityActionString)
//                val accessibilityClick = AccessibilityNodeInfoCompat.AccessibilityActionCompat(
//                    AccessibilityNodeInfoCompat.ACTION_CLICK, description
//                )
//                info?.addAction(accessibilityClick)
//            }
//        }
//    )
//}

fun PopupWindow?.isShowing(): Boolean {
    return this != null && isShowing
}

fun PopupWindow?.hide() {
    if (this != null && isShowing) {
        dismiss()
    }
}
