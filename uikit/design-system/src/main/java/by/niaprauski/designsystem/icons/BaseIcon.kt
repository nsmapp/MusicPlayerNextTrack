package by.niaprauski.designsystem.icons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp

@Composable
internal fun BaseIcon(
    modifier: Modifier,
    imageVector: ImageVector,
    size: Dp,
    contentDescription: String?,
    colorFilter: ColorFilter? = null,

    ){
    Image(
        modifier = modifier.size(size),
        imageVector = imageVector,
        contentDescription = contentDescription,
        colorFilter = colorFilter
    )
}
