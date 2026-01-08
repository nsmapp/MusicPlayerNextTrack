package by.niaprauski.library.models

import by.niaprauski.domain.models.Track
import by.niaprauski.utils.constants.TEXT_EMPTY
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class LibraryState(
    val tracks: ImmutableList<Track>,
    val searchText: String,
){
    companion object{
        val INITIAL = LibraryState(
            tracks = persistentListOf(),
            searchText = TEXT_EMPTY
        )
    }
}
