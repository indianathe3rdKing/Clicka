package com.example.clicka.services.accessibilty

// Android
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Point
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityEvent

// Kotlinx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf

// Project
import com.example.clicka.actions.gesture.GestureExecutor
import com.example.clicka.base.AndroidExecutor
import com.example.clicka.base.identifier.Identifier
import com.example.clicka.engine.ActionExecutor
import com.example.clicka.engine.Engine
import com.example.clicka.domain.model.Action
import com.example.clicka.domain.model.Scenario
import com.example.clicka.domain.repository.IScenarioRepository
import com.example.clicka.data.database.ScenarioWithActions

private const val TAG = "AutoClickService"

class AutoClickAccessibilityService : AccessibilityService() {

    companion object {
        @Volatile
        var instance: AutoClickAccessibilityService? = null
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val gestureExecutor = GestureExecutor()

    // Keep a reference to a running Engine so we can stop/release it
    private var engineInstance: Engine? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        Log.i(TAG, "Accessibility service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // no-op
    }

    override fun onInterrupt() {
        Log.w(TAG, "Accessibility service interrupted")
    }

    override fun onDestroy() {
        instance = null
        stopAutoClick()
        serviceScope.cancel()
        super.onDestroy()
    }

    /**
     * Check if auto-click scenario is currently running.
     */
    fun isRunning(): Boolean = engineInstance?.isRunning?.value == true

    /**
     * Stop any running auto-click scenario.
     */
    fun stopAutoClick() {
        engineInstance?.let { eng ->
            if (eng.isRunning.value) {
                eng.stopScenario()
            }
            eng.release()
            engineInstance = null
            Log.i(TAG, "Auto click stopped")
        }
    }

    /**
     * Start auto-clicking at multiple positions using the Engine architecture.
     *
     * @param positions List of (x, y) coordinates to click in order
     * @param cycleDelayMs Delay between full cycles of all positions
     * @param clickDelayMs Delay between individual clicks
     * @param pressDurationMs How long each click press lasts
     * @param randomize Whether to randomize timing
     */
    fun startAutoClickWithPositions(
        positions: List<Pair<Int, Int>>,
        cycleDelayMs: Long = 1000L,
        clickDelayMs: Long = 100L,
        pressDurationMs: Long = 50L,
        randomize: Boolean = false
    ) {
        try {
            // If an engine is already running, stop it first
            if (isRunning()) {
                stopAutoClick()
            }

            if (positions.isEmpty()) {
                Log.w(TAG, "No positions provided for auto-click")
                return
            }

            val scenario = buildClickScenario(positions, cycleDelayMs, clickDelayMs, pressDurationMs, randomize)
            val engine = createEngineWithScenario(scenario)

            engine.init(scenario)
            engine.startScenario()
            engineInstance = engine

            Log.i(TAG, "Auto click started for ${positions.size} positions using Engine")
        } catch (t: Throwable) {
            Log.w(TAG, "startAutoClickWithPositions failed", t)
        }
    }

    // ==================== Private Helper Methods ====================

    /**
     * Create an Engine instance with the AndroidExecutor adapter and a repository that holds the scenario.
     */
    private fun createEngineWithScenario(scenario: Scenario): Engine {
        val androidExecutor = object : AndroidExecutor {
            override suspend fun executeGesture(gestureDescription: GestureDescription) {
                gestureExecutor.dispatchGesture(this@AutoClickAccessibilityService, gestureDescription)
            }

            override fun executeGlobalAction(globalAction: Int) {
                this@AutoClickAccessibilityService.performGlobalAction(globalAction)
            }
        }

        val actionExecutor = ActionExecutor(androidExecutor)

        // Repository that returns the scenario we created
        val repositoryWithScenario = object : IScenarioRepository {
            override val scenarios = flowOf(listOf(scenario))
            override suspend fun getScenario(dbId: Long) = if (dbId == scenario.id.databaseId) scenario else null
            override fun getScenarioFlow(dbId: Long) = flowOf(if (dbId == scenario.id.databaseId) scenario else null)
            override fun getAllScenarioFlowExcept(dbId: Long) = flowOf<List<Action>>(emptyList())
            override suspend fun addScenario(scenario: Scenario) {}
            override suspend fun addScenarioCopy(scenario: ScenarioWithActions): Long? = null
            override suspend fun addScenarioCopy(scenarioId: Long, copyName: String): Long? = null
            override suspend fun updateScenario(scenario: Scenario) {}
            override suspend fun deleteScenario(scenario: Scenario) {}
            override suspend fun markAsUsed(scenarioId: Identifier) {}
        }

        return Engine(actionExecutor, repositoryWithScenario)
    }

    /**
     * Build a domain Scenario with Click actions for each position.
     */
    private fun buildClickScenario(
        positions: List<Pair<Int, Int>>,
        cycleDelayMs: Long,
        clickDelayMs: Long,
        pressDurationMs: Long,
        randomize: Boolean
    ): Scenario {
        val scenarioId = Identifier(databaseId = System.currentTimeMillis())

        // Create a Click action for each position
        val clickActions = positions.mapIndexed { index, (x, y) ->
            Action.Click(
                id = Identifier(databaseId = scenarioId.databaseId + index + 1),
                scenarioId = scenarioId,
                name = "Click${index + 1}",
                priority = index,
                repeatCount = 1,
                isRepeatInfinite = false,
                repeatDelayMs = clickDelayMs,
                position = Point(x, y),
                pressDurationMs = pressDurationMs
            )
        }

        // Add a pause action at the end of each cycle
        val cycleDelayAction = Action.Pause(
            id = Identifier(databaseId = scenarioId.databaseId + positions.size + 1),
            scenarioId = scenarioId,
            name = "CycleDelay",
            priority = positions.size,
            pauseDurationMs = cycleDelayMs
        )

        val allActions: List<Action> = clickActions + cycleDelayAction

        return Scenario(
            id = scenarioId,
            name = "OverlayAutoClick",
            actions = allActions,
            repeatCount = 0,
            maxDurationMin = 60,
            isDurationInfinite = true,
            randomize = randomize,
            stats = null,
            isRepeatInfinite = true
        )
    }
}