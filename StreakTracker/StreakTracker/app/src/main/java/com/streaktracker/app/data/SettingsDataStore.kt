package com.streaktracker.app.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

private val Context.dataStore by preferencesDataStore(name = "streak_settings")

enum class AppThemeMode { SYSTEM, LIGHT, DARK }

class SettingsDataStore(private val context: Context) {

    private object Keys {
        val THEME = stringPreferencesKey("theme_mode")
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val FIRST_LAUNCH_EPOCH_DAY = longPreferencesKey("first_launch_epoch_day")
        val BEST_STREAK = longPreferencesKey("best_streak")
    }

    val themeMode: Flow<AppThemeMode> = context.dataStore.data.map { prefs ->
        when (prefs[Keys.THEME]) {
            "LIGHT" -> AppThemeMode.LIGHT
            "DARK" -> AppThemeMode.DARK
            else -> AppThemeMode.SYSTEM
        }
    }

    suspend fun setThemeMode(mode: AppThemeMode) {
        context.dataStore.edit { it[Keys.THEME] = mode.name }
    }

    val onboardingDone: Flow<Boolean> = context.dataStore.data.map { it[Keys.ONBOARDING_DONE] ?: false }

    suspend fun setOnboardingDone() {
        context.dataStore.edit { it[Keys.ONBOARDING_DONE] = true }
    }

    /** The date the app was first used. Missed-day history is only ever computed from this date forward. */
    val firstLaunchDate: Flow<LocalDate> = context.dataStore.data.map { prefs ->
        val epoch = prefs[Keys.FIRST_LAUNCH_EPOCH_DAY]
        if (epoch != null) LocalDate.ofEpochDay(epoch) else LocalDate.now()
    }

    suspend fun ensureFirstLaunchDateSet() {
        context.dataStore.edit { prefs ->
            if (prefs[Keys.FIRST_LAUNCH_EPOCH_DAY] == null) {
                prefs[Keys.FIRST_LAUNCH_EPOCH_DAY] = LocalDate.now().toEpochDay()
            }
        }
    }

    /** Best streak is persisted explicitly (in addition to being derivable) so it is never lost or recomputed wrong. */
    val bestStreak: Flow<Int> = context.dataStore.data.map { (it[Keys.BEST_STREAK] ?: 0L).toInt() }

    suspend fun updateBestStreakIfHigher(candidate: Int) {
        context.dataStore.edit { prefs ->
            val current = (prefs[Keys.BEST_STREAK] ?: 0L).toInt()
            if (candidate > current) prefs[Keys.BEST_STREAK] = candidate.toLong()
        }
    }

    suspend fun resetAll() {
        context.dataStore.edit { it.clear() }
    }
}
