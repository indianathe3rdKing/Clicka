package com.example.clicka.config.domain

import android.content.Context
import android.graphics.Point
import com.example.clicka.base.IdentifierCreator
import com.example.clicka.base.identifier.Identifier
import com.example.clicka.domain.model.Action

class EditedActionBuilder {


    private val actionCreator = IdentifierCreator()
    private  var actionScenarioId: Identifier? =null

    internal fun startEdition(scenarioId: Identifier){
        actionCreator.resetIdCount()
        actionScenarioId=scenarioId
    }

    internal fun clearState(){
        actionCreator.resetIdCount()
        actionScenarioId=null
    }

    fun createNewClick(context: Context,position: Point): Action.Click =
        Action.Click(
            id= actionCreator.generateNewIdentifier(),
            scenarioId = getEditScenarioIdOrThrow(),
            name = context.getDefaultClickName(),
            position = position,
            pressDurationMs = context.getDefaultClickDurationMs(),
            repeatCount = context.getDefaultClickRepeatCount(),
            isRepeatInfinite = false,
            repeatDelayMs = context.getDefaultClickRepeatDelay(),
        )

    fun createNewSwipe(context: Context,from: Point,to: Point): Action.Swipe=
        Action.Swipe(
            id=actionCreator.generateNewIdentifier(),
            scenarioId = getEditScenarioIdOrThrow(),
            name = contDefultSwipeName(),
            fromPosition = from,
            toPosition = to,
            swipeDurationMs = context.getDefaultSwipeDurationMs(),
            repeatCount = context.getDefaultSwipeRepeatCount(),
            isRepeatInfinite = false,
            repeatDelayMs = context.getDefaultSwipeRepeatDelay(),
        )

    fun createNewPause(context: Context): Action.Pause =
        Action.Pause(
            id=actionCreator.generateNewIdentifier(),
            scenarioId = getEditedScenarioIdOrThrow(),
            name = context.getDefaultPauseName(),
            pauseDurationMs = context.getDefaultPauseDurationMs
        )

    fun createNewActionFrom(from: Action): Action =
        when(from){
            is Action.Click -> from.copy(
                id=actionCreator.generateNewIdentifier(),
                scenarioId = getEditScenarioIdOrThrow(),
            )
            is Action.Swipe -> from.copy(
                id=actionCreator.generateNewIdentifier(),
                scenarioId = getEditScenarioIdOrThrow(),

            )
            is Action.Pause -> from.copy(
                id = actionCreator.generateNewIdentifier(),
                scenarioId = getEditScenarioIdOrThrow(),
            )
        }

    private fun getEditedScenarioIdOrThrow: Identifier= actionScenarioId
        ?: throw IllegalStateException("Can't create items without an edited scenario")
}