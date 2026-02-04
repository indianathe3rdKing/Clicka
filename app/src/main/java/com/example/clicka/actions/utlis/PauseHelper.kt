package com.example.clicka.actions.utlis


import com.example.clicka.extensions.nextLongInOffset
import kotlin.random.Random

fun Long.getPauseDurationMs(random: Random?):Long=
    random?.nextLongInOffset(this, RANDOMIZATION_DURATION_MAX_OFFSET_MS)?:this