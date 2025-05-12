package by.niaprauski.library.models

import by.niaprauski.domain.models.Track

data class LibraryState(
    val tracks: List<Track>
){
    companion object{
        val INITIAL = LibraryState(
            tracks = emptyList()
        )
    }
}
