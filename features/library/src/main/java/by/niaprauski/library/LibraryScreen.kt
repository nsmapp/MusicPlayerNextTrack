package by.niaprauski.library

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.domain.models.Track
import by.niaprauski.library.models.LibraryEvent
import by.niaprauski.library.view.TrackListWithSearchView
import by.niaprauski.playerservice.PlayerServiceConnection
import kotlinx.coroutines.flow.distinctUntilChanged

const val TRACK_INDEX_POSITION = 6


@UnstableApi
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pagingTracks: LazyPagingItems<Track> = viewModel.pagingTracks.collectAsLazyPagingItems()


    val serviceConnection = rememberPlayerServiceConnection(context)
    val playerService by serviceConnection.service.collectAsStateWithLifecycle(null)

    val listState = rememberLazyListState()
    var isSearchBarVisible by remember { mutableStateOf(true) }


    LaunchedEffect(listState, pagingTracks.loadState) {
        snapshotFlow {
            val isScrolledPastThreshold = listState.firstVisibleItemIndex > TRACK_INDEX_POSITION
            val isAppending = pagingTracks.loadState.append is LoadState.Loading

            isScrolledPastThreshold.not() || isAppending

        }.distinctUntilChanged()
            .collect { isShowing ->
                isSearchBarVisible = isShowing
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


        AnimatedContent(
            targetState =pagingTracks.loadState.refresh is LoadState.Loading && pagingTracks.itemCount == 0,
            transitionSpec = { loadingTransform() }
        ) { isLoading ->
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(AppTheme.viewSize.view_extra_larger),
                    color = AppTheme.appColors.background_hard,
                    trackColor = AppTheme.appColors.accent,
                    strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap
                )
            } else {
                TrackListWithSearchView(
                    listState = listState,
                    pagingTracks = pagingTracks,
                    isSearchBarVisible = isSearchBarVisible,
                    state = state,
                    onPlayClick = viewModel::playTrack,
                    onIgnoreClick = viewModel::ignoreTrack,
                    onRestoreTrackClick = viewModel::onRestoreTrackClick,
                    onSearchTrack = viewModel::searchTrack
                )
            }
        }
    }
}

private fun loadingTransform(): ContentTransform {
    val enter = fadeIn(animationSpec = tween(300, delayMillis = 90)) +
            scaleIn(initialScale = 0.7f, animationSpec = tween(300, delayMillis = 90))
    val exit = fadeOut(animationSpec = tween(90))

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