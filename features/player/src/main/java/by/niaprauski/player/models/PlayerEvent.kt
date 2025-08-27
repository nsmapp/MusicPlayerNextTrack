package by.niaprauski.player.models

import android.net.Uri
import androidx.media3.common.MediaItem

sealed class PlayerEvent {

    object OpenSettings: PlayerEvent()
    object OpenLibrary: PlayerEvent()

    object Play: PlayerEvent()
    object PlayNext: PlayerEvent()
    object PlayPrevious: PlayerEvent()
    object Stop: PlayerEvent()
    object Pause: PlayerEvent()

    object ChangeShuffleMode: PlayerEvent()
    object ChangeRepeatMode: PlayerEvent()


    data class SetPlayList(val mediaItems: List<MediaItem>): PlayerEvent()
    data class PlaySingleTrack(val uri: Uri): PlayerEvent()

    object Nothing: PlayerEvent()
}