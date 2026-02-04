package by.niaprauski.library.models

import by.niaprauski.utils.constants.TEXT_EMPTY

data class LibraryState(
    val searchText: String,
){
    companion object{
        val INITIAL = LibraryState(
            searchText = TEXT_EMPTY
        )
    }
}
