package by.niaprauski.data.datastore.mapper

import by.niaprauski.data.datastore.AppSettingsEntity
import by.niaprauski.domain.models.AppSettings
import javax.inject.Inject

class SettingsMapper @Inject constructor() {

    fun toDomainModel(entity: AppSettingsEntity): AppSettings {
        return with(entity) {
            AppSettings(
                isShowWelcomeMessage = isWelcomeMessage,
                isDarkMode = isDarkMode,
                isVisuallyEnabled = isVisuallyEnabled,
                minDuration = minDuration,
                maxDuration = maxDuration
            )
        }
    }
}
