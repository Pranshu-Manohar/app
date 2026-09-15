package com.streaktracker.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.streaktracker.app.data.AppThemeMode
import com.streaktracker.app.data.StreakRepository
import com.streaktracker.app.navigation.AppNavHost
import com.streaktracker.app.ui.screens.OnboardingScreen
import com.streaktracker.app.ui.theme.StreakTrackerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StreakApp()
        }
    }
}

@Composable
private fun StreakApp() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val repository = remember { StreakRepository(context) }
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    val themeMode by repository.settings.themeMode.collectAsState(initial = AppThemeMode.SYSTEM)
    val onboardingDone by repository.settings.onboardingDone.collectAsState(initial = true)
    var localOnboardingDone by remember(onboardingDone) { mutableStateOf(onboardingDone) }

    val darkTheme = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
    }

    StreakTrackerTheme(themeMode = themeMode) {
        Surface(modifier = Modifier, color = androidx.compose.material3.MaterialTheme.colorScheme.background) {
            if (!localOnboardingDone) {
                OnboardingScreen(onFinished = {
                    scope.launch {
                        repository.settings.setOnboardingDone()
                        repository.settings.ensureFirstLaunchDateSet()
                    }
                    localOnboardingDone = true
                })
            } else {
                AppNavHost(isDarkTheme = darkTheme)
            }
        }
    }
}
