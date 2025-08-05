package by.niaprauski.designsystem.theme.dimens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Immutable
data class Padding(
    val zero: Dp,
    val micro: Dp,
    val mini: Dp,
    val default: Dp,
    val normal: Dp,
    val medium: Dp,
    val large: Dp,
    val extraLarge: Dp,
)

val defaultPaddings = Padding(
    zero = 0.dp,
    micro = 2.dp,
    mini = 4.dp,
    default = 8.dp,
    normal = 16.dp,
    medium = 24.dp,
    large = 32.dp,
    extraLarge = 64.dp,
)