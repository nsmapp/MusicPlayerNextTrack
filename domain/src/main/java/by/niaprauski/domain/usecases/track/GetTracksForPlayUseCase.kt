package by.niaprauski.domain.usecases.track

import by.niaprauski.domain.models.Track
import by.niaprauski.domain.repository.SettingsRepository
import by.niaprauski.domain.repository.TrackRepository
import by.niaprauski.domain.utils.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTracksForPlayUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val trackRepository: TrackRepository,
    private val dispatcherProvider: DispatcherProvider,
) {

    suspend fun invoke(): Result<List<Track>> =
        withContext(dispatcherProvider.io) {
            runCatching {
                val limit = settingsRepository.getTrackLimit()
                trackRepository.getRandom(limit)

            }
        }
}