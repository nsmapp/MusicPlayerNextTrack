package by.niaprauski.player.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import by.niaprauski.designsystem.theme.AppTheme
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
        Slider(
            value = trackProgress.progress,
            onValueChange = { newPosition -> onSeek(newPosition) },
            modifier = Modifier.fillMaxWidth(),
            valueRange = 0f..1f,
            steps = 0,
            thumb = {
                Spacer(
                    modifier = Modifier
                        .padding(vertical = AppTheme.padding.mini)
                        .size(AppTheme.viewSize.icon_micro)
                        .clip(RoundedCornerShape(AppTheme.viewSize.icon_micro))
                        .background(AppTheme.appColors.text)
                        .padding(vertical = AppTheme.viewSize.border_small)
                        .background(AppTheme.appColors.accent)
                )
            },
            track = {

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppTheme.viewSize.border_big)
                        .background(
                            AppTheme.appColors.accent,
                            shape = RoundedCornerShape(AppTheme.radius.micro)
                        )

                )
            }
        )

        TextMediumSmall(
            modifier = Modifier.padding(end = AppTheme.padding.default),
            text = trackProgress.currentPosition
        )
    }

}