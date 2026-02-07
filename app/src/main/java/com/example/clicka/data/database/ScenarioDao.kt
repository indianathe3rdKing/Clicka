package com.example.clicka.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ScenarioDao{
    /*
    *Get all scenarios and their actions.
    * @return the flow on the list of scenarios.
    * 1
     */
    @Transaction
    @Query("SELECT * FROM scenario_table ORDER BY name ASC")
    fun geScenariosWithActionsFlow(): Flow<List<ScenarioWithActions>>
/*
    *Get all scenarios and their actions.
    *
    * @return the scenario if found,null if not.
    * 2
 */
    @Transaction
    @Query("SELECT * FROM scenario_table WHERE id= :dbId")
    suspend fun getScenariosWithActions(dbId: Long): ScenarioWithActions?

/*
    *Get the specified scenario with its actions
    * return the scenario if found, null if not.
    * 3
 */
    @Transaction
    @Query("SELECT * FROM scenario_table WHERE id=:dbId")
        fun getScenariosWithActionFlow(dbId: Long): Flow<ScenarioWithActions?>

/*
       *Get the actions of a scenario,ordered by their priority.
       * 4
 */
    @Transaction
    @Query("SELECT * FROM action_table WHERE scenario_id!=:ScenarioId")
    suspend fun getAllActions(scenarioId: Long): Flow<List<ActionEntity>>

/*
    *Get the actions for a scenario, ordered by their priority.
    *5
 */

    @Query("SELECT * FROM action_table WHERE scenario_id=:ScenarioId ORDER BY priority ASC")
    fun getDumbActions(dumbScenarioId: Long): List<ActionEntity>

/*
    *Add a new scenario to the database.
    *
    * @param scenario the scenario to add.
    * 6
 */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addScenario(Scenario: ScenarioEntity): Long

/*
    *Add a new action to the database.
    *
    * @param Scenario the scenario to add.
    * 7
 */
    @Update
    suspend fun updateScenario(Scenario: ScenarioEntity)

/*
    *Delete a scenario from the database.
    *
    * @param Scenario the scenario to delete.
    * 8
 */
    @Delete
    suspend fun deleteAction(Actions: List<ActionEntity>)
/*
    *Get a scenario stats
    *
    * @param stats the stats to be added.
    * 9
 */
    @Query("SELECT * FROM scenario_stats_table WHERE id=:scenarioId")
    suspend fun getScanarioStats(scenarioId: Long): ScenarioStatsEntity?
/*
    *Add a scenario stats.
    *
    * @param stats the stats to be added.
    * 10
 */

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addScenarioStats(stats: ScenarioStatsEntity)
/*
    *Update the stats for a scenario
    *
    * @param stats the stats to be updated.
 */
    @Update
    suspend fun updatedScenarioStats(stats:ScenarioStatsEntity)
}
