package com.indiphile_menziwa.clicka.domain.repository

import com.indiphile_menziwa.clicka.base.identifier.Identifier
import com.indiphile_menziwa.clicka.data.database.ScenarioWithActions
import com.indiphile_menziwa.clicka.domain.model.Action
import com.indiphile_menziwa.clicka.domain.model.Scenario
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