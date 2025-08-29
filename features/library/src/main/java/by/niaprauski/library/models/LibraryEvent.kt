package by.niaprauski.library.models

import androidx.media3.common.MediaItem

sealed class LibraryEvent {

    data class PlayMediaItem(val mediaItem: MediaItem): LibraryEvent()
    data class IgnoreMediaItem(val mediaItem: MediaItem): LibraryEvent()
    data class AddMediaItem(val mediaItem: MediaItem): LibraryEvent()

}