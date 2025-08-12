package by.niaprauski.player.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.RepeatOn
import androidx.compose.material.icons.outlined.RepeatOne
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material.icons.outlined.ShuffleOn
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.SkipPrevious
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.ui.button.PlayerLiteButton
import by.niaprauski.playerservice.models.RepeatMode

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

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {

            ShuffleButton(shuffle, onShuffleModeClick)

            PlayPauseButton(isPlaying, onPauseClick, onPlayClick)

            RepeatModeButton(repeatMode, onRepeatModeClick)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {

            PlayerLiteButton(
                modifier = Modifier
                    .size(AppTheme.viewSize.icon_big)
                    .clip(RoundedCornerShape(AppTheme.viewSize.icon_big)),
                imageVector = Icons.Outlined.SkipPrevious,
                onClick = onPreviousClick,
                description = "Play previous"
            )


            PlayerLiteButton(
                modifier = Modifier
                    .size(AppTheme.viewSize.icon_large)
                    .clip(RoundedCornerShape(AppTheme.viewSize.icon_large)),
                imageVector = Icons.Outlined.Stop,
                onClick = onStopClick,
                description = "Stop"
            )

            PlayerLiteButton(
                modifier = Modifier
                    .size(AppTheme.viewSize.icon_big)
                    .clip(RoundedCornerShape(AppTheme.viewSize.icon_big)),
                imageVector = Icons.Outlined.SkipNext,
                onClick = onNextClick,
                description = "Play next"
            )
        }


        progressIndicator()
    }
}

@Composable
private fun RepeatModeButton(
    repeatMode: State<Int>?,
    onRepeatModeClick: () -> Unit
) {
    val repeatModeIcon = when (repeatMode?.value) {
        RepeatMode.REPEAT_MODE_OFF.value -> Icons.Outlined.Repeat
        RepeatMode.REPEAT_MODE_ONE.value -> Icons.Outlined.RepeatOne
        RepeatMode.REPEAT_MODE_ALL.value -> Icons.Outlined.RepeatOn
        else -> Icons.Outlined.Repeat
    }

    PlayerLiteButton(
        modifier = Modifier
            .size(AppTheme.viewSize.icon_small)
            .clip(RoundedCornerShape(AppTheme.viewSize.icon_small)),
        imageVector = repeatModeIcon,
        onClick = onRepeatModeClick,
        description = "Repeat mode"
    )
}


@Composable
fun ShuffleButton(
    shuffle: State<Boolean>?,
    onShuffleModeClick: () -> Unit
) {
    val shuffleIcon = if (shuffle?.value == true) Icons.Outlined.ShuffleOn
    else Icons.Outlined.Shuffle

    PlayerLiteButton(
        modifier = Modifier
            .size(AppTheme.viewSize.icon_small)
            .clip(RoundedCornerShape(AppTheme.viewSize.icon_small)),
        imageVector = shuffleIcon,
        onClick = onShuffleModeClick,
        description = "Shuffle"
    )
}

@Composable
private fun PlayPauseButton(
    isPlaying: State<Boolean>?,
    onPauseClick: () -> Unit,
    onPlayClick: () -> Unit
) {

    val playIcon = if (isPlaying?.value == true) Icons.Outlined.Pause else Icons.Outlined.PlayArrow
    val playClickAction = if (isPlaying?.value == true) onPauseClick else onPlayClick

    PlayerLiteButton(
        modifier = Modifier
            .size(AppTheme.viewSize.icon_extra_large)
            .clip(RoundedCornerShape(AppTheme.viewSize.icon_extra_large)),
        imageVector = playIcon,
        onClick = playClickAction,
        description = "Play"
    )
}