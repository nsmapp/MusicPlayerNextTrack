package by.niaprauski.player.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.theme.icons.IIcon
import by.niaprauski.designsystem.ui.button.PlayerLiteButton
import by.niaprauski.playerservice.models.RepeatMode
import by.niaprauski.translations.R

@Composable
fun PlayerControlView(
    modifier: Modifier = Modifier,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onShuffleModeClick: () -> Unit,
    onRepeatModeClick: () -> Unit,
    isPlaying: Boolean,
    shuffle: Boolean,
    repeatMode: Int,
    progressIndicator: @Composable () -> Unit,
) {


    Column(
        modifier = modifier,
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
                    .size(AppTheme.viewSize.big)
                    .clip(RoundedCornerShape(AppTheme.viewSize.big)),
                imageVector = IIcon.skipPrevious,
                onClick = onPreviousClick,
                description = stringResource(R.string.feature_player_play_previous)
            )


            PlayerLiteButton(
                modifier = Modifier
                    .size(AppTheme.viewSize.large)
                    .clip(RoundedCornerShape(AppTheme.viewSize.large)),
                imageVector = IIcon.stop,
                onClick = onStopClick,
                description = stringResource(R.string.feature_player_stop)
            )

            PlayerLiteButton(
                modifier = Modifier
                    .size(AppTheme.viewSize.big)
                    .clip(RoundedCornerShape(AppTheme.viewSize.big)),
                imageVector = IIcon.skipNext,
                onClick = onNextClick,
                description = stringResource(R.string.feature_player_play_next)
            )
        }


        progressIndicator()
    }
}

@Composable
private fun RepeatModeButton(
    repeatMode: Int,
    onRepeatModeClick: () -> Unit
) {
    val repeatModeIcon = when (repeatMode) {
        RepeatMode.REPEAT_MODE_OFF.value -> IIcon.repeat
        RepeatMode.REPEAT_MODE_ONE.value -> IIcon.repeatOne
        RepeatMode.REPEAT_MODE_ALL.value -> IIcon.repeatOn
        else -> IIcon.repeat
    }

    PlayerLiteButton(
        modifier = Modifier
            .size(AppTheme.viewSize.small)
            .clip(RoundedCornerShape(AppTheme.viewSize.small)),
        imageVector = repeatModeIcon,
        onClick = onRepeatModeClick,
        description = stringResource(R.string.feature_player_repeat_mode)
    )
}


@Composable
fun ShuffleButton(
    shuffle: Boolean,
    onShuffleModeClick: () -> Unit
) {
    val shuffleIcon = if (shuffle) IIcon.shuffleOn
    else IIcon.shuffle

    PlayerLiteButton(
        modifier = Modifier
            .size(AppTheme.viewSize.small)
            .clip(RoundedCornerShape(AppTheme.viewSize.small)),
        imageVector = shuffleIcon,
        onClick = onShuffleModeClick,
        description = stringResource(R.string.feature_player_shuffle)
    )
}

@Composable
private fun PlayPauseButton(
    isPlaying: Boolean,
    onPauseClick: () -> Unit,
    onPlayClick: () -> Unit
) {

    val playIcon = if (isPlaying) IIcon.pause else IIcon.play
    val playClickAction = if (isPlaying) onPauseClick else onPlayClick

    PlayerLiteButton(
        modifier = Modifier
            .size(AppTheme.viewSize.extra_large)
            .clip(RoundedCornerShape(AppTheme.viewSize.extra_large)),
        imageVector = playIcon,
        onClick = playClickAction,
        description = stringResource(R.string.feature_player_play)
    )
}