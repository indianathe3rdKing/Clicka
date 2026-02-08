package com.example.clicka.domain.model

import com.example.clicka.base.ScenarioStats
import com.example.clicka.base.identifier.Identifier
import com.example.clicka.interfaces.Identifiable

data class Scenario(
    override val id: Identifier,
    val name: String,
    val actions: List<Action> = emptyList(),
    override val repeatCount:Int,
    val maxDurationMin:Long,
    val isDurationInfinite: Boolean,
    val randomize: Boolean,
    val stats: ScenarioStats?=null,
    override val isRepeatInfinite: Boolean,
    ): Identifiable,Repeatable{
    fun isValid():Boolean = name.isNotEmpty() && actions.isNotEmpty()
}




const val SCENARIO_MIN_DURATION_MINUTES = 1
const val SCENARIO_MAX_DURATION_MINUTES =1440