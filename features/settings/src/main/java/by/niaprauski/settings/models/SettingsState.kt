package by.niaprauski.settings.models

import androidx.compose.runtime.Stable
import by.niaprauski.utils.constants.TEXT_EMPTY

@Stable
data class SettingsState(
    val isVisuallyEnabled: Boolean,
    val minDuration: String,
    val isMinDurationError: Boolean,
    val maxDuration: String,
    val isMaxDurationError: Boolean,
    val acentPositon: Float,
    val backgroundPosition: Float,
){
    companion object{
        val INITIAL = SettingsState(
            isVisuallyEnabled = true,
            minDuration = TEXT_EMPTY,
            isMinDurationError = false,
            maxDuration = TEXT_EMPTY,
            isMaxDurationError = false,
            acentPositon = 0f,
            backgroundPosition = 0f,
        )
    }
}
