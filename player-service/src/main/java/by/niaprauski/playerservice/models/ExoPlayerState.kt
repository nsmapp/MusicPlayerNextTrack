package by.niaprauski.playerservice.models

import androidx.media3.common.Player
import by.niaprauski.utils.constants.TEXT_EMPTY

data class ExoPlayerState(
    val title: String,
    val artist: String,
    val shuffle: Boolean,
    val repeatMode: Int,
    val isPlaying: Boolean,
) {
    companion object {
        val DEFAULT = ExoPlayerState(
            title = TEXT_EMPTY,
            artist = TEXT_EMPTY,
            shuffle = false,
            repeatMode = Player.REPEAT_MODE_ALL,
            isPlaying = false,
        )
    }
}