package com.streaktracker.app.ui.theme

import androidx.compose.ui.graphics.Color

// Brand
val BluePrimary = Color(0xFF2F6FED)
val GreenPrimary = Color(0xFF2E9E5B)

// Light theme surfaces
val LightBackground = Color(0xFFF7F8FA)
val LightSurface = Color(0xFFFFFFFF)
val LightOnSurface = Color(0xFF1C1E21)
val LightOnSurfaceMuted = Color(0xFF6B7280)

// Dark theme surfaces (not a simple invert — deliberately tuned)
val DarkBackground = Color(0xFF121417)
val DarkSurface = Color(0xFF1C1F24)
val DarkOnSurface = Color(0xFFECEDEE)
val DarkOnSurfaceMuted = Color(0xFF9AA1AB)

// Missed-day marker (subtle, not alarming)
val MissedLight = Color(0xFFE7DEDD)
val MissedDark = Color(0xFF3A2F2E)

// Empty / future cell
val EmptyCellLight = Color(0xFFEBEDF0)
val EmptyCellDark = Color(0xFF23262B)

/** Days 1-5 of a fresh streak: light -> stronger blue. */
val BlueProgression = listOf(
    Color(0xFFD6E4FF), // day 1
    Color(0xFFADC8FF), // day 2
    Color(0xFF7FA6FA), // day 3
    Color(0xFF4C82F0), // day 4
    Color(0xFF2F6FED)  // day 5
)

/** Days 6-21+: progressively stronger green, GitHub-contribution style. */
val GreenProgression = listOf(
    Color(0xFFCDEFD8), // day 6
    Color(0xFFB3E5C2), // 7
    Color(0xFF99DBAC), // 8
    Color(0xFF80D096), // 9
    Color(0xFF66C680), // 10
    Color(0xFF52B96F), // 11
    Color(0xFF45AC63), // 12
    Color(0xFF3D9F58), // 13
    Color(0xFF37934F), // 14
    Color(0xFF328748), // 15
    Color(0xFF2E7C42), // 16
    Color(0xFF2A713C), // 17
    Color(0xFF276637), // 18
    Color(0xFF245C32), // 19
    Color(0xFF20512D), // 20
    Color(0xFF1B4527)  // 21+ (strongest, most significant)
)

/** Color for a completed day based on its streak-count-at-completion (1-based). */
fun colorForStreakDay(streakNumber: Int): Color {
    return when {
        streakNumber <= 0 -> EmptyCellLight
        streakNumber in 1..5 -> BlueProgression[streakNumber - 1]
        else -> {
            val idx = (streakNumber - 6).coerceAtMost(GreenProgression.size - 1)
            GreenProgression[idx]
        }
    }
}
