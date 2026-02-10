package com.example.clicka.data.database

import android.util.Log
import com.example.clicka.base.identifier.DATABASE_ID_INSERTION
import com.example.clicka.base.workarounds.DatabaseListUpdater

import com.example.clicka.domain.model.Action
import com.example.clicka.domain.model.Scenario
import com.example.clicka.domain.model.toDomain
import com.example.clicka.domain.model.toEntity
import com.example.clicka.extensions.mapList

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScenarioDataSource @Inject constructor(
    database: Database
){
    private val scenarioDao: ScenarioDao = database.ScenarioDao()

    private val actionUpdater = DatabaseListUpdater<Action, ActionEntity>()

    val getAllScenarios: Flow<List<Scenario>> =
        scenarioDao.getScenariosWithActionsFlow()
            .mapList{it.toDomain()}

    suspend fun getScenario(dbId: Long): Scenario?=
        scenarioDao.getScenariosWithAction(dbId)
            ?.toDomain()

    fun getScenarioFlow(dbId:Long): Flow<Scenario?> =
        scenarioDao.getScenariosWithActionFlow(dbId)
            .map{ it?.toDomain()}

    fun getAllActionsExcept(scenarioId: Long): Flow<List<Action>> =
        scenarioDao.getAllActionsExcept(scenarioId)
            .mapList{it.toDomain()}

    suspend fun addScenario(scenario: Scenario){
        Log.d(TAG,"Add scenario $scenario")

        updateScenarioActions(
            scenarioDbId = scenarioDao.addScenario(scenario.toEntity()),
            actions = scenario.actions,
        )
    }

    suspend fun addScenarioCopy(scenarioDbId: Long,copyName: String?): Long? =
        scenarioDao.getScenariosWithAction(scenarioDbId)?.let { scenarioWithActions ->
            addScenarioCopy(scenarioWithActions,copyName)
        }

    suspend fun addScenarioCopy(scenarioWithActions: ScenarioWithActions,copyName: String? = null): Long?{
        Log.d(TAG,"Add scenario to copy ${scenarioWithActions.scenario}")

        return try {
            val scenarioId = scenarioDao.addScenario(
                scenarioWithActions.scenario.copy(
                    id= DATABASE_ID_INSERTION,
                    name = copyName?: scenarioWithActions.scenario.name,
                )
            )

            scenarioDao.addActions(
                scenarioWithActions.actions.map { action->
                    action.copy(
                        id = DATABASE_ID_INSERTION,
                        scenarioId= scenarioId,
                    )
                }
            )
            scenarioId
        }catch (ex: kotlin.Exception){
            Log .e(TAG,"Error while inserting scenario copy", ex)
            null
        }
    }

    suspend fun markAsUsed(scenarioId: Long){
        val previousStats = scenarioDao.getScenarioStats(scenarioId)
        if(previousStats != null){
            scenarioDao.updatedScenarioStats(
                previousStats.copy(
                    lastStartTimestampMs = System.currentTimeMillis(),
                    startCount = previousStats.startCount+1,
                )
            )
        }else{
            scenarioDao.addScenarioStats(
                ScenarioStatsEntity(
                    id = DATABASE_ID_INSERTION,
                    scenarioId = scenarioId,
                    lastStartTimestampMs = System.currentTimeMillis(),
                    startCount = 1,
                )
            )
        }
    }

    suspend fun updateScenario(scenario: Scenario){
        Log.d(TAG,"Update scenario $scenario")
        val scenarioEntity = scenario.toEntity()

        scenarioDao.updateScenario(scenarioEntity)
        updateScenarioActions(
            scenarioEntity.id,scenario.actions
        )
    }

    private suspend fun updateScenarioActions(scenarioDbId: Long,actions: List<Action>){
        val updater = DatabaseListUpdater<Action, ActionEntity>()
        updater.refreshUpdateValues(
            currentEntities = scenarioDao.getActions(scenarioDbId),
            newItems = actions,
            mappingClosure ={action -> action.toEntity(scenarioDbId)}
        )
        Log.d(TAG, "Actions updater: $actionUpdater")

        updater.executeUpdate(
            addList = scenarioDao::addActions,
            updateList = scenarioDao::updateActions,
            removeList  = scenarioDao::deleteActions,
        )
    }

    suspend fun deleteScenario(scenario: Scenario){
        Log.d(TAG,"Delete scenario $scenario")

        scenarioDao.deleteScenario(scenario.id.databaseId)
    }



}

private const val TAG = "ScenarioDataSource"