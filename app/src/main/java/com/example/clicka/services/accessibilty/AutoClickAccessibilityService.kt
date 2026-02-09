package com.example.clicka.services.accessibilty

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.clicka.actions.gesture.GestureExecutor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

private const val TAG = "AutoClickService"

class AutoClickAccessibilityService : AccessibilityService() {

    companion object {
        @Volatile
        var instance: AutoClickAccessibilityService? = null
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val gestureExecutor = GestureExecutor()

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
        instance = null
        serviceScope.cancel()
        super.onDestroy()
    }

    /**
     * Single click entry point used by the domain engine.
     */
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

    // Help open accessibility settings if service not enabled
    fun openAccessibilitySettings() {
        Log.i(TAG, "opening accessibility settings")
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }
}