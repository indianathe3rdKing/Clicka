package com.indiphile_menziwa.clicka.actions.utlis


import com.indiphile_menziwa.clicka.ui.extensions.nextLongInOffset
import kotlin.random.Random

fun Long.getPauseDurationMs(random: Random?):Long=
    random?.nextLongInOffset(this, RANDOMIZATION_DURATION_MAX_OFFSET_MS)?:this