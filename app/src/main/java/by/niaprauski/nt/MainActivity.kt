package by.niaprauski.nt

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.navigation.AppNavigator
import by.niaprauski.nt.models.ExternalTrack
import by.niaprauski.utils.models.MimeType
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

            AppTheme(
                accentColor = state.accentColor,
                backgroundColor = state.backgroundColor,
            ) {
                if (state.isLoading) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.appColors.transparent))
                } else {
                    AppNavigator(
                        radioTrack = externalTrack.radioTrack,
                        singleAudioTrack = externalTrack.singleAudioTrack,
                    )
                }
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

