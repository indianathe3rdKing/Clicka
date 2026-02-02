package com.example.clicka.actions.gesture

import android.graphics.Path
import android.graphics.Point
import com.example.clicka.extensions.nextIntInOffset
import com.example.clicka.extensions.safeLineTo
import com.example.clicka.extensions.safeMoveTo



import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random
import kotlin.random.nextInt


internal const val RANDOMIZATION_POSITION_MAX_OFFSET_PX = 5
internal const val RANDOMIZATION_POSITION_MIN_OFFSET_PX = 5L
internal const val MINIMUM_STROKE_DURATION_MS = 1L
internal const val MAXIMUM_STROKE_DURATION_MS = 40L

fun Path.moveTo(position: Point, random : Random?){

    if(random == null) safeMoveTo(position.x,position.y)
    else safeMoveTo(
        random.nextIntInOffset(position.x, RANDOMIZATION_POSITION_MAX_OFFSET_PX),
        random.nextIntInOffset(position.y,RANDOMIZATION_POSITION_MAX_OFFSET_PX),
    )
}

fun Path.line(from: Point?,to:Point?,random: Random){
    if (from==null || to==null) return

    moveTo(from,random)
    lineTo(to,random)
}

private fun Path.lineTo(position: Point,random: Random){
    if(random == null) safeLineTo(position.x,position.y)
    else safeLineTo(
        random.nextIntInOffset(position.x,RANDOMIZATION_POSITION_MAX_OFFSET_PX),
        random.nextIntInOffset(position.y,RANDOMIZATION_POSITION_MAX_OFFSET_PX)
    )
}

