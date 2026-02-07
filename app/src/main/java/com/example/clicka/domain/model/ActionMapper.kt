package com.example.clicka.domain.model

import android.graphics.Point

import com.example.clicka.base.identifier.DATABASE_ID_INSERTION
import com.example.clicka.base.identifier.Identifier
import com.example.clicka.data.database.ActionEntity
import com.example.clicka.data.database.ActionType


internal fun ActionEntity.toDomain(asDomain: Boolean=false): Action=when(type){
    ActionType.CLICK-> toDomainClick(asDomain)
    ActionType.SWIPE-> toDomainSwipe(asDomain)
    ActionType.PAUSE-> toDomainPause(asDomain)

}

internal fun Action.toEntity(scenarioId: Long = DATABASE_ID_INSERTION):ActionEntity=when(this){
    is Action.Click-> toClickEntity(scenarioId)
    is Action.Swipe-> toSwipeEntity(scenarioId)
    is Action.Pause-> toPauseEntity(scenarioId)
}

private fun ActionEntity.toDomainClick(asDomain:Boolean):Action.Click=
    Action.Click(
        id = Identifier(id = id,asTemporary = asDomain),
        scenarioId = Identifier(id= scenarioId, asTemporary = asDomain),
        name = name,
        priority = priority,
        position = Point(x!!,y!!),
        pressDurationMs = pressDuration!!,
        repeatCount = repeatCount!!,
        isRepeatInfinite = isRepeatInfinite!!,
        repeatDelayMs = repeatDelay!!,
    )

private fun ActionEntity.toDomainSwipe(asDomain: Boolean): Action.Swipe =
    Action.Swipe(
        id = Identifier(id = id,asTemporary = asDomain),
        scenarioId = Identifier(id = scenarioId,asTemporary = asDomain),
        name = name,
        priority = priority,
        fromPosition = Point(fromX!!,fromY!!),
        toPosition = Point(toX!!,toY!!),
        swipeDurationMs = swipeDuration!!,
        repeatCount = repeatCount!!,
        isRepeatInfinite = isRepeatInfinite!!,
        repeatDelayMs = repeatDelay!!,
)

private fun ActionEntity.toDomainPause(asDomain: Boolean): Action.Pause=
    Action.Pause(
        id = Identifier(id=id, asTemporary = asDomain),
        scenarioId = Identifier(id= scenarioId, asTemporary = asDomain),
        name=name,
        priority = priority,
        pauseDurationMs = pauseDuration!!,
    )

private fun Action.Click.toClickEntity(scenarioDbId: Long):ActionEntity{
    if (!isValid()) throw IllegalArgumentException("Can't transform to entity, Click is incompete")

    return ActionEntity(
        id= id.databaseId,
        scenarioId= if (scenarioDbId != DATABASE_ID_INSERTION) scenarioDbId else scenarioId.databaseId,
        name= name,
        priority=priority,
        type= ActionType.CLICK,
        repeatCount= repeatCount,
        repeatDelay = repeatDelayMs,
        pressDuration= pressDurationMs,
        x= position.x,
        y= position.y,
        isRepeatInfinite = isRepeatInfinite,


    )
}

private fun Action.Swipe.toSwipeEntity(scenarioDbId: Long): ActionEntity{
    if (!isValid()) throw IllegalArgumentException("Can't transform to entity, Swipe is incomplete")

    return ActionEntity(
        id= id.databaseId,
        scenarioId= if (scenarioDbId != DATABASE_ID_INSERTION ) scenarioDbId else scenarioId.databaseId,
        name= name,
        priority=priority,
        type= ActionType.SWIPE,
        repeatCount = repeatCount,
        isRepeatInfinite=isRepeatInfinite,
        repeatDelay = repeatDelayMs,
        swipeDuration = swipeDurationMs,
        fromX = fromPosition.x,
        fromY= fromPosition.y,
        toX= toPosition.x,
        toY= toPosition.y,
    )
}

private fun Action.Pause.toPauseEntity(scenarioDbId:Long):ActionEntity{
    if (!isValid()) throw IllegalArgumentException("Can't transform to entity, Pause is incomplete")

    return ActionEntity(
        id=id.databaseId,
        scenarioId= if (scenarioDbId != DATABASE_ID_INSERTION) scenarioDbId else scenarioId.databaseId,
        name=name,
        priority= priority,
        type = ActionType.PAUSE,
        pauseDuration= pauseDurationMs,
    )
}


