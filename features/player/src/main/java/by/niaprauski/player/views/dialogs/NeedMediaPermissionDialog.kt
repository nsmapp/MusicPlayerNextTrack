package by.niaprauski.player.views.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.theme.dimens.defaultRoundedShape
import by.niaprauski.designsystem.ui.text.TextBold
import by.niaprauski.designsystem.ui.text.TextMedium
import by.niaprauski.translations.R


@Composable
fun NeedMediaPermissionDialog(
    onOpenSettingsClick: () -> Unit,
    onDismissClick: () -> Unit,
) {

    Dialog(onDismissRequest = { onDismissClick() }) {
        Column(
            modifier = Modifier
                .clip(defaultRoundedShape)
                .background(AppTheme.appColors.background)
                .padding(AppTheme.padding.normal),
            horizontalAlignment = Alignment.End
        ) {
            TextMedium(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.feature_player_need_media_permission_message)
            )

            Row(modifier = Modifier.padding(top = AppTheme.padding.medium)) {

                TextBold(
                    modifier = Modifier.clickable { onDismissClick() },
                    text = stringResource(R.string.feature_player_close),
                )

                Spacer(modifier = Modifier.width(AppTheme.padding.normal))

                TextBold(
                    modifier = Modifier.clickable {
                        onOpenSettingsClick()
                        onDismissClick()
                    },
                    text = stringResource(R.string.feature_player_open_settings),
                )

            }
        }
    }
}
