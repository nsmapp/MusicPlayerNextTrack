package by.niaprauski.player.models

import androidx.media3.common.MediaItem

sealed class PlayerEvent {

    object OpenSettings: PlayerEvent()
    object OpenLibrary: PlayerEvent()

    object Play: PlayerEvent()
    object PlayNext: PlayerEvent()
    object Stop: PlayerEvent()
    object Pause: PlayerEvent()


    data class SetPlayList(val mediaItems: List<MediaItem>): PlayerEvent()

    object Nothing: PlayerEvent()
}