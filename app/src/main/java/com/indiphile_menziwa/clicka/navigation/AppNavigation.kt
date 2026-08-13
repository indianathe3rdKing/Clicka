package com.indiphile_menziwa.clicka.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.indiphile_menziwa.clicka.data.datastore.DataStoreManager
import com.indiphile_menziwa.clicka.ui.extensions.components.AlertModalConsent
import com.indiphile_menziwa.clicka.ui.screens.select.SelectTab
import com.indiphile_menziwa.clicka.ui.screens.settings.SettingsTab
import com.indiphile_menziwa.clicka.ui.theme.ClickaTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MainActivity : ComponentActivity() {
    private lateinit var dataStoreManager: DataStoreManager
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        dataStoreManager = DataStoreManager(this)
        setContent {
            ClickaTheme {
                val scope = rememberCoroutineScope()

                val isAgreementAccepted = remember(dataStoreManager) {
                    dataStoreManager.agreementAccepted.stateIn(
                        scope = scope,
                        started = SharingStarted.Eagerly,
                        initialValue = dataStoreManager.getAgreementAcceptedSync()
                    )
                }

                val agreementAccepted by isAgreementAccepted.collectAsState()
                TabNavigator(SelectTab) {
                    Scaffold(
                        content = { innerPadding ->
                            Box(
                                modifier = Modifier
                                    .padding(
                                        start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                                        top =
                                            innerPadding.calculateTopPadding(),
                                        end = innerPadding.calculateEndPadding(
                                            LayoutDirection.Ltr
                                        ),
                                        bottom = 0.dp
                                    )
                                    .fillMaxSize()
                            ) {
                                if (!agreementAccepted) {
                                    AlertModalConsent(
                                        onDismiss = { finish() },
                                        onAccept = { lifecycleScope.launchWhenResumed { dataStoreManager.setAgreementAccepted(true) } },

                                        )
                                }
                                CurrentTab()
                            }
                        },

                        bottomBar = {
                            Surface(
                                shape = RoundedCornerShape(24.dp),

                                ) {
                                NavigationBar(
                                    modifier = Modifier
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.onSurface.copy(0.68f),
                                            RoundedCornerShape(24.dp)
                                        ),
                                    containerColor = MaterialTheme.colorScheme.inverseOnSurface.copy(
                                        0.2f
                                    ),
                                    tonalElevation = 8.dp
                                ) {
                                    TabNavigationItem(SelectTab)
                                    TabNavigationItem(SettingsTab)
                                }
                            }
                        }
                    )
                }
            }

        }
    }


}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val isSelected = tabNavigator.current == tab

    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = {
            Icon(
                painter = tab.options.icon!!, contentDescription = tab.options.title,
                tint = if (isSelected) MaterialTheme.colorScheme.inverseOnSurface
                else MaterialTheme.colorScheme.onSurface.copy(0.68f)
            )
        },
        label = {
            Text(
                tab.options.title,
                color = if (isSelected) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurface.copy(0.68f)
            )
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.onSurface
        )
    )
}
