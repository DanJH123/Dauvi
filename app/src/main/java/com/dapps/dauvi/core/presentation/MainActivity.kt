package com.dapps.dauvi.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dapps.dauvi.feature_uvi.presentation.uvi.UviScreen
import com.dapps.dauvi.core.presentation.screens.Screens
import com.dapps.dauvi.core.presentation.screens.TopLevelRoute
import com.dapps.dauvi.feature_settings.presentation.SettingsScreen
import com.dapps.dauvi.ui.theme.DauviTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val topLevelRoutes = listOf(
            TopLevelRoute("UVI", Screens.UviScreen, Icons.Rounded.LightMode),
            TopLevelRoute("Settings", Screens.SettingsScreen, Icons.Rounded.Settings),
        )

        setContent {
            DauviTheme {
                var navSelectedIndex by rememberSaveable { mutableIntStateOf(0) }
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        NavigationBar (containerColor = Color.Transparent){
                            topLevelRoutes.forEachIndexed { index, topLevelRoute ->
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            imageVector = topLevelRoute.icon,
                                            contentDescription = topLevelRoute.name
                                        )
                                    },
                                    colors = NavigationBarItemColors(
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        selectedIndicatorColor = Color.Transparent,
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        disabledIconColor = MaterialTheme.colorScheme.surfaceDim,
                                        disabledTextColor = MaterialTheme.colorScheme.surfaceDim,
                                    ),
                                    selected = navSelectedIndex == index,
                                    onClick = {
                                        navSelectedIndex = index
                                        navController.navigate(topLevelRoute.screen) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screens.UviScreen,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Screens.UviScreen> { UviScreen(navController) }
                        composable<Screens.SettingsScreen> { SettingsScreen(navController) }
                    }
                }
            }
        }
    }
}