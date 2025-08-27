package by.niaprauski.utils.handlers

import android.content.ContentResolver
import android.provider.MediaStore
import by.niaprauski.utils.models.ITrack

object MediaHandler {

    fun getTrackData(cr: ContentResolver): List<ITrack> {

        val trackProjection: Array<String> = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION
        )

        val iTracks = mutableListOf<ITrack>()

        val cursor = cr.query(
            /* uri = */ MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            /* projection = */ trackProjection,
            /* selection = */ null,
            /* selectionArgs = */ null,
            /* sortOrder = */ null
        )

        cursor?.use { c ->
            while (c.moveToNext()) {
                val title = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                val duration = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))

                if (duration >= 5000) {

                    val iTrack = object : ITrack {
                        override val title = title
                        override val artist = artist
                        override val path = path
                    }
                    iTracks.add(iTrack)
                }

            }
        }
        cursor?.close()

        return iTracks
    }
}