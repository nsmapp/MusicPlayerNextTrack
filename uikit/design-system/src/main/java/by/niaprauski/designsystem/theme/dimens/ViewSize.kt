package by.niaprauski.designsystem.theme.dimens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Immutable
data class ViewSize(
    val icon_micro: Dp,
    val icon_small: Dp,
    val icon_normal: Dp,
    val icon_big:Dp,
    val icon_large:Dp,
    val icon_extra_large:Dp,

    val border_zero: Dp,
    val border_small: Dp,
    val border_normal: Dp,
    val border_big: Dp,

    val view_extra_larger: Dp,
    val short_text_field: Dp,

    )

val defaultViewSizes = ViewSize(
    icon_micro = 12.dp,
    icon_small = 24.dp,
    icon_normal = 48.dp,
    icon_big = 64.dp,
    icon_large = 96.dp,
    icon_extra_large = 160.dp,

    border_zero = 0.dp,
    border_small = 0.75.dp,
    border_normal = 2.dp,
    border_big = 4.dp,

    view_extra_larger = 192.dp,
    short_text_field = 128.dp,
)