package by.niaprauski.settings

interface SettingsContract {

    fun setDarkMode(enabled: Boolean)

    fun getSettingsFlow()

    fun setVisuallyEnabled(enabled: Boolean)
}