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
import com.example.clicka.config.domain.EditedActionBuilder
import com.example.clicka.config.domain.getDefaultPauseDurationMs
import com.example.clicka.config.data.getConfigPreferences
import com.example.clicka.config.data.getRandomizeConfig
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
    private val actionBuilder = EditedActionBuilder()

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
     * Uses EditedActionBuilder to create Click actions with config values from SharedPreferences.
     *
     * @param positions List of (x, y) coordinates to click in order
     */
    fun startAutoClickWithPositions(positions: List<Pair<Int, Int>>) {
        try {
            // If an engine is already running, stop it first
            if (isRunning()) {
                stopAutoClick()
            }

            if (positions.isEmpty()) {
                Log.w(TAG, "No positions provided for auto-click")
                return
            }

            // Convert positions to Points
            val points = positions.map { (x, y) -> Point(x, y) }

            // Build scenario using EditedActionBuilder (gets config from SharedPreferences)
            val scenario = buildClickScenarioWithBuilder(points)
            val engine = createEngineWithScenario(scenario)

            engine.init(scenario)
            engine.startScenario()
            engineInstance = engine

            Log.i(TAG, "Auto click started for ${positions.size} positions using Engine + EditedActionBuilder")
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
     * Build a domain Scenario with Click actions using EditedActionBuilder.
     * This uses the config values from SharedPreferences (press duration, repeat count, etc.)
     */
    private fun buildClickScenarioWithBuilder(points: List<Point>): Scenario {
        val scenarioId = Identifier(databaseId = System.currentTimeMillis())

        actionBuilder.startEdition(scenarioId)

        val clickActions = actionBuilder.createNewClick(this, points)

        // Pause between cycles also comes from the same prefs (via defaults)
        val pauseAction = actionBuilder.createNewPause(this).copy(priority = clickActions.size)

        val allActions: List<Action> = clickActions + pauseAction

        // Load randomize setting from SharedPreferences (default: true for anti-bot bypass)
        val randomize = getConfigPreferences().getRandomizeConfig(true)

        return Scenario(
            id = scenarioId,
            name = "OverlayAutoClick",
            actions = allActions,
            repeatCount = 0,
            maxDurationMin = 60,
            isDurationInfinite = true,
            randomize = randomize,  // Use user's randomize preference
            stats = null,
            isRepeatInfinite = true
        )
    }
}