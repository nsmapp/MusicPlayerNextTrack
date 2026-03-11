package by.niaprauski.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.settings.models.SAction
import by.niaprauski.settings.models.SettingsState
import by.niaprauski.settings.view.PlayListSettingsView
import by.niaprauski.settings.view.SyncSettingView
import by.niaprauski.settings.view.UISettingsView


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.onLaunch()
    }

    val state by viewModel.state.collectAsStateWithLifecycle()


    SettingsScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun SettingsScreenContent(
    state: SettingsState,
    onAction: (SAction) -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = AppTheme.appColors.background)
            .statusBarsPadding()
            .padding(AppTheme.padding.default)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {

        UISettingsView(
            accentPosition = state.acentPositon,
            backgroundPosition = state.backgroundPosition,
            isVisuallyEnabled = state.isVisuallyEnabled,
            onAccentColorChanged = { hex, pos -> onAction(SAction.SetAccentColor(hex, pos)) },
            onBackgroundColorChanged = { hex, pos -> onAction(SAction.SetBackgroundColor(hex, pos)) },
            onVisuallyChanged = { enabled -> onAction(SAction.SetVisuallyEnabled(enabled)) }
        )

        SyncSettingView(
            minDuration = state.minDuration,
            maxDuration = state.maxDuration,
            isMinDurationError = state.isMinDurationError,
            isMaxDurationError = state.isMaxDurationError,
            onMinDurationChanged = { duration -> onAction(SAction.SetMinDuration(duration)) },
            onMaxDurationChanged = { duration -> onAction(SAction.SetMaxDuration(duration)) },
        )

        PlayListSettingsView(
            playlistLimitSize = state.playListLimitSize,
            isPlayListLimitError = state.isPlayListLimitError,
            onLimitTrackChanged = { count -> onAction(SAction.SetPlayListLimitSize(count)) }
        )
    }
}
