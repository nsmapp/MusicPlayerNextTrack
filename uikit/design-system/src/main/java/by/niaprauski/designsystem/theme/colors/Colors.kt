package by.niaprauski.designsystem.theme.colors

import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

private val defBackground: Color = Color(0xff44475a)
private val defForegroundHard: Color = Color(0xFF1D1D25)
private val defForeground: Color = Color(0xff282a36)
private val defForegroundLight: Color = Color(0xFF2D2F3A)
private val defForegroundLightAlpha50: Color = Color(0x802D2F3A)
private val defText: Color = Color(0xfff8f8f2)
private val defTextLigth: Color = Color(0xC69B9B9A)
private val defAccent: Color = Color(0xFF07D05E)
private val defAccentLight: Color = Color(0x89205F2F)
private val defTransparent: Color = Color(0x00FFFFFF)
private val warningColor: Color = Color(0xFFC3BE75)

@Immutable
data class Colors(
    val background: Color = defBackground,
    val foreground_hard: Color = defForegroundHard,
    val foreground: Color = defForeground,
    val foreground_light: Color = defForegroundLight,
    val foregraund_light_a50: Color = defForegroundLightAlpha50,
    val text: Color = defText,
    val text_ligth: Color = defTextLigth,
    val accent: Color = defAccent,
    val accent_light: Color = defAccentLight,
    val transparent: Color = defTransparent,
    val warning: Color = warningColor,
)

@Stable
val dayColorScheme = darkColorScheme(
    primary = defBackground,
    primaryContainer = defForeground,
    onSurface = defBackground
)

