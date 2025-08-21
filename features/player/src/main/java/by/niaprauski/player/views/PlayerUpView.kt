package by.niaprauski.player.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlaylistPlay
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.ui.button.PlayerLiteButton
import by.niaprauski.translations.R

@Composable
fun PlayerUpView(
    onOpenPlayListClick: () -> Unit,
    onOpenSettingsClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        PlayerLiteButton(
            modifier = Modifier
                .padding(AppTheme.padding.normal)
                .size(AppTheme.viewSize.icon_normal)
                .clip(RoundedCornerShape(AppTheme.viewSize.icon_normal)),
            imageVector = Icons.Outlined.PlaylistPlay,
            onClick = onOpenPlayListClick,
            description = stringResource(R.string.feature_player_library)
        )

        PlayerLiteButton(
            modifier = Modifier
                .padding(AppTheme.padding.normal)
                .size(AppTheme.viewSize.icon_normal)
                .clip(RoundedCornerShape(AppTheme.viewSize.icon_normal)),
            imageVector = Icons.Outlined.Settings,
            onClick = onOpenSettingsClick,
            description = stringResource(R.string.feature_player_library_settings)
        )

    }
}