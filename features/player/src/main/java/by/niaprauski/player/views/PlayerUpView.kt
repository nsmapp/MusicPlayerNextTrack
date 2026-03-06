package by.niaprauski.player.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.theme.icons.IIcon
import by.niaprauski.designsystem.ui.button.PlayerLiteButton
import by.niaprauski.translations.R

@Composable
fun PlayerUpView(
    onOpenLibraryListClick: () -> Unit,
    onSyncPlayListClick: () -> Unit,
    onOpenSettingsClick: () -> Unit,
    onOpenPlayListClick: () -> Unit,
    isSyncing: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        PlayerLiteButton(
            modifier = Modifier
                .size(AppTheme.viewSize.normal)
                .clip(RoundedCornerShape(AppTheme.viewSize.normal)),
            imageVector = IIcon.library,
            onClick = onOpenLibraryListClick,
            description = stringResource(R.string.feature_player_library)
        )

        PlayerLiteButton(
            modifier = Modifier
                .size(AppTheme.viewSize.normal)
                .clip(RoundedCornerShape(AppTheme.viewSize.normal)),
            imageVector = IIcon.playList,
            onClick = onOpenPlayListClick,
            description = stringResource(R.string.feature_player_playlist)
        )

        val rotationAngle by animateFloatAsState(
            targetValue = if (isSyncing) 360f else 0f,
            animationSpec = tween(durationMillis = if (isSyncing) 750 else 0),
            label = stringResource(R.string.feature_player_syncrotation)
        )

        PlayerLiteButton(
            modifier = Modifier
                .size(AppTheme.viewSize.normal)
                .clip(RoundedCornerShape(AppTheme.viewSize.normal))
                .rotate(rotationAngle),
            imageVector = IIcon.sync,
            onClick = onSyncPlayListClick,
            description = stringResource(R.string.feature_player_sync_playlist)
        )

        PlayerLiteButton(
            modifier = Modifier
                .size(AppTheme.viewSize.normal)
                .clip(RoundedCornerShape(AppTheme.viewSize.normal)),
            imageVector = IIcon.settings,
            onClick = onOpenSettingsClick,
            description = stringResource(R.string.feature_player_library_settings)
        )

    }
}