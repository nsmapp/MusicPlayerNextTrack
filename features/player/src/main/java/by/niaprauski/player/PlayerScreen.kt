package by.niaprauski.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.niaprauski.player.models.PlayerEvent
import by.niaprauski.utils.handlers.MediaHandler
import by.niaprauski.utils.permission.MediaPermissions

@Composable
fun PlayerScreen(
    router: PlayerRouter,
    viewModel: PlayerViewModel = hiltViewModel(),
) {

    val context = LocalContext.current

    LaunchedEffect(null) { collectEvents(viewModel, router) }
    LaunchedEffect(null) { viewModel.onCreate() }

    val tracks by viewModel.tracks.collectAsStateWithLifecycle()

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
            Text(text = "PlayerScreen")
            Text(modifier = Modifier.clickable { viewModel.openLibrary() }, text = "open Library")
            Text(modifier = Modifier.clickable { viewModel.openSettings() }, text = "open Settings")
            Text(modifier = Modifier, text = "Tracks count: ${tracks.size}")
        }

    }

}

private suspend fun collectEvents(
    viewModel: PlayerViewModel,
    router: PlayerRouter
) {
    viewModel.event.collect { event ->
        when (event) {
            PlayerEvent.OpenSettings -> {
                router.openSettings()
            }

            PlayerEvent.OpenLibrary -> router.openLibrary()
            else -> {
                //do nothing
            }
        }
    }
}