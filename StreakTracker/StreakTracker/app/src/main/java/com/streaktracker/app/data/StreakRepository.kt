package com.streaktracker.app.data

import android.content.Context
import com.streaktracker.app.util.StreakCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate

data class DayRecord(
    val date: LocalDate,
    val completed: Boolean,
    val isMissed: Boolean,
    val isToday: Boolean,
    val isFuture: Boolean,
    val streakAtCompletion: Int,
    val quote: String?,
    val lesson: String
)

data class StreakUiState(
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val totalCompleted: Int = 0,
    val totalMissed: Int = 0,
    val todayCompleted: Boolean = false,
    val yesterdayCompleted: Boolean = false,
    val yesterdayMissed: Boolean = false,
    val todayQuote: String? = null,
    val todayLesson: String = "",
    val completedDates: Set<LocalDate> = emptySet(),
    val missedDates: Set<LocalDate> = emptySet(),
    val firstLaunchDate: LocalDate = LocalDate.now()
)

class StreakRepository(context: Context) {

    private val dao = StreakDatabase.getInstance(context).dayDao()
    val settings = SettingsDataStore(context)

    fun observeUiState(today: () -> LocalDate = { LocalDate.now() }): Flow<StreakUiState> {
        return combine(
            dao.observeAll(),
            settings.bestStreak,
            settings.firstLaunchDate
        ) { days, persistedBest, firstLaunch ->
            val todayDate = today()
            val completedDates = days.map { LocalDate.ofEpochDay(it.epochDay) }.toSet()
            val current = StreakCalculator.currentStreak(completedDates, todayDate)
            val bestFromHistory = StreakCalculator.bestStreakFromHistory(completedDates)
            val best = maxOf(persistedBest, bestFromHistory, current)
            val missed = StreakCalculator.missedDates(completedDates, firstLaunch, todayDate)
            val todayEntity = days.find { it.epochDay == todayDate.toEpochDay() }

            StreakUiState(
                currentStreak = current,
                bestStreak = best,
                totalCompleted = completedDates.size,
                totalMissed = missed.size,
                todayCompleted = todayEntity != null,
                yesterdayCompleted = completedDates.contains(todayDate.minusDays(1)),
                yesterdayMissed = missed.contains(todayDate.minusDays(1)),
                todayQuote = todayEntity?.quote,
                todayLesson = Lessons.forDate(todayDate.toEpochDay()),
                completedDates = completedDates,
                missedDates = missed.toSet(),
                firstLaunchDate = firstLaunch
            )
        }
    }

    /** Full day-by-day history from first launch to today: completed days AND remembered missed days. */
    fun observeHistory(today: () -> LocalDate = { LocalDate.now() }): Flow<List<DayRecord>> =
        combine(dao.observeAll(), settings.firstLaunchDate) { days, firstLaunch ->
            val todayDate = today()
            val completedByEpoch = days.associateBy { it.epochDay }
            val records = mutableListOf<DayRecord>()
            var d = firstLaunch
            while (!d.isAfter(todayDate)) {
                val entity = completedByEpoch[d.toEpochDay()]
                if (entity != null) {
                    records.add(
                        DayRecord(
                            date = d,
                            completed = true,
                            isMissed = false,
                            isToday = d == todayDate,
                            isFuture = false,
                            streakAtCompletion = entity.streakAtCompletion,
                            quote = entity.quote,
                            lesson = entity.lesson
                        )
                    )
                } else if (d.isBefore(todayDate)) {
                    records.add(
                        DayRecord(
                            date = d,
                            completed = false,
                            isMissed = true,
                            isToday = false,
                            isFuture = false,
                            streakAtCompletion = 0,
                            quote = null,
                            lesson = Lessons.forDate(d.toEpochDay())
                        )
                    )
                }
                d = d.plusDays(1)
            }
            records.sortedByDescending { it.date }
        }

    /** Returns true if the extend succeeded, false if today was already completed. */
    suspend fun extendStreakForToday(today: LocalDate = LocalDate.now()): Boolean {
        settings.ensureFirstLaunchDateSet()
        val epochDay = today.toEpochDay()
        if (dao.getByEpochDay(epochDay) != null) return false

        val existingDays = dao.observeAll().first()
        val completedDates = existingDays.map { LocalDate.ofEpochDay(it.epochDay) }.toSet()
        val projectedStreak = StreakCalculator.currentStreak(completedDates + today, today)

        val entity = DayEntity(
            epochDay = epochDay,
            streakAtCompletion = projectedStreak,
            quote = Quotes.forDate(epochDay),
            lesson = Lessons.forDate(epochDay),
            completedAtEpochMillis = System.currentTimeMillis()
        )
        dao.insert(entity)
        settings.updateBestStreakIfHigher(projectedStreak)
        return true
    }

    suspend fun resetAllData() {
        dao.clearAll()
        settings.resetAll()
    }
}
