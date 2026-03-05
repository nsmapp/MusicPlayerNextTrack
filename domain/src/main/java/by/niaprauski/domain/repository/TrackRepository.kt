package by.niaprauski.domain.repository

import androidx.paging.PagingData
import by.niaprauski.domain.models.SearchTrackFilter
import by.niaprauski.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {

    fun saveTrackInfo(tracks: List<Track>)

    fun getAll(): List<Track>

    fun getRandom(limit: Int): List<Track>

    fun getAllAsFlow(): Flow<List<Track>>

    fun getAllAsFlow(filter: SearchTrackFilter): Flow<List<Track>>

    fun getPagedFlow(filter: SearchTrackFilter): Flow<PagingData<Track>>

    fun markTrackAsIgnored(trackId: String)

    fun unmarkTrackAsIgnored(trackId: String)

    fun upTrackFavorite(trackId: String, value: Int)

    fun getTrackById(trackId: String): Track?
}