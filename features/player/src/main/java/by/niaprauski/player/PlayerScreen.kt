package by.niaprauski.player

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.player.contracts.PlayerRouter
import by.niaprauski.player.models.PlayerEvent
import by.niaprauski.player.views.PlayerControlView
import by.niaprauski.player.views.PlayerUpView
import by.niaprauski.player.views.TrackInfoView
import by.niaprauski.player.views.TrackProgressSlider
import by.niaprauski.player.views.dialogs.FirstLaunchDialog
import by.niaprauski.player.views.dialogs.NeedMediaPermissionDialog
import by.niaprauski.playerservice.models.TrackProgress
import by.niaprauski.utils.constants.TEXT_EMPTY
import by.niaprauski.utils.handlers.MediaHandler
import by.niaprauski.utils.models.ITrack
import by.niaprauski.utils.permission.MediaPermissions

@Composable
fun PlayerScreen(
    startUriTrack: Uri? = null,
    router: PlayerRouter,
    viewModel: PlayerViewModel = hiltViewModel(),
) {

    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    val playerService by viewModel.playerService.collectAsStateWithLifecycle()

    val title = playerService?.currentTitle?.collectAsStateWithLifecycle(initialValue = TEXT_EMPTY)
    val artist = playerService?.currentArtist
        ?.collectAsStateWithLifecycle(initialValue = TEXT_EMPTY)
    val trackProgress = playerService?.trackProgress?.collectAsStateWithLifecycle(
        initialValue = TrackProgress.DEFAULT
    )

    val isPlaying = playerService?.isPlaying?.collectAsStateWithLifecycle(
        initialValue = false
    )

    val shuffle = playerService?.shuffle?.collectAsStateWithLifecycle(false)
    val repeatMode = playerService?.repeatMode?.collectAsStateWithLifecycle(Player.REPEAT_MODE_ALL)

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
                is PlayerEvent.SeekTo -> { playerService?.seekTo(event.position) }
                is PlayerEvent.SetPlayList -> playerService?.setPlayList(event.mediaItems)
                is PlayerEvent.PlaySingleTrack -> playerService?.setPlayList(event.uri)
                PlayerEvent.ChangeRepeatMode -> playerService?.changeRepeatMode()
                PlayerEvent.ChangeShuffleMode -> playerService?.changeShuffleMode()
                PlayerEvent.SyncPlayList -> requestMediaPermissionWithSyncPlaylist(
                    hasMediaPermission = hasMediaPermission,
                    mediaPermissionLauncher = mediaPermissionLauncher,
                    context = context,
                    onSyncTrack = { tracks -> viewModel.syncTracks(tracks) })

                PlayerEvent.Nothing ->  {
                    /**do nothing **/
                }
            }
        }
    }

    LaunchedEffect(playerService, startUriTrack) {
        if (playerService != null && startUriTrack != null) {
            viewModel.playSingleTrack(startUriTrack)
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
        artist = artist,
        title = title,
        isPlaying = isPlaying,
        shuffle = shuffle,
        repeatMode = repeatMode,
        trackProgress = trackProgress,
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
        onSeek = viewModel::seekTo
    )

}

@Composable
private fun PlayersScreenContent(
    artist: State<String>?,
    title: State<String>?,
    isPlaying: State<Boolean>?,
    shuffle: State<Boolean>?,
    repeatMode: State<Int>?,
    trackProgress: State<TrackProgress>?,
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


    Column(
        modifier = Modifier
            .background(color = AppTheme.appColors.background)
            .navigationBarsPadding()
            .statusBarsPadding()
            .fillMaxSize()
            .padding(horizontal = AppTheme.padding.default),
        verticalArrangement = Arrangement.SpaceBetween
    ) {


        PlayerUpView(
            onOpenSettingsClick = onOpenSettingsClick,
            onSyncPlayListClick = onSyncPlayListClick,
            onOpenPlayListClick = onOpenPlayListClick,
        )

        TrackInfoView(artist, title)

        PlayerControlView(
            onPlayClick = onPlayClick,
            onPauseClick = onPauseClick,
            onStopClick = onStopClick,
            onNextClick = onNextClick,
            onPreviousClick = onPreviousClick,
            onShuffleModeClick = onShuffleModeClick,
            onRepeatModeClick = onRepeatModeClick,
            isPlaying = isPlaying,
            shuffle = shuffle,
            repeatMode = repeatMode,
        ) {
            TrackProgressSlider(
                trackProgress,
                onSeek = onSeek
            )
        }
    }
}

private fun requestMediaPermissionWithSyncPlaylist(
    hasMediaPermission: Boolean,
    mediaPermissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    context: Context,
    onSyncTrack: (List<ITrack>) -> Unit,
) {
    if (!hasMediaPermission) mediaPermissionLauncher.launch(MediaPermissions.permission)
    else syncPlaylist(context) { tracks -> onSyncTrack(tracks) }
}

private fun syncPlaylist(
    context: Context,
    onSyncTrack: (List<ITrack>) -> Unit,
) {
    val tracks = MediaHandler.getTrackData(context.contentResolver)
    onSyncTrack(tracks)
}
