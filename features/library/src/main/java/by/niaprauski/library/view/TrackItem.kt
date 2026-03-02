package by.niaprauski.library.view

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Reply
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.ui.button.PlayerLiteButton
import by.niaprauski.designsystem.ui.text.TextMedium
import by.niaprauski.library.models.TrackModel
import by.niaprauski.translations.R


@Composable
fun TrackItem(
    track: TrackModel,
    onPlayClick: (TrackModel) -> Unit,
    onIgnoreClick: (TrackModel) -> Unit,
    onRestoreTrackClick: (TrackModel) -> Unit,
) {

    val textColor = AppTheme.appColors.text
    val textColorLight = AppTheme.appColors.text_ligth

    val contentColor = remember(track.isIgnore, textColor, textColorLight) {
        if (track.isIgnore) textColorLight else textColor
    }
    val dividerColor = remember(contentColor) {
        contentColor.copy(alpha = 0.2f)
    }

    val density = LocalDensity.current
    val dividerPx = remember(density) { with(density) { 1.dp.toPx() } }
    val dividerOffsetPx = remember(density) { with(density) { 8.dp.toPx() } }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val y = size.height - dividerPx / 2
                drawLine(
                    color = dividerColor,
                    start = Offset(dividerOffsetPx, y),
                    end = Offset(size.width - dividerOffsetPx, y),
                    strokeWidth = dividerPx
                )
            }
            .wrapContentHeight()
            .clickable { onPlayClick(track) }
            .padding(horizontal = AppTheme.padding.default, vertical = AppTheme.padding.default),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(modifier = Modifier.weight(1f)) {
            TextMedium(
                text = track.nameWithFavorite,
                color = contentColor,
                maxLines = 1,
            )
        }

        TrackControlView(
            track,
            onIgnoreClick,
            onRestoreTrackClick
        )
    }
}

@Composable
private fun TrackControlView(
    track: TrackModel,
    onIgnoreClick: (TrackModel) -> Unit,
    onRestoreTrackClick: (TrackModel) -> Unit,
) {

    Crossfade(targetState = track.isIgnore, label = "TrackItemControl") { isIgnored ->
        if (isIgnored) {
            PlayerLiteButton(
                modifier = Modifier
                    .size(AppTheme.viewSize.icon_small)
                    .clip(CircleShape),
                imageVector = remember { Icons.Rounded.Reply },
                onClick = { onRestoreTrackClick(track) },
                description = stringResource(R.string.feature_library_restore_track),
            )
        } else {
            PlayerLiteButton(
                modifier = Modifier
                    .size(AppTheme.viewSize.icon_small)
                    .clip(CircleShape),
                imageVector = remember { Icons.Rounded.Cancel },
                onClick = { onIgnoreClick(track) },
                description = stringResource(R.string.feature_library_ignore),
            )
        }
    }

}