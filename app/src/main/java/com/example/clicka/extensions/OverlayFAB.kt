package com.example.clicka.extensions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.clicka.R
import com.example.clicka.extensions.components.fabComponent
import com.example.clicka.index.ButtonInfoProvider
import kotlin.math.roundToInt
import com.example.clicka.services.overlayservice.OverlayService

@Composable
internal fun FloatingButton(
    onMoveBy:(dragX: Int,dragY: Int)-> Unit,
    onClose:()-> Unit

){
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .padding(16.dp)
            .wrapContentHeight()
            .wrapContentWidth()
            .background(Color(98, 97, 97, 52), MaterialTheme.shapes.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom

    ) {
        AnimatedVisibility(
            visible = expanded ,
            enter= fadeIn()+ slideInVertically(initialOffsetY = {it})+ expandVertically(),
            exit = fadeOut()+ slideOutVertically(targetOffsetY = {it})+ shrinkVertically()
        ) {
            fabComponent(ButtonInfoProvider.fabItems(onClose))

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
                }.size(50.dp)
            , containerColor =Color.Transparent,
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
        ) {
            Icon(painter= painterResource(R.drawable.home),null,
                tint = Color.White)
        }
    }
}