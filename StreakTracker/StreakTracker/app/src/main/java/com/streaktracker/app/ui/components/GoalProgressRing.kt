package com.streaktracker.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GoalProgressRing(
    current: Int,
    goal: Int = 21,
    modifier: Modifier = Modifier
) {
    val fraction = (current.toFloat() / goal.toFloat()).coerceIn(0f, 1f)
    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(600),
        label = "progressRing"
    )
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val progressColor = MaterialTheme.colorScheme.primary

    Box(modifier = modifier.size(110.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(110.dp)) {
            val stroke = Stroke(width = 12f, cap = StrokeCap.Round)
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke,
                size = Size(size.width - 12f, size.height - 12f),
                topLeft = androidx.compose.ui.geometry.Offset(6f, 6f)
            )
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedFraction,
                useCenter = false,
                style = stroke,
                size = Size(size.width - 12f, size.height - 12f),
                topLeft = androidx.compose.ui.geometry.Offset(6f, 6f)
            )
        }
        Text(
            text = "${current.coerceAtMost(goal)}/$goal",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
    }
}
