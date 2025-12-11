package by.niaprauski.player

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import by.niaprauski.domain.usecases.settings.GetWelcomeMessageStatusUseCase
import by.niaprauski.domain.usecases.settings.SetWelcomeMessageStatusUseCase
import by.niaprauski.domain.usecases.track.GetTracksUseCase
import by.niaprauski.domain.usecases.track.SaveTrackUseCase
import by.niaprauski.player.contracts.PlayerContract
import by.niaprauski.player.mapper.TrackModelMapper
import by.niaprauski.player.models.PlayerEvent
import by.niaprauski.player.models.PlayerState
import by.niaprauski.playerservice.PlayerService
import by.niaprauski.playerservice.PlayerServiceConnection
import by.niaprauski.utils.models.ITrack
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val application: Application,
    private val saveTrackUseCase: SaveTrackUseCase,
    private val getTracksUseCase: GetTracksUseCase,
    private val getWelcomeMessageStatusUseCase: GetWelcomeMessageStatusUseCase,
    private val setWelcomeMessageStatusUseCase: SetWelcomeMessageStatusUseCase,
    private val trackModelMapper: TrackModelMapper,
) : ViewModel(), PlayerContract {

    private val _state = MutableStateFlow<PlayerState>(PlayerState.INITIAL)
    val state = _state.asStateFlow()

    private val _event by lazy { Channel<PlayerEvent>() }
    val event: Flow<PlayerEvent> by lazy { _event.receiveAsFlow() }

    private val serviceConnection = PlayerServiceConnection(application)
    val playerService: StateFlow<PlayerService?> = serviceConnection.service
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        serviceConnection.bind()
        startPlayerService(application)

        viewModelScope.launch {
            val service = playerService.filterNotNull().first()
            loadTracks()

        }
    }

    fun onCreate() {
        checkWelcomeDialogStatus()
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

    fun loadTracks() {
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
        if (_state.value.trackCount == 0) loadTracks()
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

    override fun playPrevious() {
        viewModelScope.launch {
            _event.send(PlayerEvent.PlayPrevious)
        }
    }

    override fun setPlayList(mediaItems: List<MediaItem>) {
        viewModelScope.launch {
            _state.update { it.copy(trackCount = mediaItems.size) }
            _event.send(PlayerEvent.SetPlayList(mediaItems))
        }
    }

    override fun changeShuffleMode() {
        viewModelScope.launch {
            _event.send(PlayerEvent.ChangeShuffleMode)
        }
    }

    override fun changeRepeatMode() {
        viewModelScope.launch {
            _event.send(PlayerEvent.ChangeRepeatMode)
        }
    }

    override fun playSingleTrack(uri: Uri) {
        viewModelScope.launch {
            _event.send(PlayerEvent.PlaySingleTrack(uri))
        }
    }
    override fun requestSync() {
        viewModelScope.launch {
            _event.send(PlayerEvent.SyncPlayList)
        }
    }

    override fun seekTo(position: Float) {
        viewModelScope.launch {
            _event.send(PlayerEvent.SeekTo(position))
        }
    }

    override fun showPermissionInformationDialog() {
        viewModelScope.launch {
            _state.update { it.copy(isShowPermissionInformationDialog = true) }
        }
    }

    override fun hideWelcomeDialogs() {
        viewModelScope.launch {
            setWelcomeMessageStatusUseCase.setFirstLaunchStatus(false)
            _state.update { it.copy(isShowWelcomeDialog = false) }
        }
    }

    override fun hideMediaPermissionInfoDialog() {
        viewModelScope.launch {
            _state.update { it.copy(isShowPermissionInformationDialog = false) }
        }
    }

    private fun checkWelcomeDialogStatus() {
        viewModelScope.launch {
            getWelcomeMessageStatusUseCase.invoke()
                .onSuccess { isShowWelcomeDialog ->
                    setWelcomeMessageStatusUseCase.setFirstLaunchStatus(false)
                    _state.update { it.copy(isShowWelcomeDialog = isShowWelcomeDialog ) }
                }
        }
    }

    override fun onCleared() {
        serviceConnection.unbind()
        super.onCleared()
    }

    private fun startPlayerService(context: Context) {
        val intent = Intent(context, PlayerService::class.java)
        context.startService(intent)
    }


}