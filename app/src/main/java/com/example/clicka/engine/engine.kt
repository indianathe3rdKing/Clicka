package com.example.clicka.engine

import com.example.clicka.domain.model.Scenario
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
    private val actionExecutor: ActionExecutor
){
//  Coroutine scope for the scenario processing
    private var proessingScope: CoroutineScope?= null
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


}