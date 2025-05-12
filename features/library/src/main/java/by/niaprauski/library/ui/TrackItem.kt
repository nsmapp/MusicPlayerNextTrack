package by.niaprauski.library.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import by.niaprauski.domain.models.Track


@Composable
fun TrackItem(
    track: Track,
    onPlayClick: (Track) -> Unit,
    onIgnoreClick: (Track) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
    ) {


        Text(text = track.title)
        Text(text = track.artist)
        Text(text = "Track ignore: ${track.isIgnore}")

        Text(
            text = "Play",
            modifier = Modifier.clickable {
                onPlayClick(track)

            })
        Text(
            text = "Ignore",
            modifier = Modifier.clickable {
                onIgnoreClick(track)

            })
    }
}