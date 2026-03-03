package by.niaprauski.library

import by.niaprauski.library.models.TrackModel

interface LibraryContract {

    fun ignoreTrack(track: TrackModel)

    fun onRestoreTrackClick(track: TrackModel)

    fun playTrack(track: TrackModel)

    fun searchTrack(text: String)

    fun play(value: Unit)

    fun pause(value: Unit)
}