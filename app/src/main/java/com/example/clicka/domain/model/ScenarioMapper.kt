package com.example.clicka.domain.model

import com.example.clicka.base.ScenarioStats
import com.example.clicka.base.identifier.Identifier


internal fun ScenarioWithActions.toDomain(asDomain: Boolean=false):Scenario =
    Scenario(
        id= Identifier(id = scenario.id, asTemporary = asDomain),
        name= scenario.name,
        repeatCount= scenario.repeatCount,
        isRepeatIfinite = scenario.maxDurationMin,
        maxDurationMin = scenario.maxDurationMin,
        isDurationInfinite= scenario.isDurationInfinite,
        randomize= scenario,randomize,
        Actions = Actions
        .sortedBy {it.priority}
        .map{ Action ->Action.toDomain(asDomain) },
        stats= stats.toDomain(),
    )

internal fun Scenario.toEntity():ScenarioEntity =
    ScenarioEntity(
        id= id.databaseId,
        name= name,
        repeatCount = repeatCount,
        isRepeatIfinite= isRepeatIfinite,
        maxDurationMin= maxDurationMin,
        isDurationInfinite= isDurationInfinite,
        randomize=randomize
    )

private fun ScenarioStatsEntity?.toDomain() =
    if(this==null) ScenarioStats(
        lastStartTimestamp = 0,
        startCount = 0
    )else ScenarioStats(
        lastStartTimestamp = lastStartTimestamp,
        startCount = startCount,
    )