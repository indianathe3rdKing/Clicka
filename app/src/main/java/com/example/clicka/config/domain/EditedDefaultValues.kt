package com.example.clicka.config.domain

import android.content.Context
import com.example.clicka.R


internal fun Context.getDefaultClickName(): String =
    getString(R.string.default_dumb_click_name)

internal fun Context.getDefaultClickDurationMs(): Long =
    getConfigPrefences().getClickPressDurationConfig(resources.getInteger(
        R.integer.default_dumb_click_press_duration
    ).toLong())

internal fun Context.getDefaultClickRepeatCount(): Int = getConfigPreferences()
    .getClickRepeatCountConfig(1)

internal fun Context.getDefaultClickRepeatDelay(): Long = getConfigPreferences()
    .getClickRepeatDelayCConfig(0)

internal fun Context.getDefaultSwipeName(): String=
    getString(R.string.default_dumb_swipe_name  )

internal fun Context.getDefaultSwipeDurationMs(): Long = getConfigPreferences()
    .getSwipeDurationConfig(resources.getInteger(R.integer.default_swipe_duration).toLong())

internal fun Context.getDefaultSwipeRepeatCount():Int = getConfigPreferences()
    .getSwipeRepeatCountConfig(1)

internal fun Context.getDefaultSwipeRepeatDelay(): Long= getConfigPreferences()
    .getSwipeRepeatDelayConfig(0)

internal fun Context.getDefaultPauseName(): String= getString(R.string.default_pause_name)

internal fun Context.getDefaultPauseDurationMs(): Long= getConfigPreferences()
    .getPauseDurationConfig(resources.getInteger(R.integer.default_dumb_pause_duration).toLong())
