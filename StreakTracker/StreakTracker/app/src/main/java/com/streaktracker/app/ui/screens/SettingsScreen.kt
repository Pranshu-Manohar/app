package com.streaktracker.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.streaktracker.app.data.AppThemeMode
import com.streaktracker.app.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = viewModel()
    val themeMode by viewModel.themeMode.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)

        Card(shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(1.dp)) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Theme", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ThemeChip("System", themeMode == AppThemeMode.SYSTEM) { viewModel.setThemeMode(AppThemeMode.SYSTEM) }
                    ThemeChip("Light", themeMode == AppThemeMode.LIGHT) { viewModel.setThemeMode(AppThemeMode.LIGHT) }
                    ThemeChip("Dark", themeMode == AppThemeMode.DARK) { viewModel.setThemeMode(AppThemeMode.DARK) }
                }
            }
        }

        Card(shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(1.dp)) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("About", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Streak helps you build a daily habit over 21 consecutive days. " +
                        "Press Extend Streak once a day — missed days are remembered, not hidden.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Card(shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(1.dp)) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Danger Zone", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(10.dp))
                OutlinedButton(onClick = { showResetDialog = true }) {
                    Text("Reset streak & data")
                }
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset all data?") },
            text = { Text("This permanently deletes your entire history, current streak, and best streak. This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showResetDialog = false
                    viewModel.resetAllData { }
                }) { Text("Reset") }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun ThemeChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(selected = selected, onClick = onClick, label = { Text(label) })
}
