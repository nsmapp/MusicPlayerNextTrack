package by.niaprauski.player.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import by.niaprauski.designsystem.theme.AppTheme
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn( FlowPreview::class)
@Composable
fun WaveBarView(
    modifier: Modifier,
    waveformFlow: StateFlow<FloatArray>?,
    isPlaying: Boolean,
) {

    val debouncedWaveform by remember(waveformFlow) {
        waveformFlow?.debounce(30L) ?: flowOf(FloatArray(0))
    }.collectAsState(initial = FloatArray(0))

    if (isPlaying && debouncedWaveform.isNotEmpty()) {
        WaveformVisualizer(
            modifier = modifier,
            waveform = debouncedWaveform,
            barColor = AppTheme.appColors.accent50
        )
    }
}