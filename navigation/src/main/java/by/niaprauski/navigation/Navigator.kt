package by.niaprauski.navigation

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

class Navigator(
    val backStack: NavBackStack<NavKey>
){
    val currentScreen: NavKey? by derivedStateOf { backStack.last() }

    fun navigate(screen: NavKey){
        if(screen == currentScreen) return
        backStack.add(screen)
    }

    fun navigateSingleTop(screen: NavKey){
        backStack.apply {
            clear()
            add(screen)
        }
    }

    fun navigateBack(){
        backStack.removeLastOrNull()
    }
}