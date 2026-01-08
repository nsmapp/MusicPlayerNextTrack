package by.niaprauski.library.view

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
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Reply
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
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
    val contentColor = if (track.isIgnore) AppTheme.appColors.text_ligth else AppTheme.appColors.text

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(AppTheme.padding.default),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {


        Column(modifier = Modifier.weight(1f)) {
            TextMedium(
                modifier = Modifier
                    .padding(bottom = AppTheme.padding.default),
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
    Row(
        modifier = Modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (track.isIgnore.not()) {
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

        } else {

            PlayerLiteButton(
                modifier = Modifier
                    .size(AppTheme.viewSize.icon_normal)
                    .clip(RoundedCornerShape(AppTheme.viewSize.icon_normal)),
                imageVector = remember { Icons.Rounded.Reply },
                onClick = remember { { onRestoreTrackClick(track) } },
                description = stringResource(R.string.feature_library_restore_track),
            )

        }
    }
}