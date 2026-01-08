package by.niaprauski.playerservice.utils

import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer


fun ExoPlayer?.getMediaItemIndex(item: MediaItem): Int {
    val player = this ?: return -1
    for (i in 0 until player.mediaItemCount) {
        if (player.getMediaItemAt(i).mediaId == item.mediaId) {
            return i
        }
    }
    return -1
}
