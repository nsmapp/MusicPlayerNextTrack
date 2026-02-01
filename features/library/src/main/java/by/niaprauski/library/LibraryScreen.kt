package by.niaprauski.library

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.ui.texxtfield.CTextField
import by.niaprauski.library.models.LibraryEvent
import by.niaprauski.library.view.TrackItem
import by.niaprauski.playerservice.PlayerServiceConnection
import by.niaprauski.translations.R
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

const val TRACK_INDEX_POSITION = 6


@UnstableApi
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    val serviceConnection = rememberPlayerServiceConnection(context)
    val playerService by serviceConnection.service.collectAsStateWithLifecycle(null)

    val listState = rememberLazyListState()
    var isSearchBarVisible by remember { mutableStateOf(true) }


    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { currentOffset -> currentOffset <= TRACK_INDEX_POSITION }
            .distinctUntilChanged()
            .collect {
                isSearchBarVisible = it
            }

    }

    LaunchedEffect(Unit) {
        viewModel.onCreate()
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is LibraryEvent.PlayMediaItem -> playerService?.playWithPosition(event.mediaItem)
                is LibraryEvent.IgnoreMediaItem -> playerService?.removeMediaItem(event.mediaItem)
                is LibraryEvent.AddMediaItem -> playerService?.addItemToPlayList(event.mediaItem)
            }
        }
    }


    Box(
        modifier = Modifier
            .background(color = AppTheme.appColors.background)
            .navigationBarsPadding()
            .statusBarsPadding()
            .padding(AppTheme.padding.mini)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = listState,
                content = {
                    itemsIndexed(
                        items = state.tracks, key = { _, item -> item.id }) { _, item ->

                        val track by remember(item.id, item.isIgnore) { mutableStateOf(item) }

                        TrackItem(
                            track = track,
                            onPlayClick = { track -> viewModel.playTrack(track) },
                            onIgnoreClick = { track -> viewModel.ignoreTrack(track) },
                            onRestoreTrackClick = { track -> viewModel.onRestoreTrackClick(track) }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = AppTheme.padding.medium),
                            thickness = AppTheme.viewSize.border_small,
                            color = AppTheme.appColors.background_hard
                        )
                    }
                }
            )

            AnimatedContent(
                targetState = isSearchBarVisible,
                transitionSpec = { searchRowTransform() },
                content = { isVisible ->
                    if (isVisible) {
                        CTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.searchText,
                            onValueChange = { viewModel.searchTrack(it) },
                            hint = stringResource(R.string.feature_library_search),
                            leadingIcon = Icons.Outlined.Search,
                        )
                    }
                }
            )
        }
    }
}


private fun searchRowTransform(): ContentTransform {
    val enter = slideInVertically(
        initialOffsetY = { -it },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
    )
    val exit = slideOutVertically(
        targetOffsetY = { it },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
    )

    return enter togetherWith exit
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