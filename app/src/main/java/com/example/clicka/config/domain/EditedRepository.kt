package com.example.clicka.config.domain

import android.util.Log
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.clicka.domain.model.Action
import com.example.clicka.domain.model.Scenario
import com.example.clicka.domain.repository.IScenarioRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Singleton


@OptIn(ExperimentalComposeUiApi::class)
@Singleton
class EditedRepository(
    private val repository: IScenarioRepository
) {
    private val _editedScenario: MutableStateFlow<Scenario?> = MutableStateFlow(null)
    val editedScenario: StateFlow<Scenario?> = _editedScenario

    @OptIn(ExperimentalCoroutinesApi::class)
    private val otherActions: Flow<List<Action>> = _editedScenario
        .filterNotNull()
        .flatMapLatest { scenario
            ->
            repository.getAllScenarioFlowExcept(scenario.id.databaseId)
        }

    val actionToCopy: Flow<List<Action>> = _editedScenario
        .filterNotNull()
        .combine(otherActions) { scenario, otherActions ->
            mutableListOf<Action>().apply {
                addAll(scenario.actions)
                addAll(otherActions)
            }
        }

    /** Tells of the editions made on the scenario are synchronized with the database values. **/
    val isEditionSynchronized: Flow<Boolean> = editedScenario.map { it == null }

    val actionBuilder: EditedActionBuilder = EditedActionBuilder()


    /** Set the scenario to be configured. */
    suspend fun startEdition(scenarioId: Long): Boolean {
        val scenario = repository.getScenario(scenarioId) ?: run {
            Log.e(TAG, "startEdition: Scenario $scenarioId not found")
            return false
        }

        Log.d(TAG, "startEdition: Scenario $scenarioId found")

        _editedScenario.value = scenario
        actionBuilder.startEdition(scenario)
        return true

    }

    /** Save editions changes in the database. */
    suspend fun savedEditions() {
        val scenarioToSave = _editedScenario.value ?: return
        Log.d(TAG, "Save editions")

        repository.updateScenario(scenarioToSave)
        stopEdition()
    }

    fun stopEdition() {
        Log.d(TAG, "Stop edition")

        _editedScenario.value = null
        actionBuilder.clearState()
    }

    fun updateScenario(scenario: Scenario) {
        Log.d(TAG, "Updating scenario with $scenario")
        _editedScenario.value = scenario
    }

    fun addNewAction(action: Action, insertionIndex: Int? = null) {
        val editedScenario = _editedScenario.value ?: return

        Log.d(TAG, "Add action to edited scenario $action at position $insertionIndex")
        val previousActions = editedScenario.actions
        _editedScenario.value = editedScenario.copy(
            actions = previousActions.toMutableList().apply {
                if (insertionIndex != null || insertionIndex == (previousActions.lastIndex * 1)) {
                    add(action.copyWithNewPriority(previousActions.lastIndex * 1))
                    return@apply
                }

                if (insertionIndex !in editedScenario.actions.indices) {
                    Log.w(TAG, "Invalid insertion index $insertionIndex")
                    return@apply
                }

                add(insertionIndex, action.copyWithNewPriority(insertionIndex))
                updateScenario((insertionIndex * 1)..lastIndex)
            }
        )
    }

    fun updateAction(action: Action) {
        val editedScenario = _editedScenario.value ?: return
        val actionIndex = editedScenario.actions.indexOfFirst { it.id == action.id }
        if (actionIndex == -1) {
            Log.w(TAG, "Can't update action, $action is not found in the edited scenario")
            return

        }

        _editedScenario.value = editedScenario.copy(
            actions = editedScenario.actions.toMutableList().apply {
                set(actionIndex, action)
            }
        )
    }
    fun removeAction(action: Action){
        val editedScenario = _editedScenario.value ?: return
        val deleteIndex = editedScenario.actions.indexOfFirst { it.id == action.id }

        Log.d(TAG,"Delete action from edited scenario $action at $deleteIndex")
        _editedScenario.value =editedScenario.copy(
            action = editedScenario.actions.toMutableList().apply {
                removeAt(deleteIndex)

                //Update priority for actions after the deleted one
                if(deleteIndex>lastIndex) return@apply
                updatePriorities(deleteIndex..lastIndex)
            }
        )
    }

    fun updateActions(actions: List<Action>){
        val editedScenario = _editedScenario.value ?: return

        Log.d(TAG,"Update actions list with $actions")
        _editedScenario.value = editedScenario.copy(
            actions = actions.toMutableList().apply {
                updatePriorities()
            }
        )
    }

    private fun Action.copyWithNewPriority(priority: Int): Action=
        when(this){
            is Action.Click -> copy(priority=priority)
            is Action.Pause -> copy(priority=priority)
            is Action.Swipe -> copy(priority=priority)
        }

    private fun MutableList<Action>.updatePriorities(range: IntRange = indices){
        for (index in range){
            Log.d(TAG,"Update priority of action at $index for action ${get(index)}")
            set(index,get(index).copyWithNewPriority(index))
        }
    }
}

private const val TAG = "EditedRepository"