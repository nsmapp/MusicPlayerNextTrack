package by.niaprauski.settings.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.ui.row.TextFieldRow
import by.niaprauski.designsystem.ui.text.TextBoldLarge
import by.niaprauski.translations.R

@Composable
fun PlayListSettingsView(
    playlistLimitSize: String,
    isPlayListLimitError: Boolean,
    onLimitTrackChanged: (String) -> Unit
) {
    TextBoldLarge(
        modifier = Modifier.padding(top = AppTheme.padding.default),
        text = stringResource(R.string.feature_settings_playback)
    )

    TextFieldRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(AppTheme.padding.mini),
        text = playlistLimitSize,
        label = stringResource(R.string.feature_settings_max_tracks),
        onValueChange = onLimitTrackChanged,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = isPlayListLimitError,
    )
}