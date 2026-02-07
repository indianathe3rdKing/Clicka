package com.example.clicka.domain.model

import android.graphics.Point
import com.example.clicka.base.identifier.Identifier
import com.example.clicka.interfaces.Identifiable


sealed class Action : Identifiable {

    abstract val scenarioId: Identifier

    abstract val name: String?

    abstract val priority: Int

    abstract fun isValid(): Boolean


    data class Click(
        override val id: Identifier,
        override val scenarioId: Identifier,
        override val name: String,
        override val priority: Int,
        override val repeatCount: Int,
        override val isRepeatInfinite: Boolean,
        override val repeatDelayMs: Long,
        val position: Point,
        val pressDurationMs: Long,

        ) : Action(), RepeatableWithDelay {
        override fun isValid(): Boolean =
            name.isNotEmpty() && pressDurationMs > 0 &&
                    isRepeatCountValid() && isRepeatDelayValid()
    }

    data class Swipe(
        override val id: Identifier,
        override val scenarioId: Identifier,
        override val name: String,
        override val priority: Int,
        override val repeatCount: Int,
        override val isRepeatInfinite: Boolean,
        override val repeatDelayMs: Long,
        val fromPosition: Point,
        val toPosition: Point,
        val swipeDurationMs: Long,

        ) : Action(), RepeatableWithDelay {
        override fun isValid(): Boolean =
            name.isNotEmpty() && swipeDurationMs > 0 && isRepeatCountValid() &&
                    isRepeatDelayValid()
    }

    data class Pause(
        override val id: Identifier,
        override val scenarioId: Identifier,
        override val name: String,
        override val priority: Int,
        val pauseDurationMs: Long,
    ) : Action() {
        override fun isValid(): Boolean = name.isNotEmpty()
    }
}