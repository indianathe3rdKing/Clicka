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

    override suspend fun getScenario(dbId: Long): Scenario? =
        scenarioDataSource.getScenario(dbId)


    override fun getScenarioFlow(dbId: Long): Flow<Scenario?> =
        scenarioDataSource.getScenarioFlow(dbId)


    override fun getAllScenarioFlowExcept(dbId: Long): Flow<List<Action>> =
        scenarioDataSource.getAllActionsExcept(dbId)

    override suspend fun addScenario(scenario: Scenario) =
        scenarioDataSource.addScenario(scenario)


    override suspend fun addScenarioCopy(scenario: ScenarioWithActions): Long? =
        scenarioDataSource.addScenarioCopy(scenario)


    override suspend fun addScenarioCopy(
        scenarioId: Long,
        copyName: String
    ): Long? =
        scenarioDataSource.addScenarioCopy(scenarioId,copyName)


    override suspend fun updateScenario(scenario: Scenario) =
        scenarioDataSource.updateScenario(scenario)


    override suspend fun deleteScenario(scenario: Scenario) =
        scenarioDataSource.deleteScenario(scenario)


    override suspend fun markAsUsed(scenarioId: Identifier) =
        scenarioDataSource.markAsUsed(scenarioId.databaseId)

}