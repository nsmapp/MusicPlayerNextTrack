package by.niaprauski.library

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import by.niaprauski.library.models.LibraryEvent
import by.niaprauski.library.ui.TrackItem
import by.niaprauski.playerservice.PlayerServiceConnection



@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    val serviceConnection = rememberPlayerServiceConnection(context)
    val playerService by serviceConnection.service.collectAsStateWithLifecycle(null)

    LaunchedEffect(Unit) {
        viewModel.onCreate()
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when(event){
                is LibraryEvent.PlayMediaItem -> playerService?.playWithPosition(event.mediaItem)
                is LibraryEvent.IgnoreMediaItem -> playerService?.removeMediaItem(event.mediaItem)
            }
        }
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "LibraryScreen")

            LazyColumn(
                modifier = Modifier.fillMaxSize(), state = rememberLazyListState(), content = {
                    itemsIndexed(
                        items = state.tracks, key = { _, item -> item.id }) { _, item ->

                        TrackItem(
                            track = item,
                            onPlayClick = { track -> viewModel.playTrack(track) },
                            onIgnoreClick = { track -> viewModel.ignoreTrack(track)},
                        )
                    }
                }
            )
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