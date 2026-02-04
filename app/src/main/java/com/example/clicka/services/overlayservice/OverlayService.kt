package com.example.clicka.services.overlayservice

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.clicka.extensions.FloatingButton
import com.example.clicka.extensions.SettingsModal
import com.example.clicka.services.accessibilty.AutoClickAccessibilityService
import com.example.clicka.services.overlaylifecycleowner.OverlayLifecyeOwner
import com.example.clicka.ui.theme.ClickaTheme
import kotlin.math.roundToInt
import com.example.clicka.ui.theme.BorderColor

private val TAG = "Overlay"
class OverlayService : Service() {
    private val windowManager
        get() = getSystemService(WINDOW_SERVICE) as WindowManager
    private val overlayViews = mutableMapOf<ComposeView, OverlayLifecyeOwner>()
    private var buttonNumber = 0

    private var modalView: ComposeView? = null

    private var overlayGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null



    override fun onCreate() {
        super.onCreate()
        showOverlay()
    }

    private fun showOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            stopSelf()
            return
        }

        val layoutFlag: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSPARENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 200
        }

        val composeView = ComposeView(this).apply { isClickable = true }
        val lifecycleOwner = OverlayLifecyeOwner()

        composeView.setContent {
            ClickaTheme {
                FloatingButton(
                    onMoveBy = { dragX, dragY ->
                        params.y += dragY
                        params.x += dragX
                        windowManager.updateViewLayout(composeView, params)
                    },
                    onClose = { stopSelf() },
                    onAdd = { addButton() }
                )
            }
        }

        setupLifecycle(composeView, lifecycleOwner)

        windowManager.addView(composeView, params)
        overlayViews[composeView] = lifecycleOwner
    }

    override fun onDestroy() {
        overlayViews.forEach { (view, owner) ->
            owner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            owner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            try {
                windowManager.removeView(view)
            } catch (_: Throwable) {
            }
        }
        overlayViews.clear()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @Composable
    private fun ClickButton(
        onMoveBy: (dragX: Int, dragY: Int) -> Unit,
        onRemove: () -> Unit, ButtonNumber: Int
    ) {

        ClickaTheme {
            FloatingActionButton(
                onClick = { onRemove() },

                modifier = Modifier
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            onMoveBy(
                                dragAmount.x.roundToInt(),
                                dragAmount.y.roundToInt()
                            )
                        }
                    }
                    .size(40.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .border(
                        2.dp,
                        BorderColor,
                        RoundedCornerShape(20.dp)
                    ),
                containerColor = Color(98, 97, 97, 52),
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Text(ButtonNumber.toString(), color = MaterialTheme.colorScheme.onSurface ,
                    fontWeight= FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }

    private fun addButton() {
        buttonNumber++
        val layoutFlag: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSPARENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 100
            y = 300
        }

        val composeView = ComposeView(this).apply { isClickable = true }
        val lifecycleOwner = OverlayLifecyeOwner()

        val metrics = resources.displayMetrics
        val fabSize = (56* metrics.density).roundToInt()
        var overlayWidth = 0
        var overlayHeight =0
        overlayGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            overlayWidth = composeView.width
            overlayHeight = composeView.height
        }
        composeView.setContent {
            ClickButton(
                onMoveBy = { dragX, dragY ->
                    params.y += dragY
                    params.x += dragX
                    windowManager.updateViewLayout(composeView, params)
                },
                onRemove = {
                    val currentOverlayWidth = if(overlayWidth>0) overlayWidth else fabSize
                    val currentOverlayHeight = if(overlayHeight>0) overlayHeight else fabSize

                    val clickX = params.x + (currentOverlayWidth/2)
                    val clickY = params.y + (currentOverlayHeight/2)
                    Log.i(TAG, "clickX=$clickX clickY=$clickY")
                    toggleAutoClick(clickX,clickY,1000) }, buttonNumber
            )
        }

        setupLifecycle(composeView, lifecycleOwner)

        windowManager.addView(composeView, params)
        overlayViews[composeView] = lifecycleOwner
    }

    private fun setModal() {


        if(modalView!= null) return

        val layoutFlag: Int = if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O ){
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }else{
            @Suppress("DEPRECAITON")
            WindowManager.LayoutParams.TYPE_PHONE
        }


        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL ,
            PixelFormat.TRANSLUCENT
        ).apply{
            gravity = Gravity.TOP or Gravity.START
            x =100
            y=300
        }
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE


        val composeView = ComposeView(this).apply { isClickable = true }
        val lifecycleOwner = OverlayLifecyeOwner()

        composeView.setContent {
//            TODO :  make the modal design
            ClickaTheme {
                SettingsModal(onClose = { removeButton(composeView, lifecycleOwner) })
            }
        }

        val viewModelStore= ViewModelStore()
        val viewModelStoreOwner= object: ViewModelStoreOwner{
            override val viewModelStore: ViewModelStore
                get() = viewModelStore
        }

        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)

        composeView.setViewTreeLifecycleOwner(lifecycleOwner)
        composeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)
        composeView.setViewTreeViewModelStoreOwner(viewModelStoreOwner)

        modalView = composeView
        windowManager.addView(modalView, params)

    }

    private fun removeButton(composeView: ComposeView, lifecycleOwner: OverlayLifecyeOwner) {
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        try {
            windowManager.removeView(composeView)
            modalView = null
        } catch (_: Throwable) {
        }
        overlayViews.remove(composeView)
    }

    private fun setupLifecycle(composeView: ComposeView, lifecycleOwner: OverlayLifecyeOwner) {
        val viewModelStore = ViewModelStore()
        val viewModelStoreOwner = object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore
                get() = viewModelStore
        }

        if (lifecycleOwner.lifecycle.currentState == Lifecycle.State.INITIALIZED) {
            lifecycleOwner.performRestore(null)
        }

        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)

        composeView.setViewTreeLifecycleOwner(lifecycleOwner)
        composeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)
        composeView.setViewTreeViewModelStoreOwner(viewModelStoreOwner)
    }



    fun toggleAutoClick(x:Int,y: Int,intervalMs: Long) {
        val svc = AutoClickAccessibilityService.instance
        if (svc != null){
            svc.toggleAutoClick(x,y,intervalMs)
        }else{
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
    }
}