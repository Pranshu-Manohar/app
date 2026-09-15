package com.streaktracker.app.util

import java.time.LocalDate

/**
 * Pure, stateless streak math. Given the full set of completed dates plus "today",
 * everything else (current streak, best streak, missed days) is derived.
 * This avoids ever having stored counters drift out of sync with the real history.
 */
object StreakCalculator {

    /**
     * Current streak = consecutive completed days ending at "today" (if today is done)
     * or ending at "yesterday" (if today is not yet done — the streak isn't broken until
     * a day fully passes without completion).
     */
    fun currentStreak(completed: Set<LocalDate>, today: LocalDate): Int {
        val anchor = if (completed.contains(today)) today else today.minusDays(1)
        if (!completed.contains(anchor)) return 0
        var count = 0
        var d = anchor
        while (completed.contains(d)) {
            count++
            d = d.minusDays(1)
        }
        return count
    }

    /** Longest run of consecutive completed dates in the whole history. */
    fun bestStreakFromHistory(completed: Set<LocalDate>): Int {
        if (completed.isEmpty()) return 0
        val sorted = completed.sorted()
        var longest = 1
        var run = 1
        for (i in 1 until sorted.size) {
            run = if (sorted[i] == sorted[i - 1].plusDays(1)) run + 1 else 1
            if (run > longest) longest = run
        }
        return longest
    }

    /**
     * All dates from [firstLaunchDate, yesterday] that were NOT completed.
     * Today and future dates are never "missed" — they simply haven't happened yet.
     */
    fun missedDates(completed: Set<LocalDate>, firstLaunchDate: LocalDate, today: LocalDate): List<LocalDate> {
        val lastPastDate = today.minusDays(1)
        if (lastPastDate.isBefore(firstLaunchDate)) return emptyList()
        val result = mutableListOf<LocalDate>()
        var d = firstLaunchDate
        while (!d.isAfter(lastPastDate)) {
            if (!completed.contains(d)) result.add(d)
            d = d.plusDays(1)
        }
        return result
    }

    /** Human-readable stage label for the 21-day goal. */
    fun milestoneLabel(streak: Int): String = when {
        streak <= 0 -> "Not started"
        streak == 1 -> "Starting"
        streak in 2..2 -> "Starting"
        streak in 3..4 -> "Building momentum"
        streak == 5 -> "First milestone"
        streak in 6..6 -> "First milestone"
        streak in 7..9 -> "One week"
        streak in 10..13 -> "Double digits"
        streak in 14..20 -> "Two weeks"
        streak >= 21 -> "Goal completed"
        else -> ""
    }
}
