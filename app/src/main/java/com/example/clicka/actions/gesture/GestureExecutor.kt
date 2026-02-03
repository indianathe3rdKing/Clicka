package com.example.clicka.actions.gesture

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityService.GestureResultCallback
import android.accessibilityservice.GestureDescription
import android.util.Log
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Singleton
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "GestureExecutor"

@Singleton
internal class GestureExecutor @Inject constructor() {

    private var resultCallback: GestureResultCallback? = null
    private var currentContinuation: Continuation<Boolean>? = null

    private var completeGesture: Long = 0L
    private var cancelledGesture: Long = 0L
    private var errorGestures: Long = 0L


    fun clear() {
        completeGesture = 0L
        cancelledGesture = 0L
        errorGestures = 0L

        resultCallback = null
        currentContinuation = null
    }

    suspend fun dispatchGesture(
        service: AccessibilityService,
        gesture: GestureDescription
    ): Boolean {
        if (currentContinuation != null) {
            Log.w(
                TAG,
                "Previous gesture result is not available yet,clearing listener to avoid stale events"
            )
        }
        resultCallback = null
        currentContinuation = null

        resultCallback = resultCallback ?: newGestureResultCallback()
        return suspendCancellableCoroutine { contiuation ->
            currentContinuation = contiuation

            try {
                service.dispatchGesture(gesture, resultCallback, null)
            } catch (rEx: RuntimeException) {
                Log.w(
                    TAG,
                    "System is not responsive, the user might be spamming gesture too quick",
                    rEx
                )
                errorGestures++
                resumeExecution(gestureError = true)
            }

            contiuation.invokeOnCancellation {
                if (currentContinuation === contiuation) {
                    currentContinuation = null
                }
            }
        }


    }

    private fun resumeExecution(gestureError: Boolean) {
        currentContinuation?.let { continuation ->
            currentContinuation = null

            try {
                continuation.resume(!gestureError)
            } catch (isEx: IllegalStateException) {
                Log.w(
                    TAG,
                    "Continuation is already resumed. This same event gets two results",
                    isEx
                )
            }
        } ?: Log.w(TAG, "Can't resume continuation. Did the same event get two results?")
    }

    private fun newGestureResultCallback() = object : GestureResultCallback() {
        override fun onCompleted(gestureDescription: GestureDescription?) {
            completeGesture++
            resumeExecution(gestureError = false)
        }

        override fun onCancelled(gestureDescription: GestureDescription?) {
            cancelledGesture++
            resumeExecution(gestureError = false)
        }
    }

}