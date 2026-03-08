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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.media3.common.util.UnstableApi
import by.niaprauski.designsystem.theme.AppTheme
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
    onSyncPlayListClick: () -> Unit,
    onOpenPlayListClick: () -> Unit,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onShuffleModeClick: () -> Unit,
    onRepeatModeClick: () -> Unit,
    onReloadPlayListClick: () -> Unit,
    onFavoriteUp: (trackId: String) -> Unit,
    onChangeTrackFavorite: (trackId: String) -> Unit,
    onSeek: (Float) -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.appColors.background)
            .statusBarsPadding()
            .pointerInput(exoPlayerState.id) {
                detectTapGestures(onDoubleTap = {
                    onFavoriteUp(exoPlayerState.id)
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
                onSyncPlayListClick = onSyncPlayListClick,
                onOpenPlayListClick = onOpenPlayListClick,
                onReloadPlayListClick = onReloadPlayListClick,
                isSyncing = isSyncing,
            )

            TrackInfoView(
                trackId = exoPlayerState.id,
                exoPlayerState.artist,
                exoPlayerState.title,
                exoPlayerState.favorite,
                onChangeTrackFavorite = onChangeTrackFavorite,
            )

            PlayerControlView(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(vertical = AppTheme.padding.large),
                onPlayClick = onPlayClick,
                onPauseClick = onPauseClick,
                onStopClick = onStopClick,
                onNextClick = onNextClick,
                onPreviousClick = onPreviousClick,
                onShuffleModeClick = onShuffleModeClick,
                onRepeatModeClick = onRepeatModeClick,
                isPlaying = exoPlayerState.isPlaying,
                shuffle = exoPlayerState.shuffle,
                repeatMode = exoPlayerState.repeatMode,
            ) {
                TrackProgressSlider(
                    trackProgress = trackProgress,
                    onSeek = onSeek
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