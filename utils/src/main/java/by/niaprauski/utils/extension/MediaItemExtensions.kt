package by.niaprauski.utils.extension

import androidx.media3.common.MediaMetadata
import by.niaprauski.utils.constants.TEXT_EMPTY
import by.niaprauski.utils.models.TRACK_KEY_FILE_NAME
import by.niaprauski.utils.models.TRACK_KEY_ID

fun MediaMetadata.getTrackId(): String = this.extras?.getString(TRACK_KEY_ID) ?: UNKNOWN_TRACK_ID

fun MediaMetadata.getFileName(default: String = TEXT_EMPTY): String =
    this.extras?.getString(TRACK_KEY_FILE_NAME) ?: default