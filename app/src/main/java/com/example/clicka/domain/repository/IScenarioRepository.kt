package com.example.clicka.domain.repository

import com.example.clicka.base.identifier.Identifier
import com.example.clicka.data.database.ScenarioWithActions
import com.example.clicka.domain.model.Action
import com.example.clicka.domain.model.Scenario
import kotlinx.coroutines.flow.Flow

interface IScenarioRepository{
    val scenarios: Flow<List<Scenario>>

    suspend fun getScenario(dbId: Long): Scenario?

    fun getScenarioFlow(dbId: Long): Flow<Scenario?>

    fun getAllScenarioFlowExcept(dbId: Long): Flow<List<Action>>

    suspend fun addScenario(scenario: Scenario)

    suspend fun addScenarioCopy(scenario: ScenarioWithActions):Long?

    suspend fun addScenarioCopy(scenarioId: Long,copyName: String): Long?

    suspend fun updateScenario(scenario: Scenario)

    suspend fun deleteScenario(scenario: Scenario)

    suspend fun markAsUsed(scenarioId: Identifier)
}