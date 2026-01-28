package com.example.clicka.screens.select

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object SelectTab: Tab{
    override val options: TabOptions
        @Composable
        get(){
            val title = "Select"
            val icon = rememberVectorPainter(Icons.Default.Create)

            return remember {
                TabOptions(
                    index=0u,
                    title=title,
                    icon=icon
                )
            }
        }

    @Composable
    override fun Content() {
        TODO("Not yet implemented")
        Box(modifier= Modifier.fillMaxSize()) {
            Text(text = "Select Tab", modifier = Modifier.align(Alignment.Center))
        }
    }


}