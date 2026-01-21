package by.niaprauski.domain.repository

import by.niaprauski.domain.models.AppSettings
import kotlinx.coroutines.flow.Flow


interface SettingsRepository {

    suspend fun get(): Flow<AppSettings>

    suspend fun save(settings: AppSettings)

    suspend fun setNightMode(enabled: Boolean)

    suspend fun setShowWelcomeMessage(isFirstLaunch: Boolean)

    suspend fun setVisuallyEnabled(enabled: Boolean)

}