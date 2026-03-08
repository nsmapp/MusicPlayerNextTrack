package by.niaprauski.designsystem.theme.dimens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import by.niaprauski.designsystem.theme.AppTheme

val defaultRoundedShape: RoundedCornerShape
    @Composable
    get() = RoundedCornerShape(AppTheme.radius.default)