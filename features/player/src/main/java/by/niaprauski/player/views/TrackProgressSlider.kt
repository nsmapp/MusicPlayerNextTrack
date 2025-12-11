package by.niaprauski.player.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.playerservice.models.TrackProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackProgressSlider(
    trackProgress: State<TrackProgress>?,
    onSeek: (Float) -> Unit
) {

    val progressValue = trackProgress?.value?.progress ?: 0f

    Slider(
        value = progressValue,
        onValueChange = { newPosition -> onSeek(newPosition) },
        modifier = Modifier.fillMaxWidth(),
        valueRange = 0f..1f,
        steps = 0,
        thumb = {
            Spacer(
                modifier = Modifier
                    .padding(AppTheme.padding.mini)
                    .size(AppTheme.viewSize.icon_micro)
                    .clip(RoundedCornerShape(AppTheme.viewSize.icon_micro))
                    .background(AppTheme.appColors.text)
                    .padding(AppTheme.viewSize.border_small)
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
}