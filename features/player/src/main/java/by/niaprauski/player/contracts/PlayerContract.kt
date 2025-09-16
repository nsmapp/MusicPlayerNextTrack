package by.niaprauski.player.contracts

import android.net.Uri
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

    fun playSingleTrack(uri: Uri)

    fun showPermissionInformationDialog()

    fun hideWelcomeDialogs()

    fun hideMediaPermissionInfoDialog()
}