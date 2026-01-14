package by.niaprauski.playerservice

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.audio.AudioSink
import androidx.media3.exoplayer.audio.DefaultAudioSink
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import by.niaprauski.playerservice.models.PlayerServiceAction
import by.niaprauski.playerservice.models.ExoPlayerState
import by.niaprauski.playerservice.models.TrackProgress
import by.niaprauski.playerservice.utils.NotificationCreator
import by.niaprauski.playerservice.utils.SoundProcessor
import by.niaprauski.playerservice.utils.getMediaItemIndex
import by.niaprauski.translations.R
import by.niaprauski.utils.extension.ifNullOrEmpty
import by.niaprauski.utils.extension.orDefault
import by.niaprauski.utils.extension.toTrackTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@UnstableApi
class PlayerService : MediaSessionService() {

    private var player: ExoPlayer? = null
    private var mediaSession: MediaSession? = null
    private var progressTrackingJob: Job? = null

    private val serviceScope by lazy { MainScope() }

    private val playerBinder = PlayerBinder()

    private val notificationId = 5465

    private val _state = MutableStateFlow(ExoPlayerState.DEFAULT)
    val state = _state.asStateFlow()

    private val _trackProgress = MutableStateFlow(TrackProgress.DEFAULT)
    val trackProgress = _trackProgress.asStateFlow()
    private val _waveform = MutableStateFlow(FloatArray(0))
    val waveform = _waveform.asStateFlow()

    private val soundProcessor = SoundProcessor(64, _waveform)


    inner class PlayerBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }

    override fun onCreate() {
        super.onCreate()

        player = ExoPlayer.Builder(this, createRenderFactory())
            .build().apply {
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC).build()
                addListener(playerListener)
                repeatMode = Player.REPEAT_MODE_ALL
                setAudioAttributes(audioAttributes, true)
            }
        player?.let { player ->
            mediaSession = MediaSession.Builder(this, player).build()
        }
    }

    private fun createRenderFactory(): DefaultRenderersFactory {

        val audioSink = DefaultAudioSink.Builder(this)
            .setAudioProcessors(arrayOf(soundProcessor))
            .build()

        return object : DefaultRenderersFactory(this) {
            override fun buildAudioSink(
                context: Context,
                enableFloatOutput: Boolean,
                enableAudioOutputPlaybackParams: Boolean
            ): AudioSink = audioSink
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    override fun onBind(intent: Intent?): IBinder {
        return playerBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            PlayerServiceAction.PLAY.name -> play()
            PlayerServiceAction.PAUSE.name -> pause()
            PlayerServiceAction.STOP.name -> stop()
            PlayerServiceAction.PREVIOUS_TRACK.name -> seekToPrevious()
            PlayerServiceAction.NEXT_TRACK.name -> seekToNext()

            else -> {
                //do nothing
            }

        }

        val notification = NotificationCreator().buildNotification(
            context = this,
            player = player,
            mediaSession = mediaSession,
        )
        startForeground(notificationId, notification)


        return START_STICKY
    }

    override fun onDestroy() {
        player?.removeListener(playerListener)
        player?.release()
        mediaSession?.release()
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun updateNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCreator().buildNotification(
            context = this,
            player = player,
            mediaSession = mediaSession,
        )

        notificationManager.notify(notificationId, notification)
    }

    fun setTracks(tracks: List<MediaItem>) {

        player?.apply {
            setMediaItems(tracks, 0, 0)
            prepare()
        }
    }

    fun setTrack(track: MediaItem) {
        stopWithClearMediaItems()
        updateTrackInfo(track.mediaMetadata)
        playSingleMediaItem(track)
    }

    private fun stopWithClearMediaItems() {
        player?.stop()
        player?.clearMediaItems()
    }

    private fun playSingleMediaItem(mediaItem: MediaItem) {
        val playList = listOf(mediaItem)
        player?.apply {
            setMediaItems(playList, 0, 0)
            prepare()
            play()
            startProgressTracking()
        }
    }

    fun play() {
        player?.prepare()
        player?.play()
        startProgressTracking()
    }

    fun pause() {
        player?.pause()
    }

    fun stop() {
        player?.stop()
    }

    fun seekToNext() {
        player?.seekToNextMediaItem()
    }

    fun seekToPrevious() {
        player?.seekToPreviousMediaItem()
    }

    fun seekTo(progress: Float) {
        if (player == null) return
        val position = (getDuration() * progress).toLong()
        player?.seekTo(position)
    }

    fun getCurrentPosition(): Long {
        return player?.currentPosition ?: 0
    }

    fun getDuration(): Long {
        return player?.duration ?: 0
    }

    fun isPlaying(): Boolean {
        return player?.isPlaying == true
    }

    fun playWithPosition(item: MediaItem) {

        val index = player?.getMediaItemIndex(item) ?: -1
        if (index != -1) {
            if (player?.currentMediaItemIndex == index) return
            player?.seekTo(index, 0)
        }

        if (!isPlaying()) play()
    }

    fun changeShuffleMode() {
        val shuffleMode = player?.shuffleModeEnabled?.not() ?: return
        player?.shuffleModeEnabled = shuffleMode
        _state.update { it.copy(shuffle = shuffleMode) }
    }

    fun changeRepeatMode() {
        val newRepeatMode = when (player?.repeatMode) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_OFF
            else -> Player.REPEAT_MODE_OFF
        }

        player?.let { player ->
            player.repeatMode = newRepeatMode
            _state.update { it.copy(repeatMode = newRepeatMode) }
        }
    }


    fun removeMediaItem(item: MediaItem) {
        val index = player?.getMediaItemIndex(item) ?: -1
        if (index == -1) return
        player?.removeMediaItem(index)
    }


    fun addItemToPlayList(item: MediaItem) {
        val index = player?.getMediaItemIndex(item) ?: -1
        if (index != -1) return
        player?.addMediaItem(item)
    }

    private fun startProgressTracking() {
        if (progressTrackingJob?.isActive == true) return

        progressTrackingJob = serviceScope.launch(Dispatchers.Main) {
            while (_state.value.isPlaying) {
                val currentPosition = getCurrentPosition()
                val duration = getDuration().toFloat()
                val trackTime = (currentPosition / 1000).toTrackTime()
                val progress = currentPosition / duration

                _trackProgress.update { TrackProgress(progress, trackTime) }
                delay(333)
            }
        }
    }

    private val playerListener = object : Player.Listener {
        override fun onPlayerErrorChanged(error: PlaybackException?) {
            when (error?.errorCode) {
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT,
                PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS,
                PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW,
                PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND,
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> {
                    if (player?.hasNextMediaItem() == true) {
                        player?.seekToNextMediaItem()
                        player?.prepare()
                        serviceScope.launch {
                            delay(500)
                            player?.play()
                        }
                    } else player?.stop()
                }
                //TODO handle other errors
                else -> println("!!! player error: ${error?.message}  code ${error?.errorCode}")
            }
        }

        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            updateTrackInfo(mediaMetadata)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE -> println("!!! player state: STATE_IDLE")
                Player.STATE_BUFFERING -> println("!!! player state: STATE_BUFFERING")
                Player.STATE_READY -> println("!!! player state: STATE_READY")
                Player.STATE_ENDED -> println("!!! player state: STATE_ENDED")
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            updateNotification()
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _state.update { it.copy(isPlaying = isPlaying) }
            updateNotification()
        }
    }

    private fun updateTrackInfo(mediaMetadata: MediaMetadata) {
        with(mediaMetadata) {
            val trackArtist = artist.ifNullOrEmpty {
                player?.currentMediaItem?.mediaMetadata?.displayTitle
            }
            val trackTitle = title.ifNullOrEmpty {
                player?.currentMediaItem?.mediaMetadata?.displayTitle
            }
            _state.update { it.copy(
                title = trackTitle.orDefault(getString(R.string.feature_player_no_track)),
                artist = trackArtist.orDefault(getString(R.string.feature_player_no_artist)),
            ) }
        }
    }
}