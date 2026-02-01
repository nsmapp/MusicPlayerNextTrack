package by.niaprauski.navigation

import android.net.Uri
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
    radioTrack: Uri? = null,
    singleAudioTrack: Uri? = null,
) {

    val backStack: NavBackStack<NavKey> = rememberNavBackStack(PlayerDest)
    val playerRouter = remember { PlayerRouterImpl(backStack) }




    NavDisplay(
        backStack = backStack,
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(350)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(350)
            )
        },
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(350)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(350)
            )
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(150)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(150)
            )
        },
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry(PlayerDest) {
                PlayerScreen(
                    radioTrack = radioTrack,
                    singleAudioTrack = singleAudioTrack,
                    router = playerRouter
                )
            }
            entry(LibraryDest) { LibraryScreen() }
            entry(SettingsDest) { SettingsScreen() }
        }
    )
}