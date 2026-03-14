package by.niaprauski.navigation.screens.about

import android.content.Intent
import androidx.core.net.toUri
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import by.niaprauski.navigation.Navigator
import by.niaprauski.about.AboutScreen

fun EntryProviderScope<NavKey>.about(navigator: Navigator) {

    entry<AboutDest.About> {
        AboutScreen(
            onShowGitHubPage = { context ->
                try {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        "https://github.com/nsmapp/MusicPlayerNextTrack".toUri()
                    )
                    context.startActivity(intent)
                } catch (e: Exception) {

                }
            },
            onSendEmail = { context ->
                try {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = "mailto:nsmappinfo@gmail.com".toUri()
                        putExtra(Intent.EXTRA_SUBJECT, "App feedback")
                    }
                    context.startActivity(intent)

                } catch (e: Exception) {
                }
            }

        )
    }
}