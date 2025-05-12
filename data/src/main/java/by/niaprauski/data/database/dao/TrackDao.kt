package by.niaprauski.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.niaprauski.data.database.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Query("SELECT * FROM tracks WHERE is_ignore == 0")
    fun getAll(): List<TrackEntity>

    @Query("SELECT * FROM tracks")
    fun getAllAsFlow(): Flow<List<TrackEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(tracks: List<TrackEntity>)

    @Query("SELECT id FROM tracks WHERE path NOT IN (:paths)")
    fun getBrokenTracksIds(paths: List<String>): List<Long>

    @Query("DELETE FROM tracks WHERE id IN(:ids)")
    fun deleteByIds(ids: List<Long>)

    @Query("UPDATE tracks SET is_ignore = 1 WHERE id = :trackId")
    fun markTrackAsIgnore(trackId: Long)
}