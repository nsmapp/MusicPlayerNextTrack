package by.niaprauski.designsystem.theme.colors

import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Immutable
data class DayColors(
    override val background: Color = Color(0xff75b1a9),
    override val background_hard: Color = Color(0xFF589791),
    override val foreground: Color = Color(0xFF75b18b),
    override val foreground_light: Color = Color(0xFF7FBF96),
    override val text: Color = Color(0xFFE5E5E0),
    override val text_ligth: Color = Color(0x59F4F4EF),
    override val accent: Color = Color(0xFFE5E5E1),
    override val transparent: Color =  Color(0x00FFFFFF),
    override val warning: Color = Color(0xFFed5752),
): AppColors

@Immutable
data class NightColors(
    override val background: Color = Color(0xFF3B3D4C),
    override val background_hard: Color =  Color(0xFF2F2F3B),
    override val foreground: Color =  Color(0xff282a36),
    override val foreground_light: Color = Color(0xFF2D2F3A),
    override val text: Color = Color(0xFFE5E5E0),
    override val text_ligth: Color = Color(0x59F4F4EF),
    override val accent: Color = Color(0xFFE5E5E0),
    override val transparent: Color =  Color(0x00FFFFFF),
    override val warning: Color = Color(0xFFed5752),
): AppColors

interface AppColors{
    val background: Color
    val background_hard: Color
    val foreground: Color
    val foreground_light: Color
    val text: Color
    val text_ligth: Color
    val accent: Color
    val transparent: Color
    val warning: Color
}


@Stable
val dayColorScheme = lightColorScheme(
//    primary = defBackground,
//    primaryContainer = defBackground,
//    onSurface = defBackground,
//    background = defBackground
)

