package by.niaprauski.player

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.annotation.OptIn
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.player.contracts.PlayerRouter
import by.niaprauski.player.models.PlayerEvent
import by.niaprauski.player.models.PlayerState
import by.niaprauski.player.models.SyncTrackStatus
import by.niaprauski.player.views.PlayerControlView
import by.niaprauski.player.views.PlayerUpView
import by.niaprauski.player.views.TrackInfoView
import by.niaprauski.player.views.TrackProgressSlider
import by.niaprauski.player.views.WaveBarView
import by.niaprauski.player.views.dialogs.FirstLaunchDialog
import by.niaprauski.player.views.dialogs.NeedMediaPermissionDialog
import by.niaprauski.playerservice.models.ExoPlayerState
import by.niaprauski.playerservice.models.TrackProgress
import by.niaprauski.utils.media.MediaHandler
import by.niaprauski.utils.permission.MediaPermissions
import kotlinx.coroutines.flow.StateFlow

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(
    radioTrack: Uri? = null,
    singleAudioTrack: Uri? = null,
    router: PlayerRouter,
) {
    val viewModel: PlayerViewModel = hiltViewModel { factory: PlayerViewModel.Factory ->
        factory.create(radioTrack, singleAudioTrack)
    }

    val context = LocalContext.current
    val state: PlayerState by viewModel.state.collectAsStateWithLifecycle()
    val exoPlayerState by viewModel.exoPlayerState.collectAsStateWithLifecycle()
    val playerService by viewModel.playerService.collectAsStateWithLifecycle()


    val hasMediaPermission by MediaPermissions.rememberMediaPermissions()

    LaunchedEffect(Unit) { viewModel.onCreate() }

    val mediaPermissionLauncher = MediaPermissions.rememberMediaPermissionsLauncher(
        onGranted = { syncPlaylist(context) { tracks -> viewModel.syncTracks(tracks) } },
        onDisablePermissions = { viewModel.showPermissionInformationDialog() }
    )

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                PlayerEvent.OpenSettings -> router.openSettings()
                PlayerEvent.OpenLibrary -> router.openLibrary()
                PlayerEvent.Play -> playerService?.play()
                PlayerEvent.PlayNext -> playerService?.seekToNext()
                PlayerEvent.PlayPrevious -> playerService?.seekToPrevious()
                PlayerEvent.Stop -> playerService?.stop()
                PlayerEvent.Pause -> playerService?.pause()
                is PlayerEvent.SeekTo -> {
                    playerService?.seekTo(event.position)
                }

                is PlayerEvent.SetPlayList -> playerService?.setTracks(event.tracks)
                is PlayerEvent.PlaySingleTrack -> playerService?.setTrack(event.track)
                PlayerEvent.ChangeRepeatMode -> playerService?.changeRepeatMode()
                PlayerEvent.ChangeShuffleMode -> playerService?.changeShuffleMode()
                PlayerEvent.SyncPlayList -> requestMediaPermissionWithSyncPlaylist(
                    hasMediaPermission = hasMediaPermission,
                    mediaPermissionLauncher = mediaPermissionLauncher,
                    context = context,
                    onSyncTrack = { syncStatus -> viewModel.syncTracks(syncStatus) })

                PlayerEvent.Nothing -> {
                    /**do nothing **/
                }
            }
        }
    }


    if (state.isShowWelcomeDialog) FirstLaunchDialog(
        onSyncClick = { viewModel.requestSync() },
        onDismissClick = { viewModel.hideWelcomeDialogs() },
    )

    if (state.isShowPermissionInformationDialog) NeedMediaPermissionDialog(
        onOpenSettingsClick = { router.openAppSettings(context) },
        onDismissClick = { viewModel.hideMediaPermissionInfoDialog() }
    )

    PlayersScreenContent(
        exoPlayerState = exoPlayerState,
        isVisuallyEnabled = state.isVisuallyEnabled,
        trackProgress = playerService?.trackProgress,
        waveformFlow = playerService?.waveform,
        isSyncing = state.isSyncing,
        onOpenSettingsClick = viewModel::openSettings,
        onSyncPlayListClick = viewModel::requestSync,
        onOpenPlayListClick = viewModel::openLibrary,
        onPlayClick = viewModel::play,
        onPauseClick = viewModel::pause,
        onStopClick = viewModel::stop,
        onNextClick = viewModel::playNext,
        onPreviousClick = viewModel::playPrevious,
        onShuffleModeClick = viewModel::changeShuffleMode,
        onRepeatModeClick = viewModel::changeRepeatMode,
        onSeek = viewModel::seekTo,
    )

}

@OptIn(UnstableApi::class)
@Composable
private fun PlayersScreenContent(
    exoPlayerState: ExoPlayerState,
    isVisuallyEnabled: Boolean,
    trackProgress : StateFlow<TrackProgress>?,
    waveformFlow: StateFlow<FloatArray>?,
    isSyncing: Boolean,
    onOpenSettingsClick: () -> Unit,
    onSyncPlayListClick: () -> Unit,
    onOpenPlayListClick: () -> Unit,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onShuffleModeClick: () -> Unit,
    onRepeatModeClick: () -> Unit,
    onSeek: (Float) -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.appColors.background)
            .navigationBarsPadding()
            .statusBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppTheme.padding.default),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            PlayerUpView(
                onOpenSettingsClick = onOpenSettingsClick,
                onSyncPlayListClick = onSyncPlayListClick,
                onOpenPlayListClick = onOpenPlayListClick,
                isSyncing = isSyncing,
            )

            TrackInfoView(exoPlayerState.artist, exoPlayerState.title)

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

        if (isVisuallyEnabled){
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

private fun requestMediaPermissionWithSyncPlaylist(
    hasMediaPermission: Boolean,
    mediaPermissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    context: Context,
    onSyncTrack: (SyncTrackStatus) -> Unit,
) {
    if (!hasMediaPermission) mediaPermissionLauncher.launch(MediaPermissions.permission)
    else syncPlaylist(context) { tracks -> onSyncTrack(tracks) }
}

private fun syncPlaylist(
    context: Context,
    onSyncTrack: (SyncTrackStatus) -> Unit,
) {
    onSyncTrack(SyncTrackStatus.Started)
    val tracks = MediaHandler.getTrackData(context.contentResolver)
    onSyncTrack(SyncTrackStatus.Finished(tracks))
}
