package by.niaprauski.nt

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.navigation.AppNavigator
import by.niaprauski.nt.models.ExternalTrack
import by.niaprauski.utils.handlers.MimeType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val externalTrack: ExternalTrack = getOutsideStartTrackUri()

        enableEdgeToEdge()
        setContent {

            val state by viewModel.state.collectAsStateWithLifecycle()

            AppTheme(isDarkThemeEnabled = state.isNightMode) {
                AppNavigator(
                    radioTrack = externalTrack.radioTrack,
                    singleAudioTrack = externalTrack.singleAudioTrack,
                )
            }

        }
    }

    private fun getOutsideStartTrackUri(): ExternalTrack {
        val type = intent?.type
        val action = intent?.action
        val data = intent?.data

        if (type == null || action == null || data == null) return ExternalTrack()

        val result = when(type) {
            MimeType.M3U.type, MimeType.PLS.type -> ExternalTrack(radioTrack = data)
            MimeType.OGG.type, MimeType.MPEG.type -> ExternalTrack(singleAudioTrack = data)
            else -> ExternalTrack()
        }

        return result
    }


}

