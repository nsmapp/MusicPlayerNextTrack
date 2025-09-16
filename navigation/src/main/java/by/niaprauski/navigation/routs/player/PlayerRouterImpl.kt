package by.niaprauski.navigation.routs.player

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
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

    override fun openAppSettings(context: Context) {
        val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.setData(uri)
        context.startActivity(intent)
    }
}