package com.streaktracker.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.streaktracker.app.ui.components.ContributionCalendar
import com.streaktracker.app.ui.components.ExtendStreakButton
import com.streaktracker.app.ui.components.GoalProgressRing
import com.streaktracker.app.util.StreakCalculator
import com.streaktracker.app.viewmodel.HomeViewModel
import java.time.LocalDate

@Composable
fun HomeScreen(isDarkTheme: Boolean) {
    val viewModel: HomeViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()
    val showCelebration by viewModel.showCelebration.collectAsState()
    val today = LocalDate.now()

    val greeting = when {
        state.todayCompleted -> "Nice work today."
        state.yesterdayCompleted -> "Your streak is waiting for you."
        state.yesterdayMissed -> "Yesterday was a miss. Today is a new start."
        else -> "Keep your streak alive."
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Column {
                Text("🔥 ${state.currentStreak} Day Streak", style = MaterialTheme.typography.headlineMedium)
                Text("Best: ${state.bestStreak} days", style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(4.dp))
                Text(greeting, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            StatChipsRow(state.totalCompleted, state.totalMissed)

            Card(shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(2.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("21-Day Goal", style = MaterialTheme.typography.titleMedium)
                        Text(
                            StreakCalculator.milestoneLabel(state.currentStreak),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Day ${state.currentStreak.coerceAtMost(21)} / 21",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    GoalProgressRing(current = state.currentStreak)
                }
            }

            ExtendStreakButton(
                alreadyCompletedToday = state.todayCompleted,
                onClick = { viewModel.extendStreak() }
            )

            Card(shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(1.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Activity", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    val streakMap = remember(state.completedDates) {
                        // Rebuild streak-at-date map for coloring, cheap for a few hundred days.
                        val sorted = state.completedDates.sorted()
                        val map = HashMap<LocalDate, Int>()
                        var run = 0
                        var prev: LocalDate? = null
                        for (d in sorted) {
                            run = if (prev != null && d == prev.plusDays(1)) run + 1 else 1
                            map[d] = run
                            prev = d
                        }
                        map
                    }
                    ContributionCalendar(
                        completedStreakByDate = streakMap,
                        missedDates = state.missedDates,
                        today = today,
                        isDarkTheme = isDarkTheme
                    )
                    Spacer(Modifier.height(10.dp))
                    Legend()
                }
            }

            if (state.todayQuote != null) {
                InfoCard(title = "Today's Quote", body = "“${state.todayQuote}”", accent = MaterialTheme.colorScheme.primary)
            }

            InfoCard(title = "Lesson of the Day", body = state.todayLesson, accent = MaterialTheme.colorScheme.secondary)

            Spacer(Modifier.height(8.dp))
        }

        AnimatedVisibility(
            visible = showCelebration,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300)),
            modifier = Modifier.align(Alignment.Center)
        ) {
            CelebrationCard(onDismiss = { viewModel.dismissCelebration() })
        }
    }
}

@Composable
private fun StatChipsRow(completed: Int, missed: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        StatChip(label = "Completed", value = completed.toString(), modifier = Modifier.weight(1f))
        StatChip(label = "Missed", value = missed.toString(), modifier = Modifier.weight(1f))
    }
}

@Composable
private fun StatChip(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(1.dp)) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(value, style = MaterialTheme.typography.titleLarge)
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun InfoCard(title: String, body: String, accent: Color) {
    Card(shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(1.dp)) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = accent)
            Spacer(Modifier.height(6.dp))
            Text(body, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun Legend() {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("Less", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        LegendDot(com.streaktracker.app.ui.theme.BlueProgression[1])
        LegendDot(com.streaktracker.app.ui.theme.GreenProgression[2])
        LegendDot(com.streaktracker.app.ui.theme.GreenProgression[8])
        LegendDot(com.streaktracker.app.ui.theme.GreenProgression[15])
        Text("More", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun LegendDot(color: Color) {
    Box(
        modifier = Modifier
            .width(12.dp)
            .height(12.dp)
            .background(color, RoundedCornerShape(3.dp))
    )
}

@Composable
private fun CelebrationCard(onDismiss: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier.padding(32.dp)
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("🎉", fontSize = 40.sp)
            Text("21-Day Goal Complete", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                "You built the habit. Keep going if you'd like — every extra day still counts.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            androidx.compose.material3.TextButton(onClick = onDismiss) { Text("Continue") }
        }
    }
}
