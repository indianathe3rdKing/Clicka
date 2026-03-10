package com.example.clicka.engine

import android.graphics.Point


data class AutoClickConfig(
    val clickDelayMs:Long,
    val pressDuration: Long,
    val cycleDelay: Long,
    val positions: List<Point>,
    val repeatCount:Int =1,
    val randomize:Boolean = false
)