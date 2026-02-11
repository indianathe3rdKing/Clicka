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
        serviceScope.cancel()
        super.onDestroy()
    }

    /**
     * Single click entry point used by the domain engine.
     */
    suspend fun performClickAt(x: Int, y: Int, durationMs: Long = 50L): Boolean {
        val path = android.graphics.Path().apply { moveTo(x.toFloat(), y.toFloat()) }
        val stroke = GestureDescription.StrokeDescription(path, 0, durationMs)
        val gesture = GestureDescription.Builder().addStroke(stroke).build()

        return try {
            val ok = gestureExecutor.dispatchGesture(this, gesture)
            Log.d(TAG, "dispatchGesture result=$ok x=$x y=$y durationMs=$durationMs")
            ok
        } catch (t: Throwable) {
            Log.w(TAG, "dispatchGesture threw x=$x y=$y durationMs=$durationMs", t)
            false
        }
    }

    // Help open accessibility settings if service not enabled
    fun openAccessibilitySettings() {
        Log.i(TAG, "opening accessibility settings")
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    // Accept coordinates so overlay can trigger a domain-based tryAction at a given point
    suspend fun dAutoclick(x: Int, y: Int){
        try {
            // build a domain Click action (hardcoded test values)
            val scenarioId = Identifier(databaseId = 1L)
            val click = Action.Click(
                id = Identifier(databaseId = 1L),
                scenarioId = scenarioId,
                name = "MVP Click",
                priority = 0,
                repeatCount = 3,
                isRepeatInfinite = false,
                repeatDelayMs = 0L,
                // use coordinates supplied by the overlay
                position = Point(x, y),
                pressDurationMs = 50L,
            )

            // AndroidExecutor adapter that delegates to this service's gesture dispatcher
            val androidExecutor = object : AndroidExecutor {
                override suspend fun executeGesture(gestureDescription: GestureDescription) {
                    // delegate to existing GestureExecutor
                    gestureExecutor.dispatchGesture(this@AutoClickAccessibilityService, gestureDescription)
                }

                override fun executeGlobalAction(globalAction: Int) {
                    this@AutoClickAccessibilityService.performGlobalAction(globalAction)
                }
            }

            // create an ActionExecutor using the adapter
            val actionExecutor = ActionExecutor(androidExecutor)

            // minimal repository stub - explicitly typed flows to satisfy interface
            val repositoryStub = object : IScenarioRepository {
                override val scenarios = flowOf<List<Scenario>>(emptyList())
                override suspend fun getScenario(dbId: Long) = null
                override fun getScenarioFlow(dbId: Long) = flowOf<Scenario?>(null)
                override fun getAllScenarioFlowExcept(dbId: Long) = flowOf<List<Action>>(emptyList())
                override suspend fun addScenario(scenario: Scenario) {}
                override suspend fun addScenarioCopy(scenario: ScenarioWithActions) : Long? = null
                override suspend fun addScenarioCopy(scenarioId: Long, copyName: String): Long? = null
                override suspend fun updateScenario(scenario: Scenario) {}
                override suspend fun deleteScenario(scenario: Scenario) {}
                override suspend fun markAsUsed(scenarioId: Identifier) {}
            }

            // construct engine with the action executor and stub repository
            val engine = Engine(actionExecutor, repositoryStub)

            // Manually build a Scenario equivalent to Action.toScenarioTry() to avoid relying on internal extension
            val finiteAction = click.copy(scenarioId = scenarioId, isRepeatInfinite = false)
            val scenario = Scenario(
                id = scenarioId,
                name = "Try",
                actions = listOf(finiteAction),
                repeatCount = 1,
                maxDurationMin = 1,
                isDurationInfinite = false,
                randomize = false,
                stats = null,
                isRepeatInfinite = false,
            )

            // Initialize engine processing scope with scenario
            engine.init(scenario)

            // finally invoke tryAction
            engine.tryAction(click) {
                Log.i(TAG, "MVP click try completed")
            }
        } catch (t: Throwable) {
            Log.w(TAG, "MVP click wiring failed", t)
        }
    }

    // Toggle domain-based auto-clicking at a given coordinate using the Engine
    // Non-suspending helper so overlay can call it directly.
    fun toggleAutoClickAt(x: Int, y: Int, intervalMs: Long) {
        try {
            // if an engine is already running, stop it
            engineInstance?.let { eng ->
                if (eng.isRunning.value) {
                    eng.stopScenario()
                    eng.release()
                    engineInstance = null
                    Log.i(TAG, "Auto click stopped")
                    return
                }
            }

            // Build AndroidExecutor adapter
            val androidExecutor = object : AndroidExecutor {
                override suspend fun executeGesture(gestureDescription: GestureDescription) {
                    gestureExecutor.dispatchGesture(this@AutoClickAccessibilityService, gestureDescription)
                }

                override fun executeGlobalAction(globalAction: Int) {
                    this@AutoClickAccessibilityService.performGlobalAction(globalAction)
                }
            }

            val actionExecutor = ActionExecutor(androidExecutor)

            // minimal repository stub
            val repositoryStub = object : IScenarioRepository {
                override val scenarios = flowOf<List<Scenario>>(emptyList())
                override suspend fun getScenario(dbId: Long) = null
                override fun getScenarioFlow(dbId: Long) = flowOf<Scenario?>(null)
                override fun getAllScenarioFlowExcept(dbId: Long) = flowOf<List<Action>>(emptyList())
                override suspend fun addScenario(scenario: Scenario) {}
                override suspend fun addScenarioCopy(scenario: ScenarioWithActions) : Long? = null
                override suspend fun addScenarioCopy(scenarioId: Long, copyName: String): Long? = null
                override suspend fun updateScenario(scenario: Scenario) {}
                override suspend fun deleteScenario(scenario: Scenario) {}
                override suspend fun markAsUsed(scenarioId: Identifier) {}
            }

            val eng = Engine(actionExecutor, repositoryStub)

            // create a click Action using provided coordinates and make it repeat infinitely with interval
            val scenarioId = Identifier(databaseId = 1L)
            val click = Action.Click(
                id = Identifier(databaseId = 1L),
                scenarioId = scenarioId,
                name = "OverlayClick",
                priority = 0,
                repeatCount = 0,
                isRepeatInfinite = true,
                repeatDelayMs = intervalMs,
                position = Point(x, y),
                pressDurationMs = 50L
            )

            val scenario = Scenario(
                id = scenarioId,
                name = "OverlayAutoScenario",
                actions = listOf(click),
                repeatCount = 1,
                maxDurationMin = 60,
                isDurationInfinite = true,
                randomize = false,
                stats = null,
                isRepeatInfinite = true,
            )

            eng.init(scenario)
            eng.startScenario()
            engineInstance = eng
            Log.i(TAG, "Auto click started at x=$x y=$y intervalMs=$intervalMs")
        } catch (t: Throwable) {
            Log.w(TAG, "toggleAutoClickAt failed", t)
        }
    }

    // Keep a reference to a running Engine so we can stop/release it
    private var engineInstance: Engine? = null
}