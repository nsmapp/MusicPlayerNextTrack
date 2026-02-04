package by.niaprauski.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import by.niaprauski.domain.models.SearchTrackFilter
import by.niaprauski.domain.models.Track
import by.niaprauski.domain.usecases.track.GetTracksPagedUseCase
import by.niaprauski.domain.usecases.track.MarkTrackAsIgnoredUseCase
import by.niaprauski.domain.usecases.track.UnmarkTrackAsIgnoredUseCase
import by.niaprauski.library.mapper.TrackModelMapper
import by.niaprauski.library.models.LibraryEvent
import by.niaprauski.library.models.LibraryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val getTrackPagedUseCase: GetTracksPagedUseCase,
    private val markTrackAsIgnoredUseCase: MarkTrackAsIgnoredUseCase,
    private val unmarkTrackAsIgnoredUseCase: UnmarkTrackAsIgnoredUseCase,
    private val trackModelMapper: TrackModelMapper,
) : ViewModel(), LibraryContract {


    private val _state = MutableStateFlow(LibraryState.INITIAL)
    val state = _state.asStateFlow()

    private val _event by lazy { Channel<LibraryEvent>() }
    val event: Flow<LibraryEvent> by lazy { _event.receiveAsFlow() }

    private val _searchFlow = MutableStateFlow<SearchTrackFilter>(SearchTrackFilter.DEFAULT)

    var pagingTracks: Flow<PagingData<Track>> = _searchFlow
        .debounce(DEBOUNCE_SEARCH_INPUT)
        .flatMapLatest {
        getTrackPagedUseCase.invoke(it)
    }.cachedIn(viewModelScope)


    fun onCreate() {

    }

    override fun ignoreTrack(track: Track) {

        viewModelScope.launch {
            markTrackAsIgnoredUseCase.invoke(track)
                .onSuccess {
                    val mediaItem = trackModelMapper.toMediaItem(track)
                    sendEvent(LibraryEvent.IgnoreMediaItem(mediaItem))

                }
        }
    }


    override fun onRestoreTrackClick(track: Track) {
        viewModelScope.launch {
            unmarkTrackAsIgnoredUseCase.invoke(track)
                .onSuccess {
                    val mediaItem = trackModelMapper.toMediaItem(track)
                    sendEvent(LibraryEvent.AddMediaItem(mediaItem))
                }
        }
    }

    override fun playTrack(track: Track) {
        val mediaItem = trackModelMapper.toMediaItem(track)
        sendEvent(LibraryEvent.PlayMediaItem(mediaItem))
    }

    override fun searchTrack(text: String) {
        _state.update { it.copy(searchText = text) }

        viewModelScope.launch {
            _searchFlow.update{it.copy(text = text)}
        }

    }

    private fun sendEvent(event: LibraryEvent) {
        viewModelScope.launch {
            _event.send(event)
        }
    }

    companion object {

        private const val DEBOUNCE_SEARCH_INPUT = 250L
    }
}