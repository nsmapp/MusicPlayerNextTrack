package by.niaprauski.library.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Reply
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import by.niaprauski.designsystem.icons.SmallIcon
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.ui.button.PlayerLiteButton
import by.niaprauski.designsystem.ui.text.TextMedium
import by.niaprauski.domain.models.Track
import by.niaprauski.translations.R


@Composable
fun TrackItem(
    track: Track,
    onPlayClick: (Track) -> Unit,
    onIgnoreClick: (Track) -> Unit,
    onRestoreTrackClick: (Track) -> Unit,
) {
    val contentColor =
        if (track.isIgnore) AppTheme.appColors.text_ligth else AppTheme.appColors.text
    val trackIcon = remember(track.isRadio) {
        if (track.isRadio) Icons.Default.Radio else Icons.Default.MusicNote
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = AppTheme.padding.default, vertical = AppTheme.padding.mini),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {


        SmallIcon(
            imageVector = trackIcon,
        )

        Column(modifier = Modifier.weight(1f)) {
            TextMedium(
                text = track.fileName,
                color = contentColor,
                maxLines = 1,
            )
        }

        TrackControlView(
            onIgnoreClick,
            track,
            onPlayClick,
            onRestoreTrackClick
        )
    }
}

@Composable
private fun TrackControlView(
    onIgnoreClick: (Track) -> Unit,
    track: Track,
    onPlayClick: (Track) -> Unit,
    onRestoreTrackClick: (Track) -> Unit,
) {
    AnimatedContent(
        targetState = track.isIgnore,
        transitionSpec = {
            val enter = fadeIn(animationSpec = tween(300, delayMillis = 90)) +
                    scaleIn(initialScale = 0.7f, animationSpec = tween(300, delayMillis = 90))
            val exit = fadeOut(animationSpec = tween(90))

            enter togetherWith exit
        },
        content = { isIgnored ->
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (isIgnored) {
                    PlayerLiteButton(
                        modifier = Modifier
                            .size(AppTheme.viewSize.icon_normal)
                            .clip(RoundedCornerShape(AppTheme.viewSize.icon_normal)),
                        imageVector = remember { Icons.Rounded.Reply },
                        onClick = remember { { onRestoreTrackClick(track) } },
                        description = stringResource(R.string.feature_library_restore_track),
                    )

                } else {
                    PlayerLiteButton(
                        modifier = Modifier
                            .size(AppTheme.viewSize.icon_small)
                            .clip(RoundedCornerShape(AppTheme.viewSize.icon_small)),
                        imageVector = remember { Icons.Rounded.Cancel },
                        onClick = remember { { onIgnoreClick(track) } },
                        description = stringResource(R.string.feature_library_ignore),
                    )

                    PlayerLiteButton(
                        modifier = Modifier
                            .size(AppTheme.viewSize.icon_normal)
                            .clip(RoundedCornerShape(AppTheme.viewSize.icon_normal)),
                        imageVector = remember { Icons.Rounded.PlayArrow },
                        onClick = remember { { onPlayClick(track) } },
                        description = stringResource(R.string.feature_library_play),
                    )
                }
            }
        }
    )
}