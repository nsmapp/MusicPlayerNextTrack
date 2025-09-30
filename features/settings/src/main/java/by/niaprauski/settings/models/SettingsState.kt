package by.niaprauski.settings.models

data class SettingsState(
    val isNightMode: Boolean,
){
    companion object{
        val INITIAL = SettingsState(
            isNightMode = false,
        )
    }
}
