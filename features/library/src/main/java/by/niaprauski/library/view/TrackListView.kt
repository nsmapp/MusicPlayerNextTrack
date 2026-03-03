package by.niaprauski.library.view

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import by.niaprauski.library.models.TrackModel


@Composable
fun ColumnScope.TrackListView(
    pagingTracks: LazyPagingItems<TrackModel>,
    onPlayClick: (TrackModel) -> Unit,
    onIgnoreClick: (TrackModel) -> Unit,
    onRestoreTrackClick: (TrackModel) -> Unit,
    currentTrackId: () -> Long
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        state = listState,
    ) {
        items(
            count = pagingTracks.itemCount,
            key = pagingTracks.itemKey { it.id },
            contentType = { "track" }
        ) { index ->

            val item: TrackModel? = pagingTracks[index]

            if (item != null) {
                TrackItem(
                    track = item,
                    onPlayClick = onPlayClick,
                    onIgnoreClick = onIgnoreClick,
                    onRestoreTrackClick = onRestoreTrackClick,
                    currentTrackId = currentTrackId,
                )
            }
        }
    }
}