package com.example.clicka.engine

import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.Log
import com.example.clicka.actions.gesture.buildSingletonStroke


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class ActionExecutor @Inject constructor(
    private val androidExecutor: AndroidExecutor,
){
    private val random: Random = Random(System.currentTimeMillis())
    private var randomize: Boolean = false

    private var unblockWorkaroundEnabled: Boolean = false

    private val unblockGestureScheduler: UnblockGestureScheduler? =
        if(unblockWorkaroundEnabled) UnblockGestureScheduler()
        else null

    fun setUnblockWorkaround(isEnabled: Boolean){
        unblockWorkaroundEnabled = isEnabled
    }

    suspend fun onScenarioLoopFinished(){
        if(unblockGestureScheduler?.shouldTrigger()== true){
            withContext(Dispatchers.Main){
                Log.i(TAG,"Injecting unblock gesture")
                androidExecutor.dispatchGesture(
                    GestureDescription.Builder().buildUnblockGesture()
                )
            }
        }
    }
}

private val TAG = "ActionExecutor"