package by.niaprauski.player.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.utils.constants.EMPTY_FLOW_ARRAY
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn( FlowPreview::class)
@Composable
fun WaveBarView(
    modifier: Modifier,
    waveformFlow: StateFlow<FloatArray>?,
    isPlaying: Boolean,
) {

    val debouncedWaveform by produceState(initialValue = EMPTY_FLOW_ARRAY, waveformFlow) {
        waveformFlow?.debounce(30L)?.collect { value = it }
    }

    if (isPlaying && debouncedWaveform.isNotEmpty()) {
        WaveformVisualizer(
            modifier = modifier,
            waveform = debouncedWaveform,
            barColor = AppTheme.appColors.text_ligth
        )
    }
}