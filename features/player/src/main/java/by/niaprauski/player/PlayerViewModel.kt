package by.niaprauski.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import by.niaprauski.domain.usecases.track.GetTracksUseCase
import by.niaprauski.domain.usecases.track.SaveTrackUseCase
import by.niaprauski.player.contracts.PlayerContract
import by.niaprauski.player.mapper.TrackModelMapper
import by.niaprauski.player.models.PlayerEvent
import by.niaprauski.player.models.PlayerState
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
) : ViewModel(), PlayerContract {

    private val _state = MutableStateFlow<PlayerState>(PlayerState.INITIAL)
    val state = _state.asStateFlow()


    private val _event by lazy { Channel<PlayerEvent>() }
    val event: Flow<PlayerEvent> by lazy { _event.receiveAsFlow() }

    fun onCreate() {
        getTracks()
    }

    fun openLibrary() {
        viewModelScope.launch {
            _event.send(PlayerEvent.OpenLibrary)
        }
    }

    fun openSettings() {
        viewModelScope.launch {
            _event.send(PlayerEvent.OpenSettings)
        }
    }


    private fun getTracks() {
        viewModelScope.launch {
            getTracksUseCase.invoke()
                .map { tracks -> trackModelMapper.toMediaItems(tracks) }
                .onSuccess { items -> setPlayList(items) }
                .onFailure {
                    //TODO add get track failure information
                }
        }
    }

    fun syncTracks(tracks: List<ITrack>) {

        viewModelScope.launch {

            val syncTracks = tracks.map { track -> trackModelMapper.toDomainModel(track) }
            saveTrackUseCase.invoke(syncTracks)
                .onSuccess { handleSyncedTracks() }
                .onFailure {
                    //TODO add sync failure message
                }
        }
    }

    private fun handleSyncedTracks() {
        if (_state.value.trackCount == 0) getTracks()
    }

    override fun play() {
        viewModelScope.launch {
            _event.send(PlayerEvent.Play)
        }
    }

    override fun pause() {
        viewModelScope.launch {
            _event.send(PlayerEvent.Pause)
        }
    }

    override fun stop() {
        viewModelScope.launch {
            _event.send(PlayerEvent.Stop)
        }
    }

    override fun playNext() {
        viewModelScope.launch {
            _event.send(PlayerEvent.PlayNext)
        }

    }

    override fun setPlayList(mediaItems: List<MediaItem> ) {
        viewModelScope.launch {
            _state.update { it.copy(trackCount = mediaItems.size) }
            _event.send(PlayerEvent.SetPlayList(mediaItems))
        }
    }

}