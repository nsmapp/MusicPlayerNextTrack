package by.niaprauski.library.mapper

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import by.niaprauski.domain.models.Track
import javax.inject.Inject

class TrackModelMapper @Inject constructor() {

    fun toMediaItem(track: Track): MediaItem = with(track) {

        val metadata =
            MediaMetadata.Builder()
                .setArtist(track.artist)
                .setTitle(track.title)
                .build()

        MediaItem.Builder()
            .setUri(track.path)
            .setMediaId(track.id.toString())
            .setMediaMetadata(metadata)
            .setMimeType(MimeTypes.AUDIO_MPEG)
            .build()
    }
}