package by.niaprauski.nt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.navigation.NavigatorImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startUriTrack: Uri? = getOutsideStartTrackUri()

        enableEdgeToEdge()
        setContent {

            val state by viewModel.state.collectAsStateWithLifecycle()

            AppTheme(isDarkThemeEnabled = state.isNightMode) {
                val navigator =
                    remember { NavigatorImpl(this, startUriTrack, supportFragmentManager) }
                navigator.navHost()
            }

        }
    }

    private fun getOutsideStartTrackUri(): Uri? = if (Intent.ACTION_VIEW == intent.action
        && intent.type != null
        && intent.type?.startsWith("audio/") == true
    ) intent.data else null


}

