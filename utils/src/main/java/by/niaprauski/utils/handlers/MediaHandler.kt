package by.niaprauski.utils.handlers

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns._ID
import android.provider.MediaStore
import by.niaprauski.utils.models.ITrack
import java.io.BufferedReader
import java.io.InputStreamReader
import android.provider.MediaStore.Files.FileColumns.MIME_TYPE
import android.provider.MediaStore.Files.FileColumns.DISPLAY_NAME
import android.provider.MediaStore.Files.FileColumns.DATA
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import androidx.media3.common.MediaItem
import by.niaprauski.utils.constants.TEXT_EMPTY
import by.niaprauski.utils.models.MimeType

object MediaHandler {

    fun getTrackData(
        cr: ContentResolver,
    ): List<ITrack> {

        val iTracks = mutableListOf<ITrack>()

        val projection: Array<String> = arrayOf(
            _ID,
            DISPLAY_NAME,
            DATA,
            MIME_TYPE,
            MediaStore.Audio.Media.DURATION,
        )

        val selection = "($MIME_TYPE = ? OR $MIME_TYPE = ? OR " +
                "($MIME_TYPE = ? OR $MIME_TYPE = ?) AND ${MediaStore.Audio.Media.DURATION} >= 0)"

        val selectionArgs = arrayOf(
            MimeType.PLS.type,
            MimeType.M3U.type,
            MimeType.OGG.type,
            MimeType.MPEG.type,
        )

        val cursor =
            cr.query(/* uri = */ MediaStore.Files.getContentUri("external"),/* projection = */
                projection,/* selection = */
                selection,/* selectionArgs = */
                selectionArgs,/* sortOrder = */
                null
            )

        cursor?.use { c ->
            while (c.moveToNext()) {
                val id = c.getLong(c.getColumnIndexOrThrow(_ID)) ?: continue
                val path = c.getString(c.getColumnIndexOrThrow(DATA)) ?: continue
                val displayName = c.getString(c.getColumnIndexOrThrow(DISPLAY_NAME)) ?: TEXT_EMPTY
                val mimeType = c.getStringOrNull(c.getColumnIndexOrThrow(MIME_TYPE)) ?: TEXT_EMPTY
                val duration = c.getLongOrNull(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))


                val isRadio = when (mimeType) {
                    MimeType.M3U.type, MimeType.PLS.type -> true
                    else -> false
                }

                val urlOrPath: String? = if (!isRadio) path
                else {
                    val contentUri = getRadioStreamUrlByIdOrNull(id)
                    parsePlaylistForStreamUrl(contentUri, cr)
                }

                if (urlOrPath != null) {
                    val iTrack = object : ITrack {
                        override val fileName = displayName
                        override val artist = TEXT_EMPTY
                        override val pathOrUrl = urlOrPath ?: TEXT_EMPTY
                        override val isRadio = isRadio
                        override val duration = duration ?: 0L
                    }
                    iTracks.add(iTrack)
                }
            }
        }
        cursor?.close()

        return iTracks
    }

    private fun getRadioStreamUrlByIdOrNull(id: Long): Uri =
        ContentUris
            .withAppendedId(MediaStore.Files.getContentUri("external"), id)

    fun parsePlaylistForStreamUrl(playlistUri: Uri, cr: ContentResolver): String? {

        val mimeType = cr.getType(playlistUri)
        val inputStream = cr.openInputStream(playlistUri)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String?

        try {
            while (reader.readLine().also { line = it } != null) {
                val trimmedLine = line?.trim() ?: continue
                val streamUrl = when (mimeType) {
                    MimeType.M3U.type -> parseM3uLine(trimmedLine)
                    MimeType.PLS.type -> parsePlsLine(trimmedLine)
                    else -> null
                }

                if (streamUrl != null) return streamUrl

            }
        } finally {
            inputStream?.close()
            reader.close()
        }

        return null
    }

    private fun parsePlsLine(line: String): String? {
        if (line.startsWith("File", ignoreCase = true)) {
            val parts = line.split('=', limit = 2)
            if (parts.size == 2) {
                val url = parts[1].trim()
                if (url.startsWith("http")) return url
            }
        }
        return null
    }

    private fun parseM3uLine(line: String): String? = if (line.startsWith("http")) line else null

    fun uriToMediaItem(uri: Uri): MediaItem = MediaItem.fromUri(uri)

    fun radioUriToMediaItem(uri: Uri, cr: ContentResolver): MediaItem? {
        val url = parsePlaylistForStreamUrl(uri, cr) ?: return null
        return MediaItem.fromUri(url)
    }
}