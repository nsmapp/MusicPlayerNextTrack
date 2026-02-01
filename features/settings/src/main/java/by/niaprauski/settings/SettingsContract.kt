package by.niaprauski.settings

interface SettingsContract {

    fun setDarkMode(enabled: Boolean)

    fun getSettings()

    fun setVisuallyEnabled(enabled: Boolean)

    fun setMinDuration(duration: String)

    fun setMaxDuration(duration: String)
}