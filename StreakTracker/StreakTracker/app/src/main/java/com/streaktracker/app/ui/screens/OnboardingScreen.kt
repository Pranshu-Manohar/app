package com.streaktracker.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🔥", fontSize = 56.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            "Build consistency, one day at a time.",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(24.dp))
        OnboardingLine("1", "Press Extend Streak once every day.")
        OnboardingLine("2", "Build toward 21 consecutive days.")
        OnboardingLine("3", "Missed days are remembered.")
        OnboardingLine("4", "Learn from every day.")
        Spacer(Modifier.height(32.dp))
        Button(onClick = onFinished, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text("Get started")
        }
    }
}

@Composable
private fun OnboardingLine(number: String, text: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text("$number.  $text", style = MaterialTheme.typography.bodyLarge)
    }
}
