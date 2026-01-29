package com.example.clicka.screens.setings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.clicka.extensions.ButtonComponent
import com.example.clicka.extensions.OverviewHeader
import com.example.clicka.index.ButtonInfoProvider

object SettingsTab: Tab {
    override val options: TabOptions

        @Composable
        get(){
            val title = "Settings"
            val icon = rememberVectorPainter(Icons.Default.Settings)

            return remember{
                TabOptions(
                    index = 0u,
                    title=title,
                    icon=icon
                )
            }
        }

    @Composable
    override fun Content() {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "Settings Tab", modifier = Modifier.align(Alignment.Center))
            OverviewHeader("Settings")



        }
    }

}