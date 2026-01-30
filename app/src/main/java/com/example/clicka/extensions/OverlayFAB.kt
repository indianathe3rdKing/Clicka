package com.example.clicka.extensions

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.clicka.extensions.components.fabComponent
import com.example.clicka.index.ButtonInfoProvider
import kotlin.math.roundToInt

@Composable
internal fun FloatingButton(
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
            fabComponent(ButtonInfoProvider.fabItems)
        }

        FloatingActionButton(
            onClick = {expanded = !expanded},
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
            ,containerColor = Color(255, 152, 0, 255),
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
        ) {
            Icon(imageVector = Icons.Filled.Add,null,
                tint = MaterialTheme.colorScheme.onSurface)
        }
    }
}