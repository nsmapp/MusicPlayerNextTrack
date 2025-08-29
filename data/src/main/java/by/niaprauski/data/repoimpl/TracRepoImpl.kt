package by.niaprauski.data.repoimpl

import by.niaprauski.data.database.dao.TrackDao
import by.niaprauski.data.mappers.TrackMapper
import by.niaprauski.domain.models.SearchTrackFilter
import by.niaprauski.domain.models.Track
import by.niaprauski.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TracRepoImpl @Inject constructor(
    private val trackDao: TrackDao,
    private val trackMapper: TrackMapper
): TrackRepository {


    override fun saveTrackInfo(tracks: List<Track>) {
        val validPaths = tracks.map { it.path }
        val brokenTracksIds = trackDao.getBrokenTracksIds(validPaths)
        trackDao.deleteByIds(brokenTracksIds)

        val list = tracks.map { track -> trackMapper.toEntity(track) }
        trackDao.insertAll(list)
    }

    override fun getAll(): List<Track> = trackDao.getAll()
        .map {track -> trackMapper.toModel(track) }

    override fun getAllAsFlow(): Flow<List<Track>> = trackDao
        .getAllAsFlow()
        .map { tracks ->
            tracks.map { trackMapper.toModel(it) }
        }

    override fun getAllAsFlow(filter: SearchTrackFilter): Flow<List<Track>> =
        trackDao.getAllAsFlow(filter.text)
            .map { tracks ->
                tracks.map { trackMapper.toModel(it) }
            }

    override fun markTrackAsIgnored(trackId: Long) {
        trackDao.markTrackAsIgnore(trackId)
    }

    override fun unmarkTrackAsIgnored(trackId: Long) {
        trackDao.unmarkTrackAsIgnore(trackId)
    }
}