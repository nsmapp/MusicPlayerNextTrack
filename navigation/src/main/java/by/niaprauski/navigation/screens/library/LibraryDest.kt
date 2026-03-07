package by.niaprauski.navigation.screens.library

import by.niaprauski.navigation.Dest
import kotlinx.serialization.Serializable

@Serializable
sealed class LibraryDest: Dest {

    companion object{
        fun root() = Library
    }

    @Serializable
    object Library: LibraryDest()
}