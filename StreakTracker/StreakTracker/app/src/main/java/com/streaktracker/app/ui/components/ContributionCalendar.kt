package com.streaktracker.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.streaktracker.app.ui.theme.EmptyCellDark
import com.streaktracker.app.ui.theme.EmptyCellLight
import com.streaktracker.app.ui.theme.MissedDark
import com.streaktracker.app.ui.theme.MissedLight
import com.streaktracker.app.ui.theme.colorForStreakDay
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * GitHub-style contribution grid: weeks as columns, Sun-Sat as rows, showing roughly
 * the last [monthsToShow] months up to and including today.
 */
@Composable
fun ContributionCalendar(
    completedStreakByDate: Map<LocalDate, Int>,
    missedDates: Set<LocalDate>,
    today: LocalDate,
    isDarkTheme: Boolean,
    monthsToShow: Int = 5,
    modifier: Modifier = Modifier
) {
    val startDate = remember(today, monthsToShow) {
        val roughStart = today.minusMonths(monthsToShow.toLong())
        val dow = roughStart.dayOfWeek.value % 7
        roughStart.minusDays(dow.toLong())
    }
    val totalDays = ChronoUnit.DAYS.between(startDate, today).toInt() + 1
    val totalWeeks = (totalDays / 7) + 1

    val emptyColor = if (isDarkTheme) EmptyCellDark else EmptyCellLight
    val missedColor = if (isDarkTheme) MissedDark else MissedLight

    val scrollState = rememberScrollState()
    LaunchedEffect(totalWeeks) { scrollState.scrollTo(scrollState.maxValue) }

    Row(
        modifier = modifier.horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (week in 0 until totalWeeks) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                for (dow in 0..6) {
                    val date = startDate.plusDays((week * 7 + dow).toLong())
                    val cellColor: Color = when {
                        date.isAfter(today) -> emptyColor
                        completedStreakByDate.containsKey(date) -> colorForStreakDay(completedStreakByDate.getValue(date))
                        missedDates.contains(date) -> missedColor
                        else -> emptyColor
                    }
                    val isToday = date == today
                    CalendarCell(color = cellColor, isToday = isToday)
                }
            }
        }
    }
}

@Composable
private fun CalendarCell(color: Color, isToday: Boolean) {
    var animate by remember { mutableStateOf(false) }
    LaunchedEffect(color) { animate = true }
    val scale by animateFloatAsState(
        targetValue = if (animate) 1f else 0.6f,
        animationSpec = tween(220),
        label = "cellScale"
    )
    Column(
        modifier = Modifier
            .size(14.dp)
            .scale(scale)
            .clip(RoundedCornerShape(3.dp))
            .background(color)
            .then(
                if (isToday) Modifier.border(1.5.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(3.dp))
                else Modifier
            )
    ) {}
}
