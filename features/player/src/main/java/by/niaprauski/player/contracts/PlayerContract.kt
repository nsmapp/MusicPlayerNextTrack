package by.niaprauski.player.contracts

import androidx.media3.common.MediaItem

interface PlayerContract {

    fun play()

    fun pause()

    fun stop()

    fun playNext()

    fun playPrevious()

    fun setPlayList(mediaItems: List<MediaItem> )

    fun changeShuffleMode()

    fun changeRepeatMode()
}