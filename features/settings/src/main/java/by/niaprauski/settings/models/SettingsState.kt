package by.niaprauski.settings.models

import androidx.compose.runtime.Stable
import by.niaprauski.utils.constants.TEXT_EMPTY

@Stable
data class SettingsState(
    val isDarkMode: Boolean,
    val isVisuallyEnabled: Boolean,
    val minDuration: String,
    val isMinDurationError: Boolean,
    val maxDuration: String,
    val isMaxDurationError: Boolean,
){
    companion object{
        val INITIAL = SettingsState(
            isDarkMode = false,
            isVisuallyEnabled = true,
            minDuration = TEXT_EMPTY,
            isMinDurationError = false,
            maxDuration = TEXT_EMPTY,
            isMaxDurationError = false,
        )
    }
}
