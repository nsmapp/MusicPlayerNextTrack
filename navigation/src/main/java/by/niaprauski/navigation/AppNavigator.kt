package by.niaprauski.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import by.niaprauski.library.LibraryScreen
import by.niaprauski.navigation.routs.library.LibraryDest
import by.niaprauski.navigation.routs.player.PlayerDest
import by.niaprauski.navigation.routs.player.PlayerRouterImpl
import by.niaprauski.navigation.routs.settings.SettingsDest
import by.niaprauski.player.PlayerScreen
import by.niaprauski.settings.SettingsScreen

@Composable
fun AppNavigator(
    startUriTrack: Uri? = null,
) {

    val backStack: NavBackStack<NavKey> = rememberNavBackStack(PlayerDest)
    val playerRouter = remember { PlayerRouterImpl(backStack) }


    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry(PlayerDest) { PlayerScreen(startUriTrack, playerRouter) }
            entry(LibraryDest) { LibraryScreen() }
            entry(SettingsDest) { SettingsScreen() }
        }
    )
}