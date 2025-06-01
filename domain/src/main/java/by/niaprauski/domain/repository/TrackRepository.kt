package by.niaprauski.domain.repository

import by.niaprauski.domain.models.SearchTrackFilter
import by.niaprauski.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {

    fun saveTrackInfo(tracks: List<Track>)

    fun getAll(): List<Track>

    fun getAllAsFlow(): Flow<List<Track>>

    fun getAllAsFlow(filter: SearchTrackFilter): Flow<List<Track>>

    fun markAsIgnoreTrack(trackId: Long)


}