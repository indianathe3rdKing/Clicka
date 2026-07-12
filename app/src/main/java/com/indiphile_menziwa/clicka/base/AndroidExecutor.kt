package com.indiphile_menziwa.clicka.base


import android.accessibilityservice.GestureDescription

interface AndroidExecutor{
    suspend fun executeGesture(gestureDescription: GestureDescription)

    fun executeGlobalAction(globalAction: Int)
}