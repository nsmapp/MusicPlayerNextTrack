package by.niaprauski.player.views

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.ui.text.TextBoldLarge
import by.niaprauski.designsystem.ui.text.TextMediumLarge

@Composable
fun TrackInfoView(
    artist: String,
    title: String
) {
    Column {
        TextBoldLarge(
            modifier = Modifier.basicMarquee(),
            text = artist,
            maxLines = 1,
        )
        TextMediumLarge(
            modifier = Modifier
                .basicMarquee()
                .padding(top = AppTheme.padding.default),
            text = title,
            maxLines = 1,
        )
    }
}