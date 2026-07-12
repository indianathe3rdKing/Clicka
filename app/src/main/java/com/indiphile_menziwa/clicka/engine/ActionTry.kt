package com.indiphile_menziwa.clicka.engine

import com.indiphile_menziwa.clicka.base.identifier.Identifier
import com.indiphile_menziwa.clicka.domain.model.Action.Click
import com.indiphile_menziwa.clicka.domain.model.Action.Swipe
import com.indiphile_menziwa.clicka.domain.model.Action.Pause
import com.indiphile_menziwa.clicka.domain.model.Action
import com.indiphile_menziwa.clicka.domain.model.Scenario

internal fun Action.toScenarioTry(): Scenario {
    val scenarioId = Identifier(databaseId = 1L)

    return Scenario(
        id = scenarioId,
        name = "Try",
        repeatCount = 1,
        isRepeatInfinite = false,
        maxDurationMin = 1,
        isDurationInfinite = false,
        randomize = false,
        actions = listOf(toFiniteAction(scenarioId))

    )
}

private fun Action.toFiniteAction(scenarioId: Identifier): Action =
    when (this) {
        is Click -> copy(
            scenarioId = scenarioId,
            isRepeatInfinite = false
        )

        is Swipe -> copy(
            scenarioId = scenarioId,
            isRepeatInfinite = false
        )

        is Pause -> copy(
            scenarioId = scenarioId,

            )
    }