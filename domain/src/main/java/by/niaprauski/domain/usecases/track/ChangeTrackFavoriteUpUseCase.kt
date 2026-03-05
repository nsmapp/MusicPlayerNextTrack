package by.niaprauski.domain.usecases.track

import by.niaprauski.domain.repository.TrackRepository
import by.niaprauski.domain.utils.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChangeTrackFavoriteUpUseCase @Inject constructor(
    private val trackRepository: TrackRepository,
    private val dispatcherProvider: DispatcherProvider
)  {

    suspend fun invoke(trackId: String): Result<Unit> = withContext(dispatcherProvider.io){
        runCatching {
            val trackId = trackRepository.getTrackById(trackId) ?: return@runCatching
            val newFavoriteValue = if (trackId.favorite > 0) 1 else 0
            trackRepository.upTrackFavorite(trackId.id, newFavoriteValue)
        }
    }
}