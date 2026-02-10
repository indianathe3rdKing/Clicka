package com.example.clicka.engine

import android.util.Log
import com.example.clicka.domain.model.Scenario
import com.example.clicka.domain.repository.IScenarioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

import java.io.PrintWriter
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class Engine @Inject constructor(
    private val actionExecutor: ActionExecutor,
    private val repository: IScenarioRepository
){
//  Coroutine scope for the scenario processing
    private var processingScope: CoroutineScope?= null
//  Job for the scenario auto stop
    private var timeoutJob: Job? = null
//  Job for the scenario execute
    private var executionJob:Job?=null
//  Completion listener actions tries
    private var onTryCompletedListener: (()-> Unit)? =null

    private val scenarioDbId: MutableStateFlow<Long?> = MutableStateFlow(null)
    val scenario: Flow<Scenario?> = scenarioDbId.flatMapLatest { dbId ->
        if(dbId == null) flowOf(null)
        else repository.getScenarioFlow(dbId)
    }

    private val _isRunning: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    fun startScenario(){
        if (_isRunning.value) return

        processingScope?.launch {
            scenarioDbId.value?.let { dbId->
                repository.getScenario(dbId)?.let{
                    scenario->
                    startEngine(scenario)
                }
            }
        }
    }


    fun stopScenario(){
        if (!isRunning.value) return
        _isRunning.value=false

        Log.d(TAG,"Stopping scenario")

        timeoutJob?.cancel()
        executionJob?.cancel()
        timeoutJob=null
        executionJob=null

        onTryCompletedListener?.invoke()
        onTryCompletedListener=null

    }

    fun release(){
        if (isRunning.value) stopScenario()
        scenarioDbId.value=null
        processingScope?.cancel()
        processingScope=null

    }

    private fun startEngine(scenario: Scenario){
        if(_isRunning.value || scenario.actions.isEmpty()) return
        _isRunning.value=true

        Log.d(TAG,"startScenario ${scenario.id} with ${scenario.maxDurationMin}")

        if(!scenario.isDurationInfinite) timeoutJob = startTimeoutJob(scenario.maxDurationMin)
        executionJob = startScenarioExecutionJob(scenario)
    }

    private fun startTimeoutJob(timeoutDurationMinutes: Long): Job?=
        processingScope?.launch {
            Log.d(TAG,"startTimeJob: timeoutDurationMinutes= $timeoutDurationMinutes")
            delay(timeoutDurationMinutes.minutes.inWholeMilliseconds)

            processingScope?.launch { stopScenario() }
        }

    private fun startScenarioExecutionJob(scenario: Scenario):Job? =
        processingScope?.launch {
            scenario.repeat {
                scenario.actions.forEach { action ->
                    actionExecutor.executeAction(action,scenario.randomize)
                }
                actionExecutor.onScenarioLoopFinished()
            }
            processingScope?.launch { stopScenario() }
        }



}

private const val TAG = "Engine"