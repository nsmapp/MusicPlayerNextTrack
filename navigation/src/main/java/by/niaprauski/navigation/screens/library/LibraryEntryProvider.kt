package by.niaprauski.navigation.screens.library

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import by.niaprauski.library.LibraryScreen
import by.niaprauski.navigation.Navigator

fun EntryProviderScope<NavKey>.library(navigator: Navigator) {

    entry<LibraryDest.Library> {
        LibraryScreen()
    }
}