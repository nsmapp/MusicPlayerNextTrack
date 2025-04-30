package by.niaprauski.domain.repository

import by.niaprauski.domain.models.Track

interface TrackRepository {

    fun saveTrackInfo(tracks: List<Track>)

    fun getAll(): List<Track>

    fun ignoreTrack(trackId: Long)


}