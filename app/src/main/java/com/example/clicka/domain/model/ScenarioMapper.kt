package com.example.clicka.domain.model

import com.example.clicka.base.ScenarioStats
import com.example.clicka.base.identifier.Identifier
import com.example.clicka.data.database.ScenarioEntity
import com.example.clicka.data.database.ScenarioStatsEntity
import com.example.clicka.data.database.ScenarioWithActions


internal fun ScenarioWithActions.toDomain(asDomain: Boolean=false):Scenario =
    Scenario(
        id= Identifier(id = scenario.id, asTemporary = asDomain),
        name= scenario.name,
        repeatCount= scenario.repeatCount,
        isRepeatInfinite = scenario.isRepeatInfinite,
        maxDurationMin = scenario.maxDurationMin,
        isDurationInfinite= scenario.isDurationInfinite,
        randomize= scenario.randomize,
        actions = actions
        .sortedBy {it.priority}
        .map{ action -> action.toDomain(asDomain) },
        stats= stats.toDomain(),
    )

internal fun Scenario.toEntity():ScenarioEntity =
    ScenarioEntity(
        id = id.databaseId,
        name = name,
        repeatCount = repeatCount,
        isRepeatInfinite = isRepeatInfinite,
        maxDurationMin = maxDurationMin,
        isDurationInfinite = isDurationInfinite,
        randomize = randomize
    )

internal fun ScenarioStatsEntity?.toDomain(): ScenarioStats =
    if(this==null) ScenarioStats(
        lastStartTimestamp = 0,
        startCount = 0
    )else ScenarioStats(
        lastStartTimestamp = lastStartTimestampMs,
        startCount = startCount,
    )
