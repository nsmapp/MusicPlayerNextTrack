package by.niaprauski.navigation.routs.player

import by.niaprauski.navigation.Navigator
import by.niaprauski.navigation.routs.library.LibraryDest
import by.niaprauski.navigation.routs.settings.SettingsDest
import by.niaprauski.player.contracts.PlayerRouter

class PlayerRouterImpl(private val navigator: Navigator) : PlayerRouter {


    override fun openSettings() {
        navigator.navController.navigate(SettingsDest)
    }

    override fun openLibrary() {
        navigator.navController.navigate(LibraryDest)
    }
}