package by.niaprauski.playerservice

import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import androidx.documentfile.provider.DocumentFile
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import by.niaprauski.playerservice.models.PlayerServiceAction
import by.niaprauski.playerservice.models.TrackProgress
import by.niaprauski.playerservice.utils.NotificationCreator
import by.niaprauski.utils.constants.TEXT_EMPTY
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

class PlayerService : MediaSessionService() {

    private var player: ExoPlayer? = null
    private var mediaSession: MediaSession? = null
    private var progressTrackingJob: Job? = null

    private val serviceScope by lazy { MainScope() }

    private val playerBinder = PlayerBinder()
    private var mediaItems: List<MediaItem> = emptyList()

    private val notificationId = 5465

    private val _currentTitle = MutableStateFlow(TEXT_EMPTY)
    val currentTitle = _currentTitle.asStateFlow()

    private val _currentArtist = MutableStateFlow(TEXT_EMPTY)
    val currentArtist = _currentArtist.asStateFlow()

    private val _trackProgress = MutableStateFlow(TrackProgress.DEFAULT)
    val trackProgress = _trackProgress.asStateFlow()

    private val _shuffle = MutableStateFlow(false)
    val shuffle = _shuffle.asStateFlow()

    private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_ALL)
    val repeatMode = _repeatMode.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()


    inner class PlayerBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build().apply {
            val audioAttributes = AudioAttributes.Builder().setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC).build()
            addListener(playerListener)
            repeatMode = Player.REPEAT_MODE_ALL
            setAudioAttributes(audioAttributes, true)
        }
        player?.let { player ->
            mediaSession = MediaSession.Builder(this, player).build()
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    override fun onBind(intent: Intent?): IBinder? {
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

    fun updateTrackInfo() {
        val mediaMetadata = player?.currentMediaItem?.mediaMetadata
        _currentTitle.value = mediaMetadata?.title?.toString() ?: TEXT_EMPTY
        _currentArtist.value = mediaMetadata?.artist?.toString() ?: TEXT_EMPTY
    }

    fun setPlayList(mediaItems: List<MediaItem>) {
        val playList = mediaItems
        this.mediaItems = playList
        player?.apply {
            setMediaItems(playList, 0, 0)
            prepare()
        }
    }

    fun setPlayList(uri: Uri) {
        val mediaItem = MediaItem.fromUri(uri)

        if (this.mediaItems.size == 1
            && this.mediaItems.firstOrNull()?.mediaId == mediaItem.mediaId
        ) return


        val playList = listOf(mediaItem)
        this.mediaItems = playList
        player?.clearMediaItems()
        player?.apply {
            setMediaItems(playList, 0, 0)
            prepare()
            play()
            startProgressTracking()
        }

        val documentFile = DocumentFile.fromSingleUri(this, uri)
        _currentArtist.value = documentFile?.name ?: TEXT_EMPTY
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

        val index = mediaItems.indexOfFirst { item.mediaId == it.mediaId }
        if (index == -1) player?.setMediaItem(item)
        else player?.seekTo(index, 0)

        if (!isPlaying()) play()
    }

    fun changeShuffleMode() {

        if (isPlaying().not()) return

        val shuffleMode = player?.shuffleModeEnabled?.not() ?: return
        player?.shuffleModeEnabled = shuffleMode
        _shuffle.update { shuffleMode }
    }

    fun changeRepeatMode() {

        if (isPlaying().not()) return

        val newRepeatMode = when (player?.repeatMode) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_OFF
            else -> Player.REPEAT_MODE_OFF
        }

        if (player?.repeatMode == Player.REPEAT_MODE_ALL) Player.REPEAT_MODE_ONE
        else Player.REPEAT_MODE_ALL

        player?.let { player ->
            player.repeatMode = newRepeatMode
            _repeatMode.update { newRepeatMode }
        }
    }


    fun removeMediaItem(item: MediaItem) {
        val index = mediaItems.indexOfFirst { item.mediaId == it.mediaId }
        if (index == -1) return

        mediaItems = mediaItems.toMutableList().apply { remove(item) }
        player?.removeMediaItem(index)
    }


    fun addItemToPlayList(item: MediaItem){
        if (mediaItems.indexOfFirst { item.mediaId == it.mediaId } != -1)  return

        this.mediaItems = this.mediaItems + listOf(item)
        player?.addMediaItem(item)
    }

    private fun startProgressTracking() {
        progressTrackingJob?.cancel()


        progressTrackingJob = serviceScope.launch(Dispatchers.Main) {
            while (true) {
                if (_isPlaying.value) {
                    val currentPosition = getCurrentPosition()
                    val duration = getDuration().toFloat()

                    val trackTime = (currentPosition / 1000).toTrackTime()
                    val progress = currentPosition / duration

                    _trackProgress.update { TrackProgress(progress, trackTime) }

                    delay(500)
                } else {
                    delay(1000)
                }
            }
        }
    }

    private val playerListener = object : Player.Listener {
        override fun onPlayerErrorChanged(error: PlaybackException?) {
            when (error?.errorCode) {
                PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND -> {
                    player?.seekToNextMediaItem()
                    player?.play()
                }
                //TODO handle other errors
                else -> println("!!! player error: ${error?.message}  code ${error?.errorCode}")
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            updateNotification()
            updateTrackInfo()
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _isPlaying.update { isPlaying }
            updateNotification()
        }
    }
}