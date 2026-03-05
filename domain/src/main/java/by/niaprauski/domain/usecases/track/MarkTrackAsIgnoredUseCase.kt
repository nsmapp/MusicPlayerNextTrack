package by.niaprauski.domain.usecases.track

import by.niaprauski.domain.repository.TrackRepository
import by.niaprauski.domain.utils.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MarkTrackAsIgnoredUseCase @Inject constructor(
    private val trackRepository: TrackRepository,
    private val dispatcherProvider: DispatcherProvider
)  {

    suspend operator fun invoke(trackId: String): Result<Unit> = withContext(dispatcherProvider.io){
        runCatching {
            trackRepository.markTrackAsIgnored(trackId)
        }
    }
}