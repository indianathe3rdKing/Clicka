package com.example.clicka.domain.repository

import com.example.clicka.base.identifier.Identifier
import com.example.clicka.data.database.ScenarioDataSource
import com.example.clicka.data.database.ScenarioWithActions
import com.example.clicka.domain.model.Action
import com.example.clicka.domain.model.Scenario
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScenarioRepository @Inject constructor(
    private val scenarioDataSource: ScenarioDataSource,
) : IScenarioRepository{

    override val scenarios: Flow<List<Scenario>> =
        scenarioDataSource.getAllScenarios

    override suspend fun getScenario(dbId: Long): Scenario? {
        scenarioDataSource.getScenario(dbId)
    }

    override fun getScenarioFlow(dbId: Long): Flow<Scenario?> {
        scenarioDataSource.getScenarioFlow(dbId)
    }

    override fun getAllScenarioFlowExcept(dbId: Long): Flow<List<Action>> {
        TODO("Not yet implemented")
    }

    override suspend fun addScenario(scenario: Scenario) {
        TODO("Not yet implemented")
    }

    override suspend fun addScenarioCopy(scenario: ScenarioWithActions): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun addScenarioCopy(
        scenarioId: Long,
        copyName: String
    ): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun updateScenario(scenario: Scenario) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteScenario(scenario: Scenario) {
        TODO("Not yet implemented")
    }

    override suspend fun markAsUsed(scenarioId: Identifier) {
        TODO("Not yet implemented")
    }
}