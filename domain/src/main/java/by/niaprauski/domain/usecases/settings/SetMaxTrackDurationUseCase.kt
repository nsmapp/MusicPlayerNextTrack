package by.niaprauski.domain.usecases.settings

import by.niaprauski.domain.repository.SettingsRepository
import by.niaprauski.domain.utils.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetMaxTrackDurationUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend operator fun invoke(maxDurationMin: Int): Result<Unit> =
        withContext(dispatcherProvider.io) {
            runCatching {
                val minDuration = settingsRepository.get().minDuration
                val duration = maxDurationMin * 60000

                if (duration <= minDuration) throw IllegalArgumentException("Max duration must be greater than min duration")
                settingsRepository.setMaxDuration(duration)
            }
        }
}