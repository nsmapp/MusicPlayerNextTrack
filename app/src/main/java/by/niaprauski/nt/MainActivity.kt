package by.niaprauski.nt

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentActivity
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.navigation.NavigatorImpl
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val startUriTrack = if (Intent.ACTION_VIEW == intent.action
            && intent.type != null
            && intent.type?.startsWith("audio/") == true
        ) intent.data else null

        enableEdgeToEdge()

        setContent {

            AppTheme(isDarkThemeEnabled = false) {
                val navigator =
                    remember { NavigatorImpl(this, startUriTrack, supportFragmentManager) }
                navigator.navHost()
            }

        }
    }


}

