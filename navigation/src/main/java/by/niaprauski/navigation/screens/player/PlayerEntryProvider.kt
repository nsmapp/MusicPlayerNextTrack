package by.niaprauski.navigation.screens.player

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.net.toUri
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import by.niaprauski.navigation.Navigator
import by.niaprauski.player.PlayerScreen

fun EntryProviderScope<NavKey>.player(navigator: Navigator) {

    entry<PlayerDest.Player> { player ->
        PlayerScreen(
            radioTrack = player.radioTrack?.toUri(),
            singleAudioTrack = player.singleAudioTrack?.toUri(),
            openAppSettings = { context ->
                val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.setData(uri)
                context.startActivity(intent)
            }
        )
    }
}