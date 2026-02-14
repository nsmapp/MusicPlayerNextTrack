package by.niaprauski.player.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.ui.slider.CSlider
import by.niaprauski.designsystem.ui.text.TextMediumSmall
import by.niaprauski.playerservice.models.TrackProgress
import kotlinx.coroutines.flow.StateFlow

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackProgressSlider(
    trackProgress: StateFlow<TrackProgress>?,
    onSeek: (Float) -> Unit
) {

    val trackProgress by trackProgress?.collectAsStateWithLifecycle(initialValue = TrackProgress.DEFAULT)
        ?: remember { mutableStateOf(TrackProgress.DEFAULT) }

    Box(
        contentAlignment = Alignment.BottomEnd
    ) {

        CSlider(
            modifier = Modifier.fillMaxWidth(),
            trackProgress = trackProgress.progress,
            valueRange = 0f..1f,
            steps = 0,
            onValueChange = { newPosition -> onSeek(newPosition) }
        )

        TextMediumSmall(
            modifier = Modifier.padding(end = AppTheme.padding.default),
            text = trackProgress.currentPosition
        )
    }

}