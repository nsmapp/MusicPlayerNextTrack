package by.niaprauski.library

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import by.niaprauski.domain.models.SearchTrackFilter
import by.niaprauski.domain.usecases.track.GetTracksPagedUseCase
import by.niaprauski.domain.usecases.track.MarkTrackAsIgnoredUseCase
import by.niaprauski.domain.usecases.track.UnmarkTrackAsIgnoredUseCase
import by.niaprauski.library.mapper.TrackModelMapper
import by.niaprauski.library.models.LAction
import by.niaprauski.library.models.LibraryEvent
import by.niaprauski.library.models.LibraryState
import by.niaprauski.library.models.TrackModel
import by.niaprauski.playerservice.PlayerService
import by.niaprauski.playerservice.PlayerServiceConnection
import by.niaprauski.playerservice.models.ExoPlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val application: Application,
    private val getTrackPagedUseCase: GetTracksPagedUseCase,
    private val markTrackAsIgnoredUseCase: MarkTrackAsIgnoredUseCase,
    private val unmarkTrackAsIgnoredUseCase: UnmarkTrackAsIgnoredUseCase,
    private val trackModelMapper: TrackModelMapper,
) : ViewModel() {

    private val serviceConnection = PlayerServiceConnection(application)
    val playerService: StateFlow<PlayerService?> = serviceConnection.service.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val exoPlayerState: StateFlow<ExoPlayerState> = playerService.flatMapLatest { service ->
        service?.state ?: flowOf(ExoPlayerState.DEFAULT)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ExoPlayerState.DEFAULT
    )

    private val _state = MutableStateFlow(LibraryState.INITIAL)
    val state = _state.asStateFlow()

    private val _event by lazy { Channel<LibraryEvent>() }
    val event: Flow<LibraryEvent> by lazy { _event.receiveAsFlow() }

    private val _searchFlow = MutableStateFlow<SearchTrackFilter>(SearchTrackFilter.DEFAULT)

    var pagingTracks: Flow<PagingData<TrackModel>> = _searchFlow
        .debounce(DEBOUNCE_SEARCH_INPUT)
        .flatMapLatest {
            getTrackPagedUseCase.invoke(it)
        }
        .mapLatest { pagingData ->
            pagingData.map { track ->
                trackModelMapper.toTrackModel(track)
            }
        }
        .cachedIn(viewModelScope)


    fun onCreate() {
        serviceConnection.bind()
    }

    fun onAction(action: LAction){
        when(action){
            is LAction.Play -> play(Unit)
            is LAction.Pause -> pause(Unit)
            is LAction.SearchTrack -> searchTrack(action.text)
            is LAction.PlayTrack -> playTrack(action.track)
            is LAction.IgnoreTrack -> ignoreTrack(action.track)
            is LAction.RestoreTrack -> onRestoreTrackClick(action.track)
        }
    }

    private fun sendEvent(event: LibraryEvent) {
        viewModelScope.launch {
            _event.send(event)
        }
    }

    //TODO refactor to trackId
    private fun ignoreTrack(track: TrackModel) {

        viewModelScope.launch {
            markTrackAsIgnoredUseCase.invoke(track.id)
                .onSuccess {
                    val mediaItem = trackModelMapper.toMediaItem(track)
                    sendEvent(LibraryEvent.IgnoreMediaItem(mediaItem))

                }
        }
    }

    //TODO refactor to trackId
    private fun onRestoreTrackClick(track: TrackModel) {
        viewModelScope.launch {
            unmarkTrackAsIgnoredUseCase.invoke(track.id)
                .onSuccess {
                    val mediaItem = trackModelMapper.toMediaItem(track)
                    sendEvent(LibraryEvent.AddMediaItem(mediaItem))
                }
        }
    }

    private fun playTrack(track: TrackModel) {
        val mediaItem = trackModelMapper.toMediaItem(track)
        sendEvent(LibraryEvent.PlayMediaItem(mediaItem))
    }

    private fun searchTrack(text: String) {
        _state.update { it.copy(searchText = text) }

        viewModelScope.launch {
            _searchFlow.update { it.copy(text = text) }
        }

    }

    private fun play(value: Unit) {
        viewModelScope.launch {
            sendEvent(LibraryEvent.Play)
        }
    }

    private fun pause(value: Unit) {
        viewModelScope.launch {
            sendEvent(LibraryEvent.Pause)
        }
    }

    override fun onCleared() {
        serviceConnection.unbind()
        super.onCleared()
    }

    companion object {

        private const val DEBOUNCE_SEARCH_INPUT = 250L
    }
}