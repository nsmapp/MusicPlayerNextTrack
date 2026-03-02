package by.niaprauski.domain.usecases.track

import by.niaprauski.domain.repository.TrackRepository
import by.niaprauski.domain.utils.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UnmarkTrackAsIgnoredUseCase @Inject constructor(
    private val trackRepository: TrackRepository,
    private val dispatcherProvider: DispatcherProvider
)  {

    suspend operator fun invoke(trackId: Long): Result<Unit> = withContext(dispatcherProvider.io){
        runCatching {
            trackRepository.unmarkTrackAsIgnored(trackId)
        }
    }
}