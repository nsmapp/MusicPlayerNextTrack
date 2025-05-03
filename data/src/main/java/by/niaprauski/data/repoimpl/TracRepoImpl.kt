package by.niaprauski.data.repoimpl

import by.niaprauski.data.database.dao.TrackDao
import by.niaprauski.data.mappers.TrackMapper
import by.niaprauski.domain.models.Track
import by.niaprauski.domain.repository.TrackRepository
import javax.inject.Inject

class TracRepoImpl @Inject constructor(
    private val trackDao: TrackDao,
    private val trackMapper: TrackMapper
): TrackRepository {


    //TODO remove inactive tracks
    override fun saveTrackInfo(tracks: List<Track>) {

        val list = tracks.map { track -> trackMapper.toEntity(track) }
        trackDao.insertAll(list)
    }

    override fun getAll(): List<Track> = trackDao.getAll()
        .map {track -> trackMapper.toModel(track) }

    override fun ignoreTrack(trackId: Long) {
        TODO("Not yet implemented")
    }
}