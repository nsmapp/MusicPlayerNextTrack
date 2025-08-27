package by.niaprauski.player.mapper

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import by.niaprauski.domain.models.Track
import by.niaprauski.domain.utils.DispatcherProvider
import by.niaprauski.player.models.TrackModel
import by.niaprauski.utils.models.ITrack
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrackModelMapper @Inject constructor(
    private val dispatcherProvider: DispatcherProvider
) {

    fun toDomainModel(track: ITrack): Track = with(track) {
        Track(
            id = 0,
            title = title,
            artist = artist,
            path = path,
            isIgnore = false,
        )
    }

    fun toModel(track: Track): TrackModel = with(track) {
        TrackModel(
            title = title,
            artist = artist,
            path = path,
        )
    }

    suspend fun toMediaItems(tracks: List<Track>): List<MediaItem> =
        withContext(dispatcherProvider.io) {

            tracks.map { track -> toMediaItem(track) }

        }

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