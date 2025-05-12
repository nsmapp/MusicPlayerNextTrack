package by.niaprauski.library

import by.niaprauski.domain.models.Track

interface LibraryContract {

    fun ignoreTrack(track: Track)

    fun playTrack(track: Track)
}