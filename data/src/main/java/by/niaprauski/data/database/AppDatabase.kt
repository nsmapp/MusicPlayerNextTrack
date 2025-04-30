package by.niaprauski.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import by.niaprauski.data.database.dao.TrackDao
import by.niaprauski.data.database.entity.TrackEntity

@Database(entities = [TrackEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}


fun getRoom(context: Context) = Room.databaseBuilder(
    context,
    AppDatabase::class.java, "nexttrackdatabase"
).build()