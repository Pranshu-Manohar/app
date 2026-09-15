package com.streaktracker.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.streaktracker.app.data.AppThemeMode
import com.streaktracker.app.data.StreakRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = StreakRepository(application)

    val themeMode: StateFlow<AppThemeMode> = repository.settings.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppThemeMode.SYSTEM)

    fun setThemeMode(mode: AppThemeMode) {
        viewModelScope.launch { repository.settings.setThemeMode(mode) }
    }

    fun resetAllData(onDone: () -> Unit) {
        viewModelScope.launch {
            repository.resetAllData()
            onDone()
        }
    }
}
