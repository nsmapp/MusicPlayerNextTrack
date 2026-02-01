package by.niaprauski.data.datastore

import androidx.datastore.core.DataStore
import by.niaprauski.data.datastore.mapper.SettingsMapper
import by.niaprauski.domain.models.AppSettings
import by.niaprauski.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepoImpl @Inject constructor(
    val store: DataStore<AppSettingsEntity>,
    val mapper: SettingsMapper,
) : SettingsRepository {

    override suspend fun getFlow(): Flow<AppSettings> =
        store.data
            .map { entity ->
            mapper.toDomainModel(entity)
        }

    override suspend fun get(): AppSettings =
        store.data
            .map { entity -> mapper.toDomainModel(entity) }
            .first()


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

    override suspend fun setMinDuration(duration: Int) {
        store.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setMinDuration(duration)
                .build()
        }
    }

    override suspend fun setMaxDuration(duration: Int) {
        store.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setMaxDuration(duration)
                .build()
        }
    }

}
