package by.niaprauski.library

import by.niaprauski.domain.models.SearchTrackFilter
import by.niaprauski.domain.models.Track

interface LibraryContract {

    fun getTracksFlow(filter: SearchTrackFilter )

    fun ignoreTrack(track: Track)

    fun playTrack(track: Track)

    fun searchTrack(text: String)
}