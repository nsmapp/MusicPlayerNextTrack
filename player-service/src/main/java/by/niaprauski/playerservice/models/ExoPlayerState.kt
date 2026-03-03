package by.niaprauski.playerservice.models

import androidx.media3.common.Player
import by.niaprauski.utils.constants.TEXT_EMPTY
import by.niaprauski.utils.extension.UNKNOWN_TRACK_ID

data class ExoPlayerState(
    val id: Long = 0,
    val fileName: String,
    val title: String,
    val artist: String,
    val shuffle: Boolean,
    val repeatMode: Int,
    val isPlaying: Boolean,
    val favorite: Int,
) {
    companion object {
        val DEFAULT = ExoPlayerState(
            id = UNKNOWN_TRACK_ID,
            fileName = TEXT_EMPTY,
            title = TEXT_EMPTY,
            artist = TEXT_EMPTY,
            shuffle = false,
            repeatMode = Player.REPEAT_MODE_ALL,
            isPlaying = false,
            favorite = 0,
        )
    }
}