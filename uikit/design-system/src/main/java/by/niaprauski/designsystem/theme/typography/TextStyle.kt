package by.niaprauski.designsystem.theme.typography

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Immutable
data class OpenSansTypography(
    val bold_large: TextStyle,
    val bold: TextStyle,
    val bold_small: TextStyle,

    val medium_large: TextStyle,
    val medium: TextStyle,
    val medium_small: TextStyle,
    val medium_micro: TextStyle,

    val regular_large: TextStyle,
    val regular: TextStyle,
    val regular_small: TextStyle,
    val regular_nano: TextStyle,
)

val opensansTypography = OpenSansTypography(
    bold_large = TextStyle(fontFamily = openSansFamily, fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
    bold = TextStyle(fontFamily = openSansFamily, fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
    bold_small = TextStyle(fontFamily = openSansFamily, fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
    medium_large = TextStyle(fontFamily = openSansFamily, fontSize = 20.sp, fontWeight = FontWeight.Medium),
    medium = TextStyle(fontFamily = openSansFamily, fontSize = 16.sp, fontWeight = FontWeight.Medium),
    medium_small = TextStyle(fontFamily = openSansFamily, fontSize = 14.sp, fontWeight = FontWeight.Medium),
    medium_micro = TextStyle(fontFamily = openSansFamily, fontSize = 10.sp, fontWeight = FontWeight.Medium),
    regular_large = TextStyle(fontFamily = openSansFamily, fontSize = 20.sp, fontWeight = FontWeight.Normal),
    regular = TextStyle(fontFamily = openSansFamily, fontSize = 16.sp, fontWeight = FontWeight.Normal),
    regular_small = TextStyle(fontFamily = openSansFamily, fontSize = 14.sp, fontWeight = FontWeight.Normal),
    regular_nano = TextStyle(fontFamily = openSansFamily, fontSize = 12.sp, fontWeight = FontWeight.Normal),
)
