package com.streaktracker.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.streaktracker.app.ui.screens.HistoryScreen
import com.streaktracker.app.ui.screens.HomeScreen
import com.streaktracker.app.ui.screens.SettingsScreen
import com.streaktracker.app.ui.screens.StatsScreen

private sealed class Destination(val route: String, val label: String) {
    object Home : Destination("home", "Home")
    object History : Destination("history", "History")
    object Stats : Destination("stats", "Stats")
    object Settings : Destination("settings", "Settings")
}

private val bottomDestinations = listOf(Destination.Home, Destination.History, Destination.Stats, Destination.Settings)

@Composable
fun AppNavHost(isDarkTheme: Boolean) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry?.destination
                bottomDestinations.forEach { dest ->
                    val selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(dest.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = when (dest) {
                                    Destination.Home -> Icons.Filled.Home
                                    Destination.History -> Icons.Filled.History
                                    Destination.Stats -> Icons.Filled.BarChart
                                    Destination.Settings -> Icons.Filled.Settings
                                },
                                contentDescription = dest.label
                            )
                        },
                        label = { Text(dest.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Destination.Home.route) { HomeScreen(isDarkTheme = isDarkTheme) }
            composable(Destination.History.route) { HistoryScreen() }
            composable(Destination.Stats.route) { StatsScreen() }
            composable(Destination.Settings.route) { SettingsScreen() }
        }
    }
}
