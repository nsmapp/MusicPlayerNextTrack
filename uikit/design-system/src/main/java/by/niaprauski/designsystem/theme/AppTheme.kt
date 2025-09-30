package by.niaprauski.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import by.niaprauski.designsystem.theme.colors.AppColors
import by.niaprauski.designsystem.theme.colors.DayColors
import by.niaprauski.designsystem.theme.colors.NightColors
import by.niaprauski.designsystem.theme.colors.dayColorScheme
import by.niaprauski.designsystem.theme.dimens.Padding
import by.niaprauski.designsystem.theme.dimens.Radius
import by.niaprauski.designsystem.theme.dimens.ViewSize
import by.niaprauski.designsystem.theme.dimens.defaultPaddings
import by.niaprauski.designsystem.theme.dimens.defaultRadius
import by.niaprauski.designsystem.theme.dimens.defaultViewSizes
import by.niaprauski.designsystem.theme.typography.OpenSansTypography
import by.niaprauski.designsystem.theme.typography.opensansTypography
import by.niaprauski.utils.constants.TEXT_EMPTY
import kotlinx.coroutines.CoroutineScope

@Composable
fun AppTheme(
    isDarkThemeEnabled: Boolean = true,
    snackBarHost: @Composable BoxScope.() -> Unit = {},
    content: @Composable () -> Unit,
) {

    val typography = opensansTypography
    val padding = defaultPaddings
    val radius = defaultRadius
    val viewSize = defaultViewSizes

    val dayColors = if (isDarkThemeEnabled) NightColors() else DayColors()
    val colorScheme = if (isDarkThemeEnabled) dayColorScheme else dayColorScheme

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()


    SystemAppearance(isDarkTheme = isDarkThemeEnabled)

    CompositionLocalProvider(
        LocalTypography provides typography,
        LocalPadding provides padding,
        LocalRadius provides radius,
        LocalViewSize provides viewSize,
        LocalAppColors provides dayColors,
        LocalColorScheme provides colorScheme,
        LocalSnackBarHostState provides snackBarHostState,
        LocalCoroutineScope provides coroutineScope
    ) {
        MaterialTheme(
            content = content,
            colorScheme = colorScheme
        )
    }

}

val LocalTypography = staticCompositionLocalOf<OpenSansTypography> { error(TEXT_EMPTY) }
val LocalPadding = staticCompositionLocalOf<Padding> { error(TEXT_EMPTY) }
val LocalRadius = staticCompositionLocalOf<Radius> { error(TEXT_EMPTY) }
val LocalViewSize = staticCompositionLocalOf<ViewSize> { error(TEXT_EMPTY) }
val LocalAppColors = staticCompositionLocalOf<AppColors> { error(TEXT_EMPTY) }
val LocalColorScheme = staticCompositionLocalOf<ColorScheme> { error(TEXT_EMPTY) }
val LocalSnackBarHostState = staticCompositionLocalOf<SnackbarHostState> { error(TEXT_EMPTY) }
val LocalCoroutineScope = staticCompositionLocalOf<CoroutineScope> { error(TEXT_EMPTY) }


@Stable
object AppTheme {

    val typography: OpenSansTypography
        @Composable get() = LocalTypography.current

    val padding: Padding
        @Composable get() = LocalPadding.current

    val radius: Radius
        @Composable get() = LocalRadius.current

    val viewSize: ViewSize
        @Composable get() = LocalViewSize.current

    val appColors: AppColors
        @Composable get() = LocalAppColors.current

}

@Composable
private fun SystemAppearance(isDarkTheme: Boolean) {
    val view = LocalView.current

    LaunchedEffect(isDarkTheme) {
        val window = (view.context as Activity).window

        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
    }
}