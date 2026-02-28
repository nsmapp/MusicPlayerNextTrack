package by.niaprauski.player.mapper

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import by.niaprauski.domain.models.Track
import by.niaprauski.domain.utils.DispatcherProvider
import by.niaprauski.utils.models.ITrack
import by.niaprauski.utils.models.TRACK_KEY_FAVORITE
import by.niaprauski.utils.models.TRACK_KEY_ID
import by.niaprauski.utils.models.TRACK_KEY_NAME
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
                        isRadio = isRadio,
                        duration = duration,
                        favorite = 0, //TODO ?
                    )
                }
            }
        }


    suspend fun toMediaItems(tracks: List<Track>): List<MediaItem> =
        withContext(dispatcherProvider.io) {
            tracks.map { track ->

                val extras = Bundle().apply {
                    putLong(TRACK_KEY_ID, track.id)
                    putInt(TRACK_KEY_FAVORITE, track.favorite)
                    putString(TRACK_KEY_NAME, track.fileName)
                }

                MediaItem.Builder()
                    .setMediaId(track.pathOrUrl)
                    .setUri(track.pathOrUrl)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setDurationMs(track.duration)
                            .setExtras(extras)
                            .build()
                    )
                    .build()
            }
        }
}