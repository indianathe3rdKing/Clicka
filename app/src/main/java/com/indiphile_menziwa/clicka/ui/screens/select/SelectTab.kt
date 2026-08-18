package com.indiphile_menziwa.clicka.ui.screens.select

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.indiphile_menziwa.clicka.R
import com.indiphile_menziwa.clicka.ui.extensions.ButtonComponent
import com.indiphile_menziwa.clicka.ui.extensions.TopBar
import com.indiphile_menziwa.clicka.index.ButtonInfoProvider
import com.indiphile_menziwa.clicka.ui.DataStoreViewModel
import com.indiphile_menziwa.clicka.ui.extensions.components.AlertModalConsent
import com.indiphile_menziwa.clicka.ui.screens.select.selectContent.SelectTabContent
import com.indiphile_menziwa.clicka.ui.screens.tutorial.TutorialScreen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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
        Navigator(screen = SelectTabContent)
    }


}