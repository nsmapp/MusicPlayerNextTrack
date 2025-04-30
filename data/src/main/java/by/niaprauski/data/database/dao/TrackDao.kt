package by.niaprauski.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.niaprauski.data.database.entity.TrackEntity

@Dao
interface TrackDao {
    @Query("SELECT * FROM tracks")
    fun getAll(): List<TrackEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tracks: List<TrackEntity>)
}