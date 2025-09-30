package by.niaprauski.domain.usecases.settings

import by.niaprauski.domain.repository.SettingsRepository
import by.niaprauski.domain.utils.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChangeNightModeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun invoke(enabled: Boolean): Result<Unit> =
        withContext(dispatcherProvider.io) {
            runCatching {
                settingsRepository.setNightMode(enabled)
            }
        }
}