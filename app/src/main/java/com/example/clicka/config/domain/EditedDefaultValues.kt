package com.example.clicka.config.domain

import android.content.Context
import com.example.clicka.R
import com.example.clicka.config.data.*


internal fun Context.getDefaultClickName(): String =
    getString(R.string.default_dumb_click_name)

internal fun Context.getDefaultClickDurationMs(): Long =
    this.getConfigPreferences().getClickClickPressDurationConfig(resources.getInteger(
        R.integer.default_dumb_click_press_duration
    ).toLong())

internal fun Context.getDefaultClickRepeatCount(): Int = this.getConfigPreferences()
    .getClickRepeatCountConfig(1)

internal fun Context.getDefaultClickRepeatDelay(): Long = this.getConfigPreferences()
    .getClickRepeatDelayConfig(0)

internal fun Context.getDefaultSwipeName(): String =
    getString(R.string.default_dumb_swipe_name)

internal fun Context.getDefaultSwipeDurationMs(): Long = this.getConfigPreferences()
    .getSwipeDurationConfig(resources.getInteger(R.integer.default_dumb_swipe_duration).toLong())

internal fun Context.getDefaultSwipeRepeatCount(): Int = this.getConfigPreferences()
    .getSwipeRepeatCountConfig(1).toInt()

internal fun Context.getDefaultSwipeRepeatDelay(): Long = this.getConfigPreferences()
    .getSwipeRepeatRepeatDelayConfig(0)

internal fun Context.getDefaultPauseName(): String = getString(R.string.default_dumb_pause_name)

internal fun Context.getDefaultPauseDurationMs(): Long = this.getConfigPreferences()
    .getPauseDurationConfig(resources.getInteger(R.integer.default_dumb_pause_duration).toLong())
