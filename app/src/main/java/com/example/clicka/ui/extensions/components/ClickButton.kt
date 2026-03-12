package com.example.clicka.ui.extensions.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clicka.ui.theme.BorderColor
import com.example.clicka.ui.theme.ClickaTheme
import com.example.clicka.ui.theme.PrimaryText
import com.example.clicka.ui.theme.TranslucentBackground
import kotlin.math.roundToInt


@Composable
fun ClickButton(
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
                    TranslucentBackground,
                    RoundedCornerShape(20.dp)
                ),
            containerColor = TranslucentBackground,
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
        ) {
            Text(
                ButtonNumber.toString(), color = PrimaryText,
                fontWeight = FontWeight.Bold, fontSize = 18.sp
            )
        }
    }
}