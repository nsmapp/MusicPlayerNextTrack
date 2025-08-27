package by.niaprauski.navigation


import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.fragment.DialogFragmentNavigator
import by.niaprauski.library.LibraryScreen
import by.niaprauski.navigation.routs.library.LibraryDest
import by.niaprauski.navigation.routs.player.PlayerDest
import by.niaprauski.navigation.routs.player.PlayerRouterImpl
import by.niaprauski.navigation.routs.settings.SettingsDest
import by.niaprauski.player.PlayerScreen
import by.niaprauski.settings.SettingsScreen


class NavigatorImpl(
    context: Context,
    startUriTrack: Uri? = null,
    fragmentManager: FragmentManager
) : Navigator {

    override val navController: NavHostController by lazy {
        NavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            navigatorProvider.addNavigator(
                DialogFragmentNavigator(
                    context,
                    fragmentManager = fragmentManager
                )
            )
        }
    }

    override val navHost: @Composable () -> Unit = {
        NavHost(navController = navController, graph = getNavGraph())
    }

    override fun navigate(dest: Dest) {
        navController.navigate(dest)
    }

    private val navGraph: NavGraph by lazy {
        navController.createGraph(startDestination = PlayerDest) {
            composable<PlayerDest> {
                PlayerScreen(
                    startUriTrack = startUriTrack,
                    router = PlayerRouterImpl(this@NavigatorImpl)
                )
            }
            composable<LibraryDest> { LibraryScreen() }
            composable<SettingsDest> { SettingsScreen() }
        }
    }

    @Composable
    private fun getNavGraph(): NavGraph {
        return remember(navController) {
            navGraph
        }
    }
}