package by.niaprauski.domain.repository

import androidx.paging.PagingData
import by.niaprauski.domain.models.SearchTrackFilter
import by.niaprauski.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {

    fun saveTrackInfo(tracks: List<Track>)

    fun getAll(): List<Track>

    fun getAllAsFlow(): Flow<List<Track>>

    fun getAllAsFlow(filter: SearchTrackFilter): Flow<List<Track>>

    fun getPagedFlow(filter: SearchTrackFilter): Flow<PagingData<Track>>

    fun markTrackAsIgnored(trackId: Long)

    fun unmarkTrackAsIgnored(trackId: Long)


}