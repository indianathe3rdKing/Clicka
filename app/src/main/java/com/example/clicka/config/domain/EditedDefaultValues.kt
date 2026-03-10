package com.example.clicka.config.domain

import android.content.Context
import com.example.clicka.R
import com.example.clicka.config.data.getClickPressDurationConfig
import com.example.clicka.config.data.getClickRepeatCountConfig
import com.example.clicka.config.data.getClickRepeatDelayConfig
import com.example.clicka.config.data.getConfigPreferences
import com.example.clicka.config.data.getPauseDurationConfig
import com.example.clicka.config.data.getSwipeDurationConfig
import com.example.clicka.config.data.getSwipeRepeatCountConfig
import com.example.clicka.config.data.getSwipeRepeatDelayConfig


internal fun Context.getDefaultClickName(): String =
    getString(R.string.default_dumb_click_name)

internal fun Context.getDefaultClickDurationMs(): Long =
    getConfigPreferences().getClickPressDurationConfig(resources.getInteger(
        R.integer.default_dumb_click_press_duration
    ).toLong())

internal fun Context.getDefaultClickRepeatCount(): Int = getConfigPreferences()
    .getClickRepeatCountConfig(1)

internal fun Context.getDefaultClickRepeatDelay(): Long = getConfigPreferences()
    .getClickRepeatDelayConfig(0)

internal fun Context.getDefaultSwipeName(): String=
    getString(R.string.default_dumb_swipe_name  )
internal fun Context.getDefaultSwipeDurationMs(): Long = getConfigPreferences()
    .getSwipeDurationConfig(resources.getInteger(R.integer.default_dumb_swipe_duration).toLong())

internal fun Context.getDefaultSwipeRepeatCount():Int = getConfigPreferences()
    .getSwipeRepeatCountConfig(1)

internal fun Context.getDefaultSwipeRepeatDelay(): Long= getConfigPreferences()
    .getSwipeRepeatDelayConfig(0)

internal fun Context.getDefaultPauseName(): String= getString(R.string.default_dumb_pause_name)

internal fun Context.getDefaultPauseDurationMs(): Long= getConfigPreferences()
    .getPauseDurationConfig(resources.getInteger(R.integer.default_dumb_pause_duration).toLong())
