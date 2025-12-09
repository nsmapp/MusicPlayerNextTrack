package by.niaprauski.navigation.routs.player

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import by.niaprauski.navigation.routs.library.LibraryDest
import by.niaprauski.navigation.routs.settings.SettingsDest
import by.niaprauski.player.contracts.PlayerRouter


class PlayerRouterImpl(private val backStack: NavBackStack<NavKey>) : PlayerRouter {


    override fun openSettings() {
        backStack.add(SettingsDest)
    }

    override fun openLibrary() {
        backStack.add(LibraryDest)
    }

    override fun openAppSettings(context: Context) {
        val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.setData(uri)
        context.startActivity(intent)
    }
}