package by.niaprauski.library

import by.niaprauski.library.models.TrackModel

interface LibraryContract {

    fun ignoreTrack(track: TrackModel)

    fun onRestoreTrackClick(track: TrackModel)

    fun playTrack(track: TrackModel)

    fun searchTrack(text: String)
}