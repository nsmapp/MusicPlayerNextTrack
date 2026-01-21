package by.niaprauski.data.datastore

import androidx.datastore.core.DataStore
import by.niaprauski.domain.models.AppSettings
import by.niaprauski.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepoImpl @Inject constructor(val store: DataStore<AppSettingsEntity>) :
    SettingsRepository {

    override suspend fun get(): Flow<AppSettings> =
        store.data.map { settingsEntity ->
            AppSettings(
                settingsEntity.isWelcomeMessage,
                settingsEntity.isDarkMode,
                settingsEntity.isVisuallyEnabled
            )
        }

    override suspend fun save(settings: AppSettings) {
        store.updateData {
            AppSettingsEntity.newBuilder()
                .setIsWelcomeMessage(settings.isShowWelcomeMessage)
                .setIsDarkMode(settings.isDarkMode)
                .setIsVisuallyEnabled(settings.isVisuallyEnabled)
                .build()
        }
    }

    override suspend fun setNightMode(enabled: Boolean) {
        store.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setIsDarkMode(enabled)
                .build()
        }
    }

    override suspend fun setShowWelcomeMessage(isFirstLaunch: Boolean) {
        store.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setIsWelcomeMessage(isFirstLaunch)
                .build()
        }
    }

    override suspend fun setVisuallyEnabled(enabled: Boolean) {
        store.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setIsVisuallyEnabled(enabled)
                .build()
        }
    }

}
