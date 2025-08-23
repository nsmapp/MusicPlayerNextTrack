package by.niaprauski.designsystem.theme.colors

import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

private val defBackground: Color = Color(0xff75b1a9)
private val defBackgroundHard: Color = Color(0xFF589791)
private val defForeground: Color = Color(0xFF75b18b)
private val defForegroundLight: Color = Color(0xFF7FBF96)
private val defText: Color = Color(0xFFE5E5E0)
private val defTextDisabled: Color = Color(0x59F4F4EF)
private val defAccent: Color = Color(0xFFE5E5E0)
private val defTransparent: Color = Color(0x00FFFFFF)
private val warningColor: Color = Color(0xFFed5752)

@Immutable
data class Colors(
    val background: Color = defBackground,
    val background_hard: Color = defBackgroundHard,
    val foreground: Color = defForeground,
    val foreground_light: Color = defForegroundLight,
    val text: Color = defText,
    val text_ligth: Color = defTextDisabled,
    val accent: Color = defAccent,
    val transparent: Color = defTransparent,
    val warning: Color = warningColor,
)

@Stable
val dayColorScheme = lightColorScheme(
//    primary = defBackground,
//    primaryContainer = defBackground,
//    onSurface = defBackground,
//    background = defBackground
)

