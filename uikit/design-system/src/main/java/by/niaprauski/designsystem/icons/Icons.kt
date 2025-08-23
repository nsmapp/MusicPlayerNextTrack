package by.niaprauski.designsystem.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import by.niaprauski.designsystem.theme.AppTheme

@Composable
fun MicroIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    colorFilter: ColorFilter? = ColorFilter.tint(AppTheme.colors.accent),
){
    BaseIcon(
        modifier = modifier,
        imageVector = imageVector,
        size = AppTheme.viewSize.icon_micro,
        contentDescription = null,
        colorFilter = colorFilter,
    )
}

@Composable
fun SmallIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    colorFilter: ColorFilter? = ColorFilter.tint(AppTheme.colors.accent),
){
    BaseIcon(
        modifier = modifier,
        imageVector = imageVector,
        size = AppTheme.viewSize.icon_small,
        contentDescription = null,
        colorFilter = colorFilter,
    )
}

@Composable
fun NormalIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    colorFilter: ColorFilter? = ColorFilter.tint(AppTheme.colors.accent),
){
    BaseIcon(
        modifier = modifier,
        imageVector = imageVector,
        size = AppTheme.viewSize.icon_normal,
        contentDescription = null,
        colorFilter = colorFilter,
    )
}