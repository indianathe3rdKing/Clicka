package com.example.clicka.services.overlayservice

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.clicka.services.overlaylifecycleowner.OverlayLifecyeOwner
import kotlin.math.roundToInt

class OverlayService : Service() {
    val lifecycleOwner = OverlayLifecyeOwner()

    private val windowManager
        get() = getSystemService(WINDOW_SERVICE) as WindowManager
    private var overlayView: ComposeView? = null

    override fun onCreate() {
        super.onCreate()
        showOverlay()

    }

    private fun showOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            stopSelf()
            return
        }

        if (overlayView != null) return

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
            PixelFormat.TRANSLUCENT

        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 200
        }

        val composeView = ComposeView(this).apply { isClickable=true }

        composeView.setContent {
        FloatingButton(
            onMoveBy = {
                dragX,dragY->
                params.y+=dragY
                params.x+=dragX
                windowManager.updateViewLayout(composeView,params)
            },{stopSelf()}
        )
        }


        val viewModelStore = ViewModelStore()
        val viewModelStoreOwner= object : ViewModelStoreOwner{
            override val viewModelStore: ViewModelStore
                get() = viewModelStore

        }


        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)

        composeView.setViewTreeLifecycleOwner(lifecycleOwner)
        composeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)
        composeView.setViewTreeViewModelStoreOwner(viewModelStoreOwner)

    }

    override fun onDestroy() {
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        overlayView?.let{
            try {
                windowManager.removeView((it))
            }catch (_: Throwable){

            }
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null

    }
}

@Composable
private fun FloatingButton(
    onMoveBy:(dragX: Int,dragY: Int)-> Unit,
    onClose:()-> Unit

){
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentHeight()
            .wrapContentWidth()
    ) {
        AnimatedVisibility(
            visible = expanded ,
            enter= fadeIn()+ slideInVertically(initialOffsetY = {it})+ expandVertically(),
            exit = fadeOut()+ slideOutVertically(targetOffsetY = {it})+ shrinkVertically()
        ) {
            FloatingActionButton(
                onClick = {},
                containerColor = Color(0,150,136,255),
                modifier = Modifier.padding(6.dp)
            ) {
                Icon(Icons.Filled.Build,null)
            }
        }

        FloatingActionButton(
            onClick = {},
            modifier = Modifier
                .pointerInput(Unit){
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        onMoveBy(
                            dragAmount.x.roundToInt(),
                            dragAmount.y.roundToInt()
                        )
                    }
                }
            ,containerColor = Color(0,150,136,255),
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
        ) {
            Icon(imageVector = Icons.Filled.Add,null)
        }
    }
}