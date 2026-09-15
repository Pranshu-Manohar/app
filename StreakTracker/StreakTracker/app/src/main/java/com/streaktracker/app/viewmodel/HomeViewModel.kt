package com.streaktracker.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.streaktracker.app.data.StreakRepository
import com.streaktracker.app.data.StreakUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StreakRepository(application)

    val uiState: StateFlow<StreakUiState> = repository.observeUiState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StreakUiState())

    private val _showCelebration = MutableStateFlow(false)
    val showCelebration: StateFlow<Boolean> = _showCelebration

    private val _lastAction = MutableStateFlow<String?>(null)
    val lastAction: StateFlow<String?> = _lastAction

    fun extendStreak() {
        viewModelScope.launch {
            val before = uiState.value.currentStreak
            val success = repository.extendStreakForToday()
            if (success) {
                val after = before + 1
                _lastAction.value = "extended"
                if (after == 21 || (after > 21 && before < 21)) {
                    _showCelebration.value = true
                }
            }
        }
    }

    fun dismissCelebration() {
        _showCelebration.value = false
    }
}
