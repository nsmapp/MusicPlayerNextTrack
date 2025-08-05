package by.niaprauski.player.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import by.niaprauski.playerservice.PlayerService
import by.niaprauski.playerservice.models.TrackProgress

@Composable
fun TrackProgressSlider(
    trackProgress: State<TrackProgress>?,
    playerService: PlayerService?
) {

    val progressValue = trackProgress?.value?.progress ?: 0f

    Slider(
        value = progressValue ,
        onValueChange = { newPosition -> playerService?.seekTo(newPosition) },
        modifier = Modifier.fillMaxWidth(),
        valueRange = 0f..1f,
        steps = 0,
    )
}