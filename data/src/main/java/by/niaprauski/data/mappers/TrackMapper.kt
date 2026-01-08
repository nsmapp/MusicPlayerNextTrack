package by.niaprauski.data.mappers

import by.niaprauski.data.database.entity.TrackEntity
import by.niaprauski.domain.models.Track
import javax.inject.Inject

class TrackMapper @Inject constructor(){

    fun toEntity(track :Track): TrackEntity =
        with(track) {
            TrackEntity(
                id = id,
                name = fileName,
                path = pathOrUrl,
                isIgnore = isIgnore,
                isRadio,
            )
        }

    fun toModel(track : TrackEntity): Track =
        with(track) {
            Track(
                id = id,
                fileName = name,
                pathOrUrl = path,
                isIgnore = isIgnore,
                isRadio = isRadio,
            )
        }


}