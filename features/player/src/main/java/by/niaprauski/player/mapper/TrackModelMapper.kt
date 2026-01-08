package by.niaprauski.player.mapper

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import by.niaprauski.domain.models.Track
import by.niaprauski.domain.utils.DispatcherProvider
import by.niaprauski.utils.models.ITrack
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrackModelMapper @Inject constructor(
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun toDomainModels(tracks: List<ITrack>): List<Track> =
        withContext(dispatcherProvider.io) {
            tracks.map { track ->
                with(track) {
                    Track(
                        id = 0,
                        fileName = fileName,
                        pathOrUrl = pathOrUrl,
                        isIgnore = false,
                        isRadio = isRadio
                    )
                }
            }
        }


    suspend fun toMediaItems(tracks: List<Track>): List<MediaItem> =
        withContext(dispatcherProvider.io) {
            tracks.map { track ->
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
        }
}