package com.indiphile_menziwa.clicka.index

import androidx.compose.ui.res.stringResource
import com.indiphile_menziwa.clicka.R
import com.indiphile_menziwa.clicka.domain.model.ClickMode

data class ButtonInfo(
    val title: Int,
    val description: Int,
    val icon: Int,
    val mode: ClickMode
)


data class FABInfo(
    val icon: Int,
    val onClick: () -> Unit
)


object ButtonInfoProvider {
    // Sample/fake data that compiles without Android drawable resources
    val samples: List<ButtonInfo> = listOf(
        ButtonInfo(
            title = R.string.single_button_title,
            description = R.string.single_button_description,
            icon = R.drawable.single_point,
            mode = ClickMode.SINGLE
        ),
        ButtonInfo(
            title = R.string.multiple_button_title,
            description = R.string.multiple_button_description,
            icon = R.drawable.click_point,
            mode = ClickMode.MULTIPLE
        ),
        ButtonInfo(
            title = R.string.swipe_button_title,
            description = R.string.swipe_button_description,
            icon = R.drawable.swipe,
            mode = ClickMode.SWIPE
        )
    )

    fun fabItems(
        onClose: () -> Unit,
        onAdd: () -> Unit,
        onPlay: () -> Unit,
        onRemove: () -> Unit,
        onSettings: () -> Unit = {}
    ): List<FABInfo> = listOf(
        FABInfo(R.drawable.play, onPlay),
        FABInfo(R.drawable.add, onAdd),
        FABInfo(R.drawable.remove_icon, onRemove),
        FABInfo(R.drawable.settings_outline, onSettings),
        FABInfo(R.drawable.close, { onClose() })
    )

}
