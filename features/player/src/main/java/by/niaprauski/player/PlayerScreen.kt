package by.niaprauski.player

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import by.niaprauski.playerservice.PlayerService
import by.niaprauski.playerservice.PlayerServiceConnection
import by.niaprauski.playerservice.models.TrackProgress
import by.niaprauski.utils.constants.TEXT_EMPTY
import by.niaprauski.utils.handlers.MediaHandler
import by.niaprauski.utils.permission.MediaPermissions

@Composable
fun PlayerScreen(
    startUriTrack: Uri? = null,
    router: PlayerRouter,
    viewModel: PlayerViewModel = hiltViewModel(),
) {

    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    var singleTrack by remember { mutableStateOf(startUriTrack) }

    val serviceConnection = rememberPlayerServiceConnection(context)
    val playerService by serviceConnection.service.collectAsStateWithLifecycle(null)


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

    LaunchedEffect(Unit) { startPlayerService(context) }

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
                is PlayerEvent.SetPlayList -> playerService?.setPlayList(event.mediaItems)
                is PlayerEvent.PlaySingleTrack -> playerService?.setPlayList(event.uri)
                PlayerEvent.ChangeRepeatMode -> playerService?.changeRepeatMode()
                PlayerEvent.ChangeShuffleMode -> playerService?.changeShuffleMode()

                PlayerEvent.Nothing -> {
                    //do nothing
                }

            }
        }
    }

    val hasMediaPermission by MediaPermissions.rememberMediaPermissions()

    val mediaPermissionLauncher = MediaPermissions.rememberMediaPermissionsLauncher(
        onGranted = {syncPlaylist(context, viewModel) },
        onDisablePermissions = {
            //TODO handle information add notice
            println("!!! media permission don't granted")
        }
    )

    LaunchedEffect(Unit) {
        when {
            startUriTrack != null -> viewModel.playSingleTrack(startUriTrack)
            !hasMediaPermission -> mediaPermissionLauncher.launch(MediaPermissions.permission)
            else -> syncPlaylist(context, viewModel)
        }
    }

    Column(
        modifier = Modifier
            .background(color = AppTheme.colors.background)
            .navigationBarsPadding()
            .statusBarsPadding()
            .fillMaxSize()
            .padding(horizontal = AppTheme.padding.default),
        verticalArrangement = Arrangement.SpaceBetween
    ) {


        PlayerUpView(
            onOpenSettingsClick = { viewModel.openSettings() },
            onOpenPlayListClick = { viewModel.openLibrary() },
        )

        TrackInfoView(artist, title)

        PlayerControlView(
            onPlayClick = { viewModel.play() },
            onPauseClick = { viewModel.pause() },
            onStopClick = { viewModel.stop() },
            onNextClick = { viewModel.playNext() },
            onPreviousClick = { viewModel.playPrevious() },
            onShuffleModeClick = { viewModel.changeShuffleMode() },
            onRepeatModeClick = { viewModel.changeRepeatMode() },
            isPlaying = isPlaying,
            shuffle = shuffle,
            repeatMode = repeatMode,
        ) {
            TrackProgressSlider(trackProgress, playerService)
        }
    }

}

private fun startPlayerService(context: Context) {
    val intent = Intent(context, PlayerService::class.java)
    context.startService(intent)
}

//TODO need fix relaunch after return to screen
private fun syncPlaylist(
    context: Context,
    viewModel: PlayerViewModel
) {
    val tracks = MediaHandler.getTrackData(context.contentResolver)
    viewModel.syncTracks(tracks)
}


@Composable
fun rememberPlayerServiceConnection(context: Context): PlayerServiceConnection {
    val connection = remember { PlayerServiceConnection(context) }

    DisposableEffect(context) {
        connection.bind()
        onDispose {
            connection.unbind()
        }
    }

    return connection
}
