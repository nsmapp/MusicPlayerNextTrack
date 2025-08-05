package by.niaprauski.player.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import by.niaprauski.designsystem.icons.Pause
import by.niaprauski.designsystem.icons.Play
import by.niaprauski.designsystem.icons.PlayNext
import by.niaprauski.designsystem.icons.PlayPrev
import by.niaprauski.designsystem.icons.Stop
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.theme.Icons
import by.niaprauski.designsystem.ui.button.PlayerLiteButton

@Composable
fun PlayerControlView(
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onShuffleModeClick: () -> Unit,
    onRepeatModeClick: () -> Unit,
    isPlaying: State<Boolean>?,
    shuffle: State<Boolean>?,
    repeatMode: State<Int>?,
    progressIndicator: @Composable () -> Unit,
) {


    Column(
        modifier = Modifier.wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PlayerLiteButton(
            modifier = Modifier
                .size(AppTheme.viewSize.icon_extra_large)
                .clip(RoundedCornerShape(AppTheme.viewSize.icon_extra_large)),
            imageVector = Icons.PlayNext,
            onClick = onNextClick,
            description = "Play next"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            PlayerLiteButton(
                modifier = Modifier
                    .size(AppTheme.viewSize.icon_big)
                    .clip(RoundedCornerShape(AppTheme.viewSize.icon_big)),
                imageVector = Icons.PlayPrev,
                onClick = onPreviousClick,
                description = "Play previous"
            )

            PlayPauseButton(isPlaying, onPauseClick, onPlayClick)

            PlayerLiteButton(
                modifier = Modifier
                    .size(AppTheme.viewSize.icon_big)
                    .clip(RoundedCornerShape(AppTheme.viewSize.icon_big)),
                imageVector = Icons.Stop,
                onClick = onStopClick,
                description = "Stop"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = "Shuffle: ${shuffle?.value}",
                modifier = Modifier.clickable { onShuffleModeClick }
            )
            Text(
                text = "Repeat Mode: ${repeatMode?.value}",
                modifier = Modifier.clickable { onRepeatModeClick }
            )
        }

        progressIndicator()
    }
}

@Composable
private fun PlayPauseButton(
    isPlaying: State<Boolean>?,
    onPauseClick: () -> Unit,
    onPlayClick: () -> Unit
) {

    val playIcon = if (isPlaying?.value == true) Icons.Pause else Icons.Play
    val playClickAction = if (isPlaying?.value == true) onPauseClick else onPlayClick

    PlayerLiteButton(
        modifier = Modifier
            .size(AppTheme.viewSize.icon_large)
            .clip(RoundedCornerShape(AppTheme.viewSize.icon_large)),
        imageVector = playIcon,
        onClick = playClickAction,
        description = "Play"
    )
}