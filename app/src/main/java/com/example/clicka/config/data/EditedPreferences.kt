package com.example.clicka.config.data

import android.content.Context
import android.content.SharedPreferences


/* @return the shared preferences for the default configuration. */
internal fun Context.getConfigPreferences(): SharedPreferences =
    getSharedPreferences(
        CONFIG_PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

/** @return the default duration for a click press */
internal fun SharedPreferences.getClickPressDurationConfig(default: Long):Long=
    getLong(PREF_LAST_CLICK_PRESS_DURATION, default)

/** Save a new default duration for a click press. */
internal fun SharedPreferences.Editor.putClickPressDurationConfig(durationMs: Long): SharedPreferences.Editor=
    putLong(PREF_LAST_CLICK_PRESS_DURATION, durationMs)

/** return the default repeat count for a click */
internal fun SharedPreferences.getClickRepeatCountConfig(default: Int):Int =
    getInt(PREF_LAST_CLICK_REPEAT_COUNT, default)

/** Save a new default repeat count for a click*/
internal fun SharedPreferences.Editor.putClickRepeatCountConfig(durationMs: Long): SharedPreferences.Editor=
    putLong(PREF_LAST_CLICK_REPEAT_COUNT,durationMs)

/** return the default repeat delay for a click */
internal fun SharedPreferences.getClickRepeatDelayConfig(default: Int):Int =
    getInt(PREF_LAST_CLICK_REPEAT_DELAY, default)

/** Save a new default repeat delay for a click*/
internal fun SharedPreferences.Editor.putClickRepeatDelayConfig(durationMs: Long): SharedPreferences.Editor=
    putLong(PREF_LAST_CLICK_REPEAT_DELAY,durationMs)



/** return the defaul duration for a swipe. */
internal fun SharedPreferences.getSwipeDurationConfig(default: Long): Long =
    getLong(PREF_LAST_SWIPE_DURATION, default)

/** Save the new default duration for a swipe. */
internal fun SharedPreferences.Editor.putSwipeDurationConfig(durationMs: Long): SharedPreferences.Editor=
    putLong(PREF_LAST_SWIPE_DURATION, durationMs)

/** return the defaul repeat count for a swipe. */
internal fun SharedPreferences.getSwipeRepeatCountConfig(default: Int): Int =
    getInt(PREF_LAST_SWIPE_REPEAT_COUNT, default)

/** Save the new default repeat count for a swipe. */
internal fun SharedPreferences.Editor.putSwipeRepeatCountConfig(durationMs: Long): SharedPreferences.Editor=
    putLong(PREF_LAST_SWIPE_REPEAT_COUNT, durationMs)

/** return the defaul repeat delay for a swipe. */
internal fun SharedPreferences.getSwipeRepeatDelayConfig(default: Long): Long =
    getLong(PREF_LAST_SWIPE_REPEAT_DELAY, default)

/** Save the new default repeat delay for a swipe. */
internal fun SharedPreferences.Editor.putSwipeRepeatDelayConfig(durationMs: Long): SharedPreferences.Editor=
    putLong(PREF_LAST_SWIPE_REPEAT_DELAY, durationMs)

/** return the defaul repeat count for a swipe. */
internal fun SharedPreferences.getPauseDurationConfig(default: Long): Long =
    getLong(PREF_LAST_PAUSE_DURATION, default)

/** Save the new default repeat count for a swipe. */
internal fun SharedPreferences.Editor.putPauseDurationConfig(durationMs: Long): SharedPreferences.Editor=
    putLong(PREF_LAST_PAUSE_DURATION, durationMs)

/** Name of the preference file */
private const val CONFIG_PREFERENCES_NAME = "config_preferences"
/** User last click press duration key in the SharedPreferences. */
private const val PREF_LAST_CLICK_PRESS_DURATION = "last_click_press_duration"
/** User last click repeat count key in the SharedPreferences. */
private const val PREF_LAST_CLICK_REPEAT_COUNT="last_click_repeat_count"
/** User last click repeat delay key in the SharedPreferences. */
private const val PREF_LAST_CLICK_REPEAT_DELAY="last_click_repeat_delay"
/** User last swipe duration key in the SharedPreferences. */
private const val PREF_LAST_SWIPE_DURATION="last_swipe_duration"
/** User last swipe repeat key in the SharedPreferences. */
private const val PREF_LAST_SWIPE_REPEAT_COUNT="last_swipe_repeat_count"
/** User last swipe repeat delay key in the SharedPreferences. */
private const val PREF_LAST_SWIPE_REPEAT_DELAY="last_swipe_repeat_delay"
/** User last pause duration key in the SharedPreferences. */
private const val PREF_LAST_PAUSE_DURATION="last_pause_duration"