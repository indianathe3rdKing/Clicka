package com.indiphile_menziwa.clicka.ui.screens.select

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.indiphile_menziwa.clicka.R
import com.indiphile_menziwa.clicka.ui.extensions.ButtonComponent
import com.indiphile_menziwa.clicka.ui.extensions.TopBar
import com.indiphile_menziwa.clicka.index.ButtonInfoProvider

object SelectTab : Tab {
    @Suppress("unused")
    private fun readResolve(): Any = SelectTab

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.select)
            val icon = painterResource(R.drawable.gesture_select)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp, 30.dp)
        ) {
            TopBar("Clicka")
            ButtonComponent(ButtonInfoProvider.samples)

        }
    }


}