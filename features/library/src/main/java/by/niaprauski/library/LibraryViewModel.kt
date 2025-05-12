package by.niaprauski.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.niaprauski.domain.models.Track
import by.niaprauski.domain.usecases.track.GetTracksFlowUseCase
import by.niaprauski.domain.usecases.track.MarkAsIgnoreTrackUseCase
import by.niaprauski.library.mapper.TrackModelMapper
import by.niaprauski.library.models.LibraryEvent
import by.niaprauski.library.models.LibraryState
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
class LibraryViewModel @Inject constructor(
    private val getTracksFlowUseCase: GetTracksFlowUseCase,
    private val markAsIgnoreTrackUseCase: MarkAsIgnoreTrackUseCase,
    private val trackModelMapper: TrackModelMapper,
) : ViewModel(), LibraryContract {


    private val _state = MutableStateFlow(LibraryState.INITIAL)
    val state = _state.asStateFlow()

    private val _event by lazy { Channel<LibraryEvent>() }
    val event: Flow<LibraryEvent> by lazy { _event.receiveAsFlow() }


    fun onCreate() {
        getTracksFlow()

    }

    private fun getTracksFlow() {
        viewModelScope.launch {
            getTracksFlowUseCase.invoke()
                .onSuccess { tracks ->
                    tracks.collect { trackList ->
                        _state.update { it.copy(tracks = trackList) }

                    }
                }
        }
    }

    override fun ignoreTrack(track: Track) {

        viewModelScope.launch {
            markAsIgnoreTrackUseCase.invoke(track)
                .onSuccess {
                    val mediaItem = trackModelMapper.toMediaItem(track)
                    sendEvent(LibraryEvent.IgnoreMediaItem(mediaItem))

                }
        }
    }

    override fun playTrack(track: Track) {
        val mediaItem = trackModelMapper.toMediaItem(track)
        sendEvent(LibraryEvent.PlayMediaItem(mediaItem))
    }

    private fun sendEvent(event: LibraryEvent) {
        viewModelScope.launch {
            _event.send(event)
        }
    }
}