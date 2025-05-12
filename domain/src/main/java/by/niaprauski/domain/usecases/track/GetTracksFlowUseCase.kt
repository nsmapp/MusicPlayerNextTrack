package by.niaprauski.domain.usecases.track

import by.niaprauski.domain.models.Track
import by.niaprauski.domain.repository.TrackRepository
import by.niaprauski.domain.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTracksFlowUseCase @Inject constructor(
    private val trackRepository: TrackRepository,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun invoke(): Result<Flow<List<Track>>> =
        withContext(dispatcherProvider.io) {
            runCatching {
                trackRepository.getAllAsFlow()
            }
        }
}