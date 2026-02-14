package by.niaprauski.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.niaprauski.designsystem.theme.AppTheme
import by.niaprauski.designsystem.ui.row.SwitchRow
import by.niaprauski.designsystem.ui.row.TextFieldRow
import by.niaprauski.designsystem.ui.slider.RainbowSlider
import by.niaprauski.designsystem.ui.text.TextBoldLarge
import by.niaprauski.designsystem.ui.text.TextMedium
import by.niaprauski.settings.models.SettingsState
import by.niaprauski.translations.R


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
        onVisuallyChanged = viewModel::setVisuallyEnabled,
        onMinDurationChanged = viewModel::setMinDuration,
        onMaxDurationChanged = viewModel::setMaxDuration,
        onAccentColorChanged = viewModel::setAccentColorSettings,
        onBackgroundColorChanged = viewModel::setBackgroundColorSettings,
    )
}

@Composable
private fun SettingsScreenContent(
    state: SettingsState,
    onVisuallyChanged: (Boolean) -> Unit,
    onMinDurationChanged: (String) -> Unit,
    onMaxDurationChanged: (String) -> Unit,
    onAccentColorChanged: (String, Float) -> Unit,
    onBackgroundColorChanged: (String, Float) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = AppTheme.appColors.background)
            .navigationBarsPadding()
            .statusBarsPadding()
            .padding(AppTheme.padding.default)
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
    ) {

        TextBoldLarge(text = stringResource(R.string.feature_settings_interface))

        TextMedium(
            modifier = Modifier.padding(top = AppTheme.padding.default),
            text = stringResource(R.string.feature_settings_text_color)
        )

        RainbowSlider(
            trackProgress = state.acentPositon,
            valueRange = 0f..1f,
            steps = 0,
            onAccentColorChanged = onAccentColorChanged
        )

        TextMedium(
            modifier = Modifier.padding(top = AppTheme.padding.default),
            text = stringResource(R.string.feature_settings_background_color)
        )

        RainbowSlider(
            trackProgress = state.backgroundPosition,
            valueRange = 0f..1f,
            steps = 0,
            onAccentColorChanged = onBackgroundColorChanged
        )

        SwitchRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(AppTheme.padding.mini),
            isChecked = state.isVisuallyEnabled,
            label = stringResource(R.string.feature_settings_visually),
            onCheckedChange = onVisuallyChanged,
        )

        TextBoldLarge(
            modifier = Modifier.padding(top = AppTheme.padding.default),
            text = stringResource(R.string.feature_settings_sync_settings)
        )

        TextFieldRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(AppTheme.padding.mini),
            text = state.minDuration,
            label = stringResource(R.string.feature_settings_min_duration_sec),
            onValueChange = onMinDurationChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = state.isMinDurationError,
        )

        TextFieldRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(AppTheme.padding.mini),
            text = state.maxDuration,
            label = stringResource(R.string.feature_settings_max_duration_min),
            onValueChange = onMaxDurationChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = state.isMaxDurationError,
        )
    }
}

