package by.niaprauski.library.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.ui.texxtfield.CTextField
import by.niaprauski.domain.models.Track
import by.niaprauski.library.models.LibraryState
import by.niaprauski.translations.R

@Composable
fun TrackListWithSearchView(
    listState: LazyListState,
    pagingTracks: LazyPagingItems<Track>,
    isSearchBarVisible: Boolean,
    state: LibraryState,
    onPlayClick: (Track) -> Unit,
    onIgnoreClick: (Track) -> Unit,
    onRestoreTrackClick: (Track) -> Unit,
    onSearchTrack: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = listState,
        ) {
            items(
                count = pagingTracks.itemCount,
                key = pagingTracks.itemKey { it.id },
            ) { index ->

                val item: Track? = pagingTracks[index]

                if (item != null) {
                    Column(
                        modifier = Modifier.animateItem(tween())
                    ) {
                        TrackItem(
                            track = item,
                            onPlayClick = onPlayClick,
                            onIgnoreClick = onIgnoreClick,
                            onRestoreTrackClick = onRestoreTrackClick,
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = AppTheme.padding.medium),
                            thickness = AppTheme.viewSize.border_small,
                            color = AppTheme.appColors.background_hard
                        )
                    }

                }
            }
        }

        //TODO to stickHeader?
        AnimatedContent(
            targetState = isSearchBarVisible,
            transitionSpec = { searchRowTransform() },
            content = { isVisible ->
                if (isVisible) {
                    CTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.searchText,
                        onValueChange = onSearchTrack,
                        hint = stringResource(R.string.feature_library_search),
                        leadingIcon = Icons.Outlined.Search,
                    )
                }
            })
    }
}


private fun searchRowTransform(): ContentTransform {
    val enter = slideInVertically(
        initialOffsetY = { -it },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        ),
    )
    val exit = slideOutVertically(
        targetOffsetY = { it },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        ),
    )

    return enter togetherWith exit
}