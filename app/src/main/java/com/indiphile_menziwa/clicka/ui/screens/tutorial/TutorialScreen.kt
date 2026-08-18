package com.indiphile_menziwa.clicka.ui.screens.tutorial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.indiphile_menziwa.clicka.R
import com.indiphile_menziwa.clicka.ui.extensions.TopBar
import com.indiphile_menziwa.clicka.ui.extensions.TutorialCards


class TutorialScreen : Screen {
    @Composable
    override fun Content() {
        val scrollState = rememberScrollState()
        val size = 16.dp
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)) {

            TopBar("Tutorial")
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(vertical = 16.dp)
            ) {


                TutorialCards("1", "Enter Accessibility", R.drawable.step1)
                Spacer(Modifier.height(size))
                TutorialCards("2", "Select \"Clicka\"", R.drawable.step2)
                Spacer(Modifier.height(size))
                TutorialCards("3", "Turn on \"Clicka\" toggle", R.drawable.step3)
                Spacer(Modifier.height(size))
                TutorialCards("4", "Allow the permissions", R.drawable.step4)

                Spacer(Modifier.height(90.dp))

            }
        }
    }
}