package by.niaprauski.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tracks", indices = [Index(value = ["path"], unique = true)])
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo("path")
    val path: String,
    @ColumnInfo("is_ignore")
    val isIgnore: Boolean,
    @ColumnInfo("is_radio")
    val isRadio: Boolean
)
