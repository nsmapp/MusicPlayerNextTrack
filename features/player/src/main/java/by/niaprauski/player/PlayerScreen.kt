package by.niaprauski.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import by.niaprauski.player.models.PlayerEvent

@Composable
fun PlayerScreen(
    router: PlayerRouter,
    viewModel: PlayerViewModel = viewModel(),
) {


    LaunchedEffect(null) {
        viewModel.event.collect { event ->

            when(event){
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



    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(text = "PlayerScreen")
            Text(modifier = Modifier.clickable { viewModel.openLibrary() }, text = "open Library")
            Text(modifier = Modifier.clickable { viewModel.openSettings() }, text = "open Settings")
        }

    }

}