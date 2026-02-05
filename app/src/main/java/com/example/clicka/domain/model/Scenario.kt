package com.example.clicka.domain.model

import com.example.clicka.base.identifier.Identifier
import com.example.clicka.interfaces.Identifiable

data class Scenario(
    override val id: Identifier,
    val name: String,
    val Actions: List<Action> = emptyList(),
    override val repeatCount:Int,

    val maxDuration:Int,
    val isDurationMin:Int,
    val isDurationInfinite: Boolean,
    val randomize: Boolean,
    val state: ScenarioStats?=null,
    override val isRepeatIfinite: Boolean,

    ): Identifiable,Repeatable{
    fun isValid():Boolean = name.isNotEmpty() && Actions.isNotEmpty()
}




const val SCENARIO_MIN_DURATION_MINUTES = 1
const val SCENARIO_MAX_DURATION_MINUTES =1440