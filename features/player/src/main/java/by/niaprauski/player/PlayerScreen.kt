package by.niaprauski.player

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import by.niaprauski.player.contracts.PlayerRouter
import by.niaprauski.player.models.PlayerEvent
import by.niaprauski.player.views.PlayerControlView
import by.niaprauski.player.views.TrackProgressSlider
import by.niaprauski.playerservice.PlayerService
import by.niaprauski.playerservice.PlayerServiceConnection
import by.niaprauski.playerservice.models.TrackProgress
import by.niaprauski.utils.constants.TEXT_EMPTY
import by.niaprauski.utils.handlers.MediaHandler
import by.niaprauski.utils.permission.MediaPermissions

@Composable
fun PlayerScreen(
    router: PlayerRouter,
    viewModel: PlayerViewModel = hiltViewModel(),
) {

    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

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

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                PlayerEvent.OpenSettings -> router.openSettings()
                PlayerEvent.OpenLibrary -> router.openLibrary()
                PlayerEvent.Play -> {
                    playerService?.play()
                }

                PlayerEvent.PlayNext -> playerService?.seekToNext()
                PlayerEvent.PlayPrevious -> playerService?.seekToPrevious()
                PlayerEvent.Stop -> playerService?.stop()
                PlayerEvent.Pause -> playerService?.pause()
                is PlayerEvent.SetPlayList -> {
                    if (playerService?.isPlaying() == false) playerService?.setPlayList(event.mediaItems)
                }

                PlayerEvent.ChangeRepeatMode -> playerService?.changeRepeatMode()
                PlayerEvent.ChangeShuffleMode -> playerService?.changeShuffleMode()

                PlayerEvent.Nothing -> {
                    //do nothing
                }

            }
        }
    }


    LaunchedEffect(Unit) {
        viewModel.onCreate()
    }

    LaunchedEffect(Unit) {
        val intent = Intent(context, PlayerService::class.java)
        context.startService(intent)
    }


    val hasMediaPermission by MediaPermissions.rememberMediaPermissions()
    val mediaPermissionLauncher = MediaPermissions.rememberMediaPermissionsLauncher(
        onGranted = {
            val tracks = MediaHandler.getTrackData(context.contentResolver)
            viewModel.syncTracks(tracks)
        },
        onDisablePermissions = {
            //TODO handle information add notice
            println("!!! media permission don't granted")
        }
    )

    LaunchedEffect(Unit) {
        if (!hasMediaPermission) {
            mediaPermissionLauncher.launch(MediaPermissions.permission)
        } else {
            val tracks = MediaHandler.getTrackData(context.contentResolver)
            viewModel.syncTracks(tracks)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Column {
                Text(text = title?.value ?: TEXT_EMPTY)
                Text(text = artist?.value ?: TEXT_EMPTY)
            }


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


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    modifier = Modifier.clickable { viewModel.openLibrary() },
                    text = "open Library"
                )
                Text(
                    modifier = Modifier.clickable { viewModel.openSettings() },
                    text = "open Settings"
                )
            }

        }

    }

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

