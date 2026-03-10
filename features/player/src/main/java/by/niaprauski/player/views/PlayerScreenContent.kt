package by.niaprauski.player.views

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.media3.common.util.UnstableApi
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.player.models.PAction
import by.niaprauski.playerservice.models.ExoPlayerState
import by.niaprauski.playerservice.models.TrackProgress
import kotlinx.coroutines.flow.StateFlow

@OptIn(UnstableApi::class)
@Composable
fun PlayersScreenContent(
    exoPlayerState: ExoPlayerState,
    isVisuallyEnabled: Boolean,
    trackProgress: StateFlow<TrackProgress>?,
    waveformFlow: StateFlow<FloatArray>?,
    isSyncing: Boolean,
    onAction: (PAction) -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.appColors.background)
            .statusBarsPadding()
            .pointerInput(exoPlayerState.id) {
                detectTapGestures(onDoubleTap = {
                    onAction(PAction.UpTrackFavorite(exoPlayerState.id))
                })
            },
        contentAlignment = Alignment.BottomCenter
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppTheme.padding.default),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            PlayerUpView(
                onAction = onAction,
                isSyncing = isSyncing,
            )

            TrackInfoView(
                trackId = exoPlayerState.id,
                artist = exoPlayerState.artist,
                title = exoPlayerState.title,
                favorite = exoPlayerState.favorite,
                onAction = onAction,
            )

            PlayerControlView(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(vertical = AppTheme.padding.large),
                onAction = onAction,
                isPlaying = exoPlayerState.isPlaying,
                shuffle = exoPlayerState.shuffle,
                repeatMode = exoPlayerState.repeatMode,
            ) {
                TrackProgressSlider(
                    trackProgress = trackProgress,
                    onAction = onAction
                )
            }

        }

        if (isVisuallyEnabled) {
            WaveBarView(
                modifier = Modifier
                    .padding(horizontal = AppTheme.padding.default)
                    .fillMaxWidth()
                    .height(AppTheme.padding.large),
                waveformFlow = waveformFlow,
                isPlaying = exoPlayerState.isPlaying
            )
        }
    }
}