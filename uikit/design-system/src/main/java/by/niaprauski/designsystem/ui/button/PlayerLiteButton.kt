package by.niaprauski.designsystem.ui.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import by.niaprauski.designsystem.theme.AppTheme


@Composable
fun PlayerLiteButton(
    modifier: Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector,
    description: String,
    colorFilter: ColorFilter? = ColorFilter.tint(AppTheme.appColors.accent),
){
    Image(
        modifier = modifier.clickable{ onClick() },
        imageVector = imageVector,
        contentDescription = description,
        colorFilter = colorFilter,
    )

}


@Preview
@Composable
fun PlayerLiteButtonPreview(){



}