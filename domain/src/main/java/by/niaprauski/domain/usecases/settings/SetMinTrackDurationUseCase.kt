package by.niaprauski.domain.usecases.settings

import by.niaprauski.domain.repository.SettingsRepository
import by.niaprauski.domain.utils.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetMinTrackDurationUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend operator fun invoke(minDurationMin: Int): Result<Unit> =
        withContext(dispatcherProvider.io) {
            runCatching {
                val maxDuration = settingsRepository.get().maxDuration
                val duration = minDurationMin * 1000

                if (duration >= maxDuration) throw IllegalArgumentException("Min duration must be less than max duration")
                settingsRepository.setMinDuration(duration)
            }
        }
}