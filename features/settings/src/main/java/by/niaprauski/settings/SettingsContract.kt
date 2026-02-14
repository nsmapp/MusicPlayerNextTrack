package by.niaprauski.settings

interface SettingsContract {

    fun getSettings()

    fun setVisuallyEnabled(enabled: Boolean)

    fun setMinDuration(duration: String)

    fun setMaxDuration(duration: String)

    fun setAccentColorSettings(hexColor: String, position: Float)

    fun setBackgroundColorSettings(hexColor: String, position: Float)

}