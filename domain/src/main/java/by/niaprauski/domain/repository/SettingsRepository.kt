package by.niaprauski.domain.repository

import kotlinx.coroutines.flow.Flow


interface SettingsRepository {


    suspend fun isShowWelcomeMessage(): Boolean

    suspend fun setShowWelcomeMessage(isFirstLaunch: Boolean)

    suspend fun getDarkModeFlow(): Flow<Boolean>

    suspend fun setNightMode(isDarkMode: Boolean)
}