package com.streaktracker.app.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtils {
    private val displayFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    private val shortFormatter = DateTimeFormatter.ofPattern("MMM d")

    fun display(date: LocalDate): String = date.format(displayFormatter)
    fun short(date: LocalDate): String = date.format(shortFormatter)

    /** Sunday-aligned first day of the week containing [date], GitHub-calendar style. */
    fun startOfWeek(date: LocalDate): LocalDate {
        val dow = date.dayOfWeek.value % 7 // Monday=1..Sunday=7 -> Sunday=0
        return date.minusDays(dow.toLong())
    }
}
