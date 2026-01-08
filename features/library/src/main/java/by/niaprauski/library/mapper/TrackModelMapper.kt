package by.niaprauski.library.mapper

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import by.niaprauski.domain.models.Track
import javax.inject.Inject

class TrackModelMapper @Inject constructor() {

    fun toMediaItem(track: Track): MediaItem =
        MediaItem.Builder()
            .setMediaId(track.pathOrUrl)
            .setUri(track.pathOrUrl)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setDisplayTitle(track.fileName)
                    .build()
            )
            .build()
}