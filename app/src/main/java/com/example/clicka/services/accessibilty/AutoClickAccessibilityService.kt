package com.example.clicka.services.accessibilty

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.clicka.actions.gesture.GestureExecutor
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicBoolean

private const val TAG = "AutoClickService"

class AutoClickAccessibilityService : AccessibilityService() {

    companion object {
        @Volatile
        var instance: AutoClickAccessibilityService? = null
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val gestureExecutor = GestureExecutor()
    private var autoClickJob: Job? = null
    private val running = AtomicBoolean(false)

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        Log.i(TAG, "Accessibility service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // no-op
    }

    override fun onInterrupt() {
        Log.w(TAG, "Accessibility service interrupted")
    }

    override fun onDestroy() {
        stopAutoClick("onDestroy")
        instance = null
        scope.cancel()
        super.onDestroy()
    }

    suspend fun performClickAt(x: Int, y: Int, durationMs: Long = 50L): Boolean {
        val path = android.graphics.Path().apply { moveTo(x.toFloat(), y.toFloat()) }
        val stroke = GestureDescription.StrokeDescription(path, 0, durationMs)
        val gesture = GestureDescription.Builder().addStroke(stroke).build()
        return try {
            val ok = gestureExecutor.dispatchGesture(this, gesture)
            Log.d(TAG, "dispatchGesture result=$ok x=$x y=$y durationMs=$durationMs")
            ok
        } catch (t: Throwable) {
            Log.w(TAG, "dispatchGesture threw x=$x y=$y durationMs=$durationMs", t)
            false
        }
    }

    fun startAutoClick(x: Int, y: Int, intervalMs: Long) {
        val safeInterval = intervalMs.coerceAtLeast(50L)

        if (running.getAndSet(true)) {
            Log.d(TAG, "startAutoClick ignored: already running")
            return
        }

        Log.i(TAG, "startAutoClick x=$x y=$y intervalMs=$safeInterval")

        autoClickJob = scope.launch {
            try {
                while (isActive) {
                    val ok = performClickAt(x, y)
                    if (!ok) {
                        Log.w(TAG, "tap failed, stopping auto click")
                        break
                    }
                    delay(safeInterval)
                }
            } catch (t: Throwable) {
                Log.e(TAG, "auto click loop crashed", t)
            } finally {
                running.set(false)
                Log.i(TAG, "auto click loop ended")
            }
        }
    }

    fun stopAutoClick(reason: String = "manual") {
        Log.i(TAG, "stopAutoClick reason=$reason")
        autoClickJob?.cancel()
        autoClickJob = null
        running.set(false)
    }

    // Help open accessibility settings if service not enabled
    fun openAccessibilitySettings() {
        Log.i(TAG, "opening accessibility settings")
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    fun isAutoClickRunning(): Boolean = running.get()

    fun toggleAutoClick(x: Int, y: Int, intervalMs: Long) {
        Log.i(TAG, "toggleAutoClick running=${isAutoClickRunning()} x=$x y=$y intervalMs=$intervalMs")
        if (isAutoClickRunning()) stopAutoClick("toggle") else startAutoClick(x, y, intervalMs)
    }
}