package com.streaktracker.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.streaktracker.app.data.StreakRepository
import com.streaktracker.app.data.StreakUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class StatsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = StreakRepository(application)

    val uiState: StateFlow<StreakUiState> = repository.observeUiState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StreakUiState())
}
