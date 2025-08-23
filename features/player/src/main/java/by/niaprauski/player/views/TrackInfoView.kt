package by.niaprauski.player.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.ui.text.TextBoldLarge
import by.niaprauski.designsystem.ui.text.TextMediumLarge
import by.niaprauski.translations.R

@Composable
fun TrackInfoView(
    artist: State<String>?,
    title: State<String>?
) {
    Column {
        TextBoldLarge(
            text = artist?.value ?: stringResource(R.string.feature_player_no_artist),
            maxLines = 1,
        )
        TextMediumLarge(
            modifier = Modifier.padding(top = AppTheme.padding.default),
            text = title?.value ?: stringResource(R.string.feature_player_no_track),
            maxLines = 2,
        )
    }
}