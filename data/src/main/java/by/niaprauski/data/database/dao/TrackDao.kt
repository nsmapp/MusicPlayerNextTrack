package by.niaprauski.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.niaprauski.data.database.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Query("SELECT * FROM tracks WHERE is_ignore == 0 ORDER BY favorite DESC")
    fun getAll(): List<TrackEntity>

    @Query("SELECT id FROM tracks WHERE is_ignore = 0")
    fun getAllIdsWithoutIgnored(): List<String>

    @Query("SELECT * FROM tracks WHERE id IN (:ids) ORDER BY favorite DESC")
    fun getTracksByIds(ids: List<String>): List<TrackEntity>

    @Query("SELECT * FROM tracks")
    fun getAllAsFlow(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM tracks WHERE name LIKE ('%' || :text || '%') OR name LIKE ('%' || :text || '%') ")
    fun getAllAsFlow(text: String): Flow<List<TrackEntity>>

    @Query("SELECT * FROM tracks WHERE name LIKE ('%' || :text || '%') OR name LIKE ('%' || :text || '%') ")
    fun getPagedFlow(text: String): PagingSource<Int, TrackEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(tracks: List<TrackEntity>)

    @Query("SELECT id FROM tracks WHERE id NOT IN (:paths)")
    fun getBrokenTracksIds(paths: List<String>): List<String>

    @Query("DELETE FROM tracks WHERE id IN(:ids)")
    fun deleteByIds(ids: List<String>)

    @Query("UPDATE tracks SET is_ignore = 1 WHERE id = :trackId")
    fun markTrackAsIgnore(trackId: String)

    @Query("UPDATE tracks SET is_ignore = 0 WHERE id = :trackId")
    fun unmarkTrackAsIgnore(trackId: String)

    @Query("SELECT * FROM tracks WHERE id = :trackId")
    fun getById(trackId: String): TrackEntity?

    @Query("UPDATE tracks SET favorite = :value WHERE id = :trackId")
    fun upTrackFavorite(trackId: String, value: Int)
}