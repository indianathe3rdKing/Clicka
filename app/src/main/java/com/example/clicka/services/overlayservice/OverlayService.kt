package com.example.clicka.services.overlayservice

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.clicka.ui.extensions.FloatingButton
import com.example.clicka.ui.extensions.SettingsModal
import com.example.clicka.ui.extensions.AutoClickSettings
import com.example.clicka.services.accessibilty.AutoClickAccessibilityService
import com.example.clicka.services.overlaylifecycleowner.OverlayLifecyeOwner
import com.example.clicka.ui.theme.ClickaTheme
import com.example.clicka.ui.theme.BorderColor
import com.example.clicka.config.data.getConfigPreferences
import com.example.clicka.config.data.getClickPressDurationConfig
import com.example.clicka.config.data.getClickRepeatCountConfig
import com.example.clicka.config.data.getClickRepeatDelayConfig
import com.example.clicka.config.data.getPauseDurationConfig
import com.example.clicka.config.data.getRandomizeConfig
import com.example.clicka.config.data.putClickPressDurationConfig
import com.example.clicka.config.data.putClickRepeatDelayConfig
import com.example.clicka.config.data.putPauseDurationConfig
import com.example.clicka.config.data.putClickRepeatCountConfig
import com.example.clicka.config.data.putRandomizeConfig
import com.example.clicka.domain.model.ClickMode
import com.example.clicka.state.ModeState
import kotlin.math.roundToInt

private val TAG = "Overlay"

class OverlayService : Service() {
    private val windowManager
        get() = getSystemService(WINDOW_SERVICE) as WindowManager
    private val overlayViews = mutableMapOf<ComposeView, OverlayLifecyeOwner>()
    private var buttonNumber = 0

    private var modalView: ComposeView? = null

    private var overlayGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    // Track click button positions: buttonNumber -> (x, y) center coordinates
    private val clickButtonPositions = mutableMapOf<Int, Pair<Int, Int>>()
    private val swipePositions = mutableListOf<Pair<Int, Int>>()


    // Track click button views for removal
    private val clickButtonViews = mutableMapOf<Int, Pair<ComposeView, OverlayLifecyeOwner>>()

    // Track if auto-clicking is active (to show/hide buttons)
    private var isAutoClicking = false

    override fun onCreate() {
        super.onCreate()
        showOverlay()
    }

    private fun showOverlay() {
        if (!Settings.canDrawOverlays(this)) {
            stopSelf()
            return
        }

        val layoutFlag: Int =
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSPARENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 200
        }

        val composeView = ComposeView(this).apply { isClickable = true }
        val lifecycleOwner = OverlayLifecyeOwner()

        composeView.setContent {
            ClickaTheme {
                FloatingButton(
                    onMoveBy = { dragX, dragY ->
                        params.y += dragY
                        params.x += dragX
                        windowManager.updateViewLayout(composeView, params)
                    },
                    onClose = { stopSelf() },
                    onAdd = {
                        // Get the current mode WHEN the button is clicked, not when the overlay was created
                        val currentMode = ModeState.getCurrentMode()
                        Log.i(TAG, "onAdd clicked - currentMode: $currentMode, buttonNumber: $buttonNumber")
                        when (currentMode) {
                            ClickMode.SINGLE -> if (buttonNumber < 1) addButton()
                            ClickMode.SWIPE -> if (buttonNumber < 2) addButton()
                            ClickMode.MULTIPLE -> addButton()
                        }
                    },
                    onPlay = {
                        // Get the current mode WHEN play is clicked
                        val currentMode = ModeState.getCurrentMode()
                        mode(currentMode)
                    },
                    onRemove = { removeLastButton() },
                    onSettings = { showSettingsModal() }
                )
            }
        }

        setupLifecycle(composeView, lifecycleOwner)

        windowManager.addView(composeView, params)
        overlayViews[composeView] = lifecycleOwner
    }

    override fun onDestroy() {
        overlayViews.forEach { (view, owner) ->
            owner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            owner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            try {
                windowManager.removeView(view)
            } catch (_: Throwable) {
            }
        }
        overlayViews.clear()
        // Stop any running auto-click via accessibility service
        AutoClickAccessibilityService.instance?.stopAutoClick()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @Composable
    private fun ClickButton(
        onMoveBy: (dragX: Int, dragY: Int) -> Unit,
        onRemove: () -> Unit, ButtonNumber: Int
    ) {

        ClickaTheme {
            FloatingActionButton(
                onClick = { onRemove() },

                modifier = Modifier
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            onMoveBy(
                                dragAmount.x.roundToInt(),
                                dragAmount.y.roundToInt()
                            )
                        }
                    }
                    .size(40.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .border(
                        2.dp,
                        BorderColor,
                        RoundedCornerShape(20.dp)
                    ),
                containerColor = Color(98, 97, 97, 52),
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Text(
                    ButtonNumber.toString(), color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold, fontSize = 18.sp
                )
            }
        }
    }

    private fun addButton() {
        buttonNumber++
        val currentButtonNumber = buttonNumber
        val layoutFlag: Int =
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSPARENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 100
            y = 300
        }

        val composeView = ComposeView(this).apply { isClickable = true }
        val lifecycleOwner = OverlayLifecyeOwner()

        val metrics = resources.displayMetrics
        val fabSize = (40 * metrics.density).roundToInt()
        var overlayWidth = fabSize
        var overlayHeight = fabSize

        // Initialize position tracking for this button
        clickButtonPositions[currentButtonNumber] = Pair(
            params.x + (overlayWidth / 2),
            params.y + (overlayHeight / 2)
        )

        // For SWIPE mode, also track in swipePositions (max 2 points)
        val currentMode = ModeState.getCurrentMode()
        if (currentMode == ClickMode.SWIPE && swipePositions.size < 2) {
            swipePositions.add(Pair(
                params.x + (overlayWidth / 2),
                params.y + (overlayHeight / 2)
            ))
            Log.i(TAG, "Added swipe point ${swipePositions.size}/2: ${swipePositions.last()}")
        }

        overlayGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            overlayWidth = if (composeView.width > 0) composeView.width else fabSize
            overlayHeight = if (composeView.height > 0) composeView.height else fabSize
            // Update position when layout changes
            val newPos = Pair(
                params.x + (overlayWidth / 2),
                params.y + (overlayHeight / 2)
            )
            clickButtonPositions[currentButtonNumber] = newPos

            // Also update swipePositions if in SWIPE mode
            val currentMode = ModeState.getCurrentMode()
            if (currentMode == ClickMode.SWIPE) {
                val index = currentButtonNumber - 1
                if (index in swipePositions.indices) {
                    swipePositions[index] = newPos
                }
            }
        }
        composeView.setContent {
            ClickButton(
                onMoveBy = { dragX, dragY ->
                    params.y += dragY
                    params.x += dragX
                    windowManager.updateViewLayout(composeView, params)
                    // Update position when button is dragged
                    val newPos = Pair(
                        params.x + (overlayWidth / 2),
                        params.y + (overlayHeight / 2)
                    )
                    clickButtonPositions[currentButtonNumber] = newPos

                    // Also update swipePositions if in SWIPE mode
                    val currentMode = ModeState.getCurrentMode()
                    if (currentMode == ClickMode.SWIPE) {
                        val index = currentButtonNumber - 1
                        if (index in swipePositions.indices) {
                            swipePositions[index] = newPos
                        }
                    }
                },
                onRemove = {
                    // When clicked, just log position (could show settings in future)
                    val pos = clickButtonPositions[currentButtonNumber]
                    Log.i(TAG, "Button $currentButtonNumber clicked at position: $pos")
                }, currentButtonNumber
            )
        }

        setupLifecycle(composeView, lifecycleOwner)

        windowManager.addView(composeView, params)
        overlayViews[composeView] = lifecycleOwner
        clickButtonViews[currentButtonNumber] = Pair(composeView, lifecycleOwner)
    }

    private fun showSettingsModal() {
        if (modalView != null) return

        val layoutFlag: Int =
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY


        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
            x = 0
            y = 0
        }
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE

        val composeView = ComposeView(this).apply { isClickable = true }
        val lifecycleOwner = OverlayLifecyeOwner()

        // Load current settings from SharedPreferences (same prefs that EditedActionBuilder uses)
        val prefs = getConfigPreferences()
        val initialSettings = AutoClickSettings(
            pressDurationMs = prefs.getClickPressDurationConfig(50L),
            repeatDelayMs = prefs.getClickRepeatDelayConfig(100L),
            repeatCount = prefs.getClickRepeatCountConfig(1),
            cycleDelayMs = prefs.getPauseDurationConfig(1000L),
            randomize = prefs.getRandomizeConfig(true)
        )

        composeView.setContent {
            ClickaTheme {
                SettingsModal(
                    initialSettings = initialSettings,
                    onSave = { settings ->
                        // Save settings to SharedPreferences (EditedActionBuilder will read these)
                        getConfigPreferences().edit()
                            .putClickPressDurationConfig(settings.pressDurationMs)
                            .putClickRepeatDelayConfig(settings.repeatDelayMs)
                            .putClickRepeatCountConfig(settings.repeatCount)
                            .putPauseDurationConfig(settings.cycleDelayMs)
                            .putRandomizeConfig(settings.randomize)
                            .apply()
                        Log.i(TAG, "Saved click settings: $settings")
                    },
                    onClose = { removeButton(composeView, lifecycleOwner) }
                )
            }
        }

        val viewModelStore = ViewModelStore()
        val viewModelStoreOwner = object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore
                get() = viewModelStore
        }

        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)

        composeView.setViewTreeLifecycleOwner(lifecycleOwner)
        composeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)
        composeView.setViewTreeViewModelStoreOwner(viewModelStoreOwner)

        modalView = composeView
        windowManager.addView(modalView, params)

    }

    private fun removeButton(composeView: ComposeView, lifecycleOwner: OverlayLifecyeOwner) {
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        try {
            windowManager.removeView(composeView)
            modalView = null
        } catch (_: Throwable) {
        }
        overlayViews.remove(composeView)
    }

    private fun setupLifecycle(composeView: ComposeView, lifecycleOwner: OverlayLifecyeOwner) {
        val viewModelStore = ViewModelStore()
        val viewModelStoreOwner = object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore
                get() = viewModelStore
        }

        if (lifecycleOwner.lifecycle.currentState == Lifecycle.State.INITIALIZED) {
            lifecycleOwner.performRestore(null)
        }

        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)

        composeView.setViewTreeLifecycleOwner(lifecycleOwner)
        composeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)
        composeView.setViewTreeViewModelStoreOwner(viewModelStoreOwner)
    }


    /**
     * Hide all click button overlays so gestures can reach the underlying app.
     */
    private fun hideClickButtons() {
        clickButtonViews.values.forEach { (view, _) ->
            try {
                view.visibility = android.view.View.GONE
            } catch (_: Throwable) {
            }
        }
    }

    /**
     * Show all click button overlays again.
     */
    private fun showClickButtons() {
        clickButtonViews.values.forEach { (view, _) ->
            try {
                view.visibility = android.view.View.VISIBLE
            } catch (_: Throwable) {
            }
        }
    }

    /**
     * Start/stop auto-clicking at all placed button positions.
     * This is called when the user presses the "play" FAB button.
     * Clicks are dispatched via Accessibility Service using the Engine architecture.
     */
    private fun startAutoClickAllButtons() {
        val service = AutoClickAccessibilityService.instance

        if (service == null) {
            Log.w(TAG, "Accessibility service not running, opening settings")
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            return
        }

        if (clickButtonPositions.isEmpty()) {
            Log.w(TAG, "No click buttons placed, nothing to auto-click")
            return
        }

        // Toggle: if already running, stop and show buttons again
        if (service.isRunning()) {
            Log.i(TAG, "Stopping auto-click via Engine")
            service.stopAutoClick()
            isAutoClicking = false
            showClickButtons()
            return
        }

        // Get all button positions sorted by button number (order of creation)
        val positions = clickButtonPositions.toSortedMap().values.toList()
        Log.i(TAG, "Starting auto-click for ${positions.size} buttons via Engine: $positions")

        // Hide the click buttons so gestures reach the underlying app
        isAutoClicking = true
        hideClickButtons()

        // Use the Engine-based approach via the accessibility service
        // Config values (press duration, repeat count, etc.) come from SharedPreferences via EditedActionBuilder
        service.startAutoClickWithPositions(positions)

    }

    private fun startAutoSwipeAllButtons() {
        val service = AutoClickAccessibilityService.instance

        if (service==null){
            Log.w(TAG, "Accessibility service not running, opening settings")
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            return
        }

        if(swipePositions.size<2){
            Log.w(TAG, "No click buttons placed, nothing to auto-click")
            return
        }

        //Toggle if already running, stop and show buttons again
        if (service.isRunning()){
            Log.i(TAG, "Stopping auto-click via Engine")
            service.stopAutoClick()
            isAutoClicking=false
            showClickButtons()
            return
        }

        val fromPoint=swipePositions[0]
        val toPoint=swipePositions[1]

        //Hide the click buttons so gestures reach the underlying app
        isAutoClicking=true
        hideClickButtons()

        //Start the service
        service.startAutoSwipeWithPositions(fromPoint,toPoint)

    }

    /**
     * Remove the last added click button.
     */

    private fun removeLastButton() {
        if (buttonNumber <= 0) {
            Log.w(TAG, "No buttons to remove")
            return
        }

        val lastButtonNumber = buttonNumber
        clickButtonViews[lastButtonNumber]?.let { (view, owner) ->
            owner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            owner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            try {
                windowManager.removeView(view)
            } catch (_: Throwable) {
            }
            overlayViews.remove(view)
            clickButtonViews.remove(lastButtonNumber)
            clickButtonPositions.remove(lastButtonNumber)

            // Also remove from swipePositions if in SWIPE mode
            val currentMode = ModeState.getCurrentMode()
            if (currentMode == ClickMode.SWIPE && swipePositions.isNotEmpty()) {
                swipePositions.removeAt(swipePositions.size - 1)
                Log.i(TAG, "Removed swipe point, remaining: ${swipePositions.size}")
            }

            buttonNumber--
            Log.i(TAG, "Removed button $lastButtonNumber")
        }
    }

    private fun mode(mode: ClickMode) {
        when (mode) {
            ClickMode.SINGLE -> {
                startAutoClickAllButtons()
                Log.i(TAG, "Single Mode")
            }

            ClickMode.MULTIPLE -> {
                startAutoClickAllButtons()
                Log.i(TAG, "Multiple Mode")
            }

            ClickMode.SWIPE -> {
                startAutoSwipeAllButtons()
                Log.i(TAG, "Swipe Mode")
            }
        }
    }
}
