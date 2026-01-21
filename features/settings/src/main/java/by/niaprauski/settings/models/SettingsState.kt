package by.niaprauski.settings.models

data class SettingsState(
    val isDarkMode: Boolean,
    val isVisuallyEnabled: Boolean,
){
    companion object{
        val INITIAL = SettingsState(
            isDarkMode = false,
            isVisuallyEnabled = true
        )
    }
}
