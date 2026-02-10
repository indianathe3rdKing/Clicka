package com.example.clicka.engine

import com.example.clicka.base.identifier.Identifier
import com.example.clicka.domain.model.Action.Click
import com.example.clicka.domain.model.Action.Swipe
import com.example.clicka.domain.model.Action.Pause
import com.example.clicka.domain.model.Action
import com.example.clicka.domain.model.Scenario

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