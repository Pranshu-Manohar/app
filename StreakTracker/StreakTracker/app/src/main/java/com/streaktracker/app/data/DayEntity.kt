package com.streaktracker.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One row per COMPLETED day only. A date with no row is either a missed day
 * (if it's in the past) or an untouched future/today day.
 * epochDay = LocalDate.toEpochDay(), used as a stable, timezone-safe primary key.
 */
@Entity(tableName = "days")
data class DayEntity(
    @PrimaryKey val epochDay: Long,
    val streakAtCompletion: Int,
    val quote: String,
    val lesson: String,
    val completedAtEpochMillis: Long
)
