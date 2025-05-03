package by.niaprauski.player

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import by.niaprauski.player.contracts.PlayerRouter
import by.niaprauski.player.models.PlayerEvent
import by.niaprauski.utils.constants.TEXT_EMPTY
import by.niaprauski.utils.constants.TRACK_START_POSITION
import by.niaprauski.utils.handlers.MediaHandler
import by.niaprauski.utils.permission.MediaPermissions

@Composable
fun PlayerScreen(
    router: PlayerRouter,
    viewModel: PlayerViewModel = hiltViewModel(),
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val state by viewModel.state.collectAsStateWithLifecycle()

    val title = remember { mutableStateOf(TEXT_EMPTY) }
    val artist = remember { mutableStateOf(TEXT_EMPTY) }


    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .build()

            setAudioAttributes(audioAttributes, true)
            addListener(object : Player.Listener {

                override fun onPlayerErrorChanged(error: PlaybackException?) {
                    super.onPlayerErrorChanged(error)
                    when(error?.errorCode){
                        ERROR_CODE_IO_FILE_NOT_FOUND -> {
                            seekToNextMediaItem()
                            play()
                        }
                        //TODO handle other errors
                        else -> println("!!! player error: ${error?.message}  code ${error?.errorCode}")
                    }
                }
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    title.value = mediaMetadata.title?.toString() ?: TEXT_EMPTY
                    artist.value = mediaMetadata.artist?.toString() ?: TEXT_EMPTY
                }
            })

        }
    }
    val mediaSession = remember { MediaSession.Builder(context, player).build() }

    LaunchedEffect(null) {
        viewModel.event.collect { event ->
            when (event) {
                PlayerEvent.OpenSettings -> router.openSettings()

                PlayerEvent.OpenLibrary -> router.openLibrary()
                PlayerEvent.Play -> player.play()
                PlayerEvent.PlayNext -> player.seekToNextMediaItem()
                PlayerEvent.Stop -> handlePlayerStop(player)
                PlayerEvent.Pause -> player.pause()
                is PlayerEvent.SetPlayList -> handlePlayList(player, event)

                else -> {
                    //do nothing
                }
            }
        }
    }

    LaunchedEffect(null) { viewModel.onCreate() }

    DisposableEffect(null) {
        mediaSession.setPlayer(player)
        onDispose {
            player.release()
            mediaSession.release()
        }
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
        Column {

            Text(text = title.value)
            Text(text = artist.value)
            Text(text = "Track count: ${state.trackCount}")

            Text(text = "")
            Text(text = "PlayerScreen")
            Text(modifier = Modifier.clickable { viewModel.openLibrary() }, text = "open Library")
            Text(modifier = Modifier.clickable { viewModel.openSettings() }, text = "open Settings")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Play", modifier = Modifier.clickable { viewModel.play() })
                Text(text = "Pause", modifier = Modifier.clickable { viewModel.pause() })
                Text(text = "Stop", modifier = Modifier.clickable { viewModel.stop() })
                Text(text = "Next track", modifier = Modifier.clickable { viewModel.playNext() })
            }
        }

    }

}

private fun handlePlayerStop(player: ExoPlayer) {
    player.seekTo(TRACK_START_POSITION)
    player.stop()
}

private fun handlePlayList(
    player: ExoPlayer,
    event: PlayerEvent.SetPlayList
) {
    player.apply {
        clearMediaItems()
        addMediaItems(0, event.mediaItems)
        prepare()
    }
}
