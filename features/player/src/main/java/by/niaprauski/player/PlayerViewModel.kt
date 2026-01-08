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
import by.niaprauski.utils.handlers.MediaHandler
import by.niaprauski.utils.models.ITrack
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
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


@HiltViewModel(assistedFactory = PlayerViewModel.Factory::class)
class PlayerViewModel @AssistedInject constructor(
    @Assisted("radioTrack") val radioTrack: Uri? = null,
    @Assisted("singleAudioTrack") val singleAudioTrack: Uri? = null,
    private val application: Application,
    private val saveTrackUseCase: SaveTrackUseCase,
    private val getTracksUseCase: GetTracksUseCase,
    private val getWelcomeMessageStatusUseCase: GetWelcomeMessageStatusUseCase,
    private val setWelcomeMessageStatusUseCase: SetWelcomeMessageStatusUseCase,
    private val trackModelMapper: TrackModelMapper,
) : ViewModel(), PlayerContract {

    @dagger.assisted.AssistedFactory
    interface Factory {
        fun create(
            @Assisted("radioTrack") radioTrack: Uri?,
            @Assisted("singleAudioTrack") singleAudioTrack: Uri?,
        ): PlayerViewModel
    }

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
        playInitialTrack()
    }

    private fun playInitialTrack() {
        viewModelScope.launch {
            playerService.filterNotNull().first()
            when {
                radioTrack == null && singleAudioTrack == null -> loadTracks()
                radioTrack != null -> playRadioTrack(radioTrack)
                singleAudioTrack != null -> playSingleAudioTrack(singleAudioTrack)
            }
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
                .onSuccess { items -> setPlayList(trackModelMapper.toMediaItems(items)) }
                .onFailure {
                    //TODO add get track failure information
                }
        }
    }

    fun syncTracks(tracks: List<ITrack>) {

        viewModelScope.launch {

            val syncTracks = trackModelMapper.toDomainModels(tracks)
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

    override fun setPlayList(tracks: List<MediaItem>) {
        viewModelScope.launch {
            _state.update { it.copy(trackCount = tracks.size) }
            _event.send(PlayerEvent.SetPlayList(tracks))
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

    override fun playSingleAudioTrack(uri: Uri) {
        viewModelScope.launch {
            val mediaItem = MediaHandler.uriToMediaItem(uri)
            _event.send(PlayerEvent.PlaySingleTrack(mediaItem))
        }
    }

    override fun playRadioTrack(uri: Uri) {
        viewModelScope.launch {
            MediaHandler.radioUriToMediaItem(uri, application.contentResolver)
                ?.let { mediaItem ->
                    _event.send(PlayerEvent.PlaySingleTrack(mediaItem))
                }
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
                    _state.update { it.copy(isShowWelcomeDialog = isShowWelcomeDialog) }
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
