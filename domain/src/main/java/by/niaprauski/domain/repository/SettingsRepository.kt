package by.niaprauski.domain.repository


interface SettingsRepository {


    suspend fun isShowWelcomeMessage(): Boolean

    suspend fun setShowWelcomeMessage(isFirstLaunch: Boolean)

    suspend fun isDarkModeFlow(): Boolean

    suspend fun setDarkMode(isDarkMode: Boolean)
}