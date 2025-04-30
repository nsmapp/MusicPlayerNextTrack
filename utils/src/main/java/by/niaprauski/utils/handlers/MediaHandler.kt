package by.niaprauski.utils.handlers

import android.content.ContentResolver
import android.provider.MediaStore
import by.niaprauski.utils.models.ITrack

object MediaHandler {

    fun getTrackData(cr: ContentResolver): List<ITrack> {

        val projection: Array<String> = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
        )

        val ITracks = mutableListOf<ITrack>()

        val cursor = cr.query(
            /* uri = */ MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            /* projection = */ projection,
            /* selection = */ null,
            /* selectionArgs = */ null,
            /* sortOrder = */ null
        )

        cursor?.use { c ->
            while (c.moveToNext()) {
                val title = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))

                val ITrack = object : ITrack {
                    override val title = title
                    override val artist = artist
                    override val path = path
                }
                ITracks.add(ITrack)
            }
        }
        cursor?.close()

        return ITracks
    }


}