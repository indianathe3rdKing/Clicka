package com.example.clicka.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onFirstVisible
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.clicka.ui.screens.select.SelectTab
import com.example.clicka.ui.screens.settings.SettingsTab
import com.example.clicka.ui.theme.ClickaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClickaTheme {
                TabNavigator(SelectTab) {
                    Scaffold(
                        content = { innerPadding ->
                            Box(modifier = Modifier.padding(innerPadding)) {
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
                                    containerColor = MaterialTheme.colorScheme.inverseOnSurface.copy(0.2f),
                                    tonalElevation = 2.dp
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
                else MaterialTheme.colorScheme.onSurface
            )
        },
        label = {
            Text(
                tab.options.title,
                color = if (isSelected) MaterialTheme.colorScheme.onSurface.copy(0.68f)
                else MaterialTheme.colorScheme.onSurface
            )
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.onSurface.copy(0.68f)
        )
    )
}
