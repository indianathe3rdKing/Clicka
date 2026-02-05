package com.example.clicka.engine

import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.Log
import com.example.clicka.actions.gesture.buildSingletonStroke
import com.example.clicka.actions.gesture.line
import com.example.clicka.actions.gesture.moveTo
import com.example.clicka.actions.utlis.getPauseDurationMs
import com.example.clicka.base.AndroidExecutor
import com.example.clicka.base.UnblockGestureScheduler
import com.example.clicka.base.buildUnblockGesture
import com.example.clicka.domain.model.Action
import com.example.clicka.domain.model.Repeatable


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class ActionExecutor @Inject constructor(
    private val androidExecutor: AndroidExecutor,
) {
    private val random: Random = Random(System.currentTimeMillis())
    private var randomize: Boolean = false

    private var unblockWorkaroundEnabled: Boolean = false

    private val unblockGestureScheduler: UnblockGestureScheduler? =
        if (unblockWorkaroundEnabled) UnblockGestureScheduler()
        else null

    fun setUnblockWorkaround(isEnabled: Boolean) {
        unblockWorkaroundEnabled = isEnabled
    }

    suspend fun onScenarioLoopFinished() {
        if (unblockGestureScheduler?.shouldTrigger() == true) {
            withContext<Unit>(Dispatchers.Main) {
                Log.i(TAG, "Injecting unblock gesture")
                androidExecutor.executeGesture(
                    GestureDescription.Builder().buildUnblockGesture()
                )
            }
        }
    }

    suspend fun executeAction(action: Action, randomize: Boolean) {
        this.randomize = randomize
        when (action) {
            is Action.Click -> executeClick(action)
            is Action.Swipe -> executeSwipe(action)
            is Action.Pause -> executePause(action)

        }
    }

    private suspend fun executeClick(click: Action.Click) {
        val clickGesture = GestureDescription.Builder().buildSingletonStroke(
            path = Path().apply { moveTo(click.position, random) },
            click.pressDurationMs, random = random
        )

        executeRepeatableGesture(clickGesture, click)
    }

    private suspend fun executeSwipe(swipe: Action.Swipe) {
        val swipeGesture = GestureDescription.Builder().buildSingletonStroke(
            path = Path().apply {
                line(
                    from = swipe.fromPosition,
                            to = swipe.toPosition,
                    random = random
                )
            },
            durationMs = swipe.swipeDurationMs,
            random = random
        )
        executeRepeatableGesture(swipeGesture, swipe)
    }

    private suspend fun executePause(pause: Action.Pause) {
        delay(pause.pauseDurationMs.getPauseDurationMs(random))
    }

    private suspend fun executeRepeatableGesture(gesture: GestureDescription, repeatable: Repeatable) {
        repeatable.repeatCount {
            withContext<Unit>(Dispatchers.Main) {
                androidExecutor.executeGesture(gesture)
            }
        }
    }

}

private val TAG = "ActionExecutor"