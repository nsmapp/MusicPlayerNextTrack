package by.niaprauski.player.views

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.ui.button.PlayerLiteButton
import by.niaprauski.designsystem.ui.text.TextBoldLarge
import by.niaprauski.designsystem.ui.text.TextMediumLarge
import by.niaprauski.translations.R

@Composable
fun TrackInfoView(
    trackId: Long,
    artist: String,
    title: String,
    favorite: Int,
    onChangeTrackFavorite: (trackId: Long) -> Unit,
) {

    val scale = remember { Animatable(1f) }
    val lastTrackId = remember { mutableLongStateOf(trackId) }

    LaunchedEffect(favorite, trackId) {
        if (favorite > 0 && lastTrackId.longValue == trackId) {
            scale.animateTo(
                targetValue = 1.4f,
                animationSpec =tween(durationMillis = 100)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec =tween(durationMillis = 100)
            )
        }

        lastTrackId.longValue = trackId
    }


    val favoriteIcon by remember(favorite) {
        derivedStateOf {
            if (favorite == 0) Icons.Default.FavoriteBorder
            else Icons.Default.Favorite
        }
    }

    val baseIconColor = AppTheme.appColors.accent
    val favoriteIconColor by remember(favorite) {
        derivedStateOf { calculateIconColor(favorite, baseIconColor) }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TextBoldLarge(
            modifier = Modifier.basicMarquee(),
            text = artist,
            maxLines = 1,
        )
        TextMediumLarge(
            modifier = Modifier
                .basicMarquee()
                .padding(top = AppTheme.padding.default),
            text = title,
            maxLines = 1,
        )

        PlayerLiteButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = AppTheme.padding.default)
                .size(AppTheme.viewSize.normal)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                }
                .clip(RoundedCornerShape(AppTheme.viewSize.normal)),
            imageVector = favoriteIcon,
            onClick = {
                onChangeTrackFavorite(trackId)
            },
            description = stringResource(R.string.feature_player_favorite),
            colorFilter = ColorFilter.tint(favoriteIconColor)
        )
    }
}

private fun calculateIconColor(
    favorite: Int,
    baseIconColor: Color
): Color = if (favorite == 0) baseIconColor.copy(alpha = 0.5f)
else {
    val alpha = when {
        favorite >= 50 -> 1f
        else -> 0.5f + favorite / 100f
    }
    baseIconColor.copy(alpha = alpha)
}