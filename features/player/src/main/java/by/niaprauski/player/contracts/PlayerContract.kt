package by.niaprauski.player.contracts

import android.net.Uri
import androidx.media3.common.MediaItem

interface PlayerContract {

    fun play()

    fun pause()

    fun stop()

    fun playNext()

    fun playPrevious()

    fun seekTo(position: Float)

    fun setPlayList(tracks: List<MediaItem>)

    fun changeShuffleMode()

    fun changeRepeatMode()

    fun playSingleAudioTrack(uri: Uri)

    fun playRadioTrack(uri: Uri)

    fun requestSync()

    fun showPermissionInformationDialog()

    fun hideWelcomeDialogs()

    fun hideMediaPermissionInfoDialog()
}