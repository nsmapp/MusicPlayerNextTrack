package by.niaprauski.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.niaprauski.domain.usecases.track.GetTracksUseCase
import by.niaprauski.domain.usecases.track.SaveTrackUseCase
import by.niaprauski.player.mapper.TrackModelMapper
import by.niaprauski.player.models.PlayerEvent
import by.niaprauski.player.models.TrackModel
import by.niaprauski.utils.models.ITrack
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val saveTrackUseCase: SaveTrackUseCase,
    private val getTracksUseCase: GetTracksUseCase,
    private val trackModelMapper: TrackModelMapper,
): ViewModel() {

    private val _tracks = MutableStateFlow<List<ITrack>>(listOf())
    val tracks = _tracks.asStateFlow()

    private val _event by lazy { Channel<PlayerEvent>() }
    val event: Flow<PlayerEvent> by lazy { _event.receiveAsFlow() }

    fun onCreate(){
        getTracks()
    }

    fun openLibrary(){
        viewModelScope.launch {
            _event.send(PlayerEvent.OpenLibrary)
        }
    }

    fun openSettings(){
        viewModelScope.launch {
            _event.send(PlayerEvent.OpenSettings)
        }
    }



    private fun getTracks(){
        viewModelScope.launch {
            getTracksUseCase.invoke()
                .onSuccess { tracks ->
                    val tracksList = tracks.map { track -> trackModelMapper.toModel(track) }
                    _tracks.update { tracksList }
                }
                .onFailure {
                    //TODO add get track failure information
                }
        }
    }

    fun syncTracks(tracks: List<ITrack>){

        viewModelScope.launch {

            val syncTracks = tracks.map { track -> trackModelMapper.toDomainModel(track) }
            saveTrackUseCase.invoke(syncTracks)
                .onSuccess { handleSyncedTracks(tracks) }
                .onFailure {
                    //TODO add sync failure message
                }
        }

        tracks.forEach {
            println("!!! --> ${it.title}")
        }
    }

    private fun handleSyncedTracks(tracks: List<ITrack>) {
        if (_tracks.value.isEmpty()) {
            val trackList: List<TrackModel> = tracks as List<TrackModel>
            _tracks.update { trackList }
        }
    }


}