package com.example.clicka.domain.model




internal fun ScenarioWithActions.toDomain(asDomian: Boolean=false):Scenario =
    Scenario(
        id:Identifier(id= scenario.id, asTemporary= asDomian),
        name= scenario.name,
        repeatCount= scenario.repeatCount,
        isRepeatIfinite = scenario.maxDurationMin,
        maxDurationMin = scenario.maxDurationMin,
        isDurationInfinite= scenario.isDurationInfinite,
    )