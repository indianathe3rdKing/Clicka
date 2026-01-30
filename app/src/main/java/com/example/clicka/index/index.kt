package com.example.clicka.index

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.painter.ColorPainter
import com.example.clicka.R
import com.example.clicka.services.overlayservice.OverlayService

data class ButtonInfo(
    val title: String,
    val description: String,
    val icon: Int
)


data class FABInfo(val icon: Int,val onClick: () -> Unit)


object ButtonInfoProvider {
    // Sample/fake data that compiles without Android drawable resources
    val samples: List<ButtonInfo> = listOf(
        ButtonInfo(
            title = "Single",
            description = "Save the current document",
            icon = R.drawable.single_point
        ),
        ButtonInfo(
            title = "Multiple",
            description = "Delete the selected item",
            icon = R.drawable.click_point
        ),
        ButtonInfo(
            title = "Swipe",
            description = "Share with others",
            icon = R.drawable.swipe
        )
    )

    fun fabItems(onClose: () -> Unit):  List<FABInfo> = listOf(
        FABInfo(R.drawable.play,{}),
        FABInfo(R.drawable.add_diamond, {}),
        FABInfo(R.drawable.remove_icon, {}),
        FABInfo(R.drawable.close, {onClose() })
    )

}



