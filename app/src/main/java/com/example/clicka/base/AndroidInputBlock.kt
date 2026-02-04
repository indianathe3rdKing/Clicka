package com.example.clicka.base

import android.accessibilityservice.GestureDescription
import android.graphics.Path


fun GestureDescription.Builder.buildUnblockGesture(): GestureDescription=
    addStroke(createUnblockClickStroke(1f,1f))
        .addStroke(createUnblockClickStroke(1f,3f))
        .addStroke(createUnblockClickStroke(2f,2f))
        .build()


private fun createUnblockClickStroke(posX: Float,posY:Float): GestureDescription.StrokeDescription =
    GestureDescription.StrokeDescription(
        Path().apply { moveTo(posX,posY) },
        0L,
        1L
    )

class UnblockGestureScheduler{
    private var lastUnblockTimeMs = System.currentTimeMillis()

    fun shouldTrigger():Boolean{
        val currentTimeMs = System.currentTimeMillis()
        if(currentTimeMs>= (lastUnblockTimeMs+ UNBLOCK_DELAY_MS)){
            lastUnblockTimeMs = currentTimeMs

            return true
        }

        return false
    }
}

private const val UNBLOCK_DELAY_MS = 10000L