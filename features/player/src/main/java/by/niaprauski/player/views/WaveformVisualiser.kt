package by.niaprauski.player.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun WaveformVisualizer(
    waveform: FloatArray,
    modifier: Modifier = Modifier,
    barColor: Color,
    barGap: Dp = 2.dp,
    minBarHeight: Dp = 1.dp
) {
    val animatedWaveform = waveform.map { value ->
        animateFloatAsState(
            targetValue = value,
            animationSpec = tween(30),
            label = "waveformValue"
        ).value
    }

    Canvas(modifier = modifier) {
        if (animatedWaveform.isEmpty()) return@Canvas

        val totalGapsWidth = (animatedWaveform.size - 1) * barGap.toPx()
        val totalBarWidth = size.width - totalGapsWidth
        val barWidth = totalBarWidth / animatedWaveform.size

        animatedWaveform.forEachIndexed { index, value ->
            val barHeight = (size.height * value).coerceAtLeast(minBarHeight.toPx())
            val x = index * (barWidth + barGap.toPx())
            val y = (size.height - barHeight)

            drawRoundRect(
                color = barColor,
                topLeft = Offset(x = x, y = y),
                size = Size(width = barWidth, height = barHeight),
                cornerRadius = CornerRadius(x = 4f, y = 4f)
            )
        }
    }
}