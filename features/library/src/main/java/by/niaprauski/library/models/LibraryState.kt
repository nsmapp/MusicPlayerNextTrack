package by.niaprauski.library.models

import by.niaprauski.domain.models.Track
import by.niaprauski.utils.constants.TEXT_EMPTY

data class LibraryState(
    val tracks: List<Track>,
    val searchText: String,
){
    companion object{
        val INITIAL = LibraryState(
            tracks = emptyList(),
            searchText = TEXT_EMPTY
        )
    }
}
