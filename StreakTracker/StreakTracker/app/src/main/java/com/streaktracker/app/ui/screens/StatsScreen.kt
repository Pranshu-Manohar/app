package com.streaktracker.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.streaktracker.app.viewmodel.StatsViewModel
import kotlin.math.roundToInt

@Composable
fun StatsScreen() {
    val viewModel: StatsViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()
    val totalTracked = state.totalCompleted + state.totalMissed
    val completionPct = if (totalTracked == 0) 0 else ((state.totalCompleted.toDouble() / totalTracked) * 100).roundToInt()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Statistics", style = MaterialTheme.typography.headlineMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            StatCard("Current streak", "${state.currentStreak}", Modifier.weight(1f))
            StatCard("Best streak", "${state.bestStreak}", Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            StatCard("Total completed", "${state.totalCompleted}", Modifier.weight(1f))
            StatCard("Total missed", "${state.totalMissed}", Modifier.weight(1f))
        }
        StatCard("Completion rate", "$completionPct%", Modifier.fillMaxWidth())

        Card(shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(1.dp)) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("21-Day Achievements", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(10.dp))
                val completedCycles = state.bestStreak / 21
                if (completedCycles <= 0) {
                    Text(
                        "You haven't reached the 21-day goal yet — keep going!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        "You've reached the 21-day milestone $completedCycles time${if (completedCycles > 1) "s" else ""} (based on your best streak).",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(18.dp), elevation = CardDefaults.cardElevation(1.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(value, style = MaterialTheme.typography.headlineMedium)
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
