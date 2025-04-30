package by.niaprauski.player.mapper

import by.niaprauski.domain.models.Track
import by.niaprauski.player.models.TrackModel
import by.niaprauski.utils.models.ITrack
import javax.inject.Inject

class TrackModelMapper @Inject constructor() {

    fun toDomainModel(track: ITrack): Track =
        with(track) {
            Track(
                id = 0,
                title = title,
                artist = artist,
                path = path,
            )
        }

    fun toModel(track: Track): TrackModel =
        with(track) {
            TrackModel(
                title = title,
                artist = artist,
                path = path,
            )
        }
}