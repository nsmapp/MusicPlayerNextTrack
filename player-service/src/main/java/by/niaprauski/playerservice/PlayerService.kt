package by.niaprauski.playerservice

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.view.KeyEvent
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaButtonReceiver
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.MediaStyleNotificationHelper
import by.niaprauski.playerservice.models.PlayerServiceAction
import by.niaprauski.playerservice.models.TrackProgress
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

    private val chanelId = "by.niaprauski.nexttreck.chanel"
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

        val notification = buildNotification()
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


    //TODO move to string resources
    private fun createNotificationChannel() {
        val name = "Next Track Channel"
        val descriptionText = "Next track music chanel"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(chanelId, name, importance)
        channel.description = descriptionText

        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @OptIn(UnstableApi::class)
    private fun buildNotification(): Notification {
        createNotificationChannel()

        val mediaMetadata = player?.currentMediaItem?.mediaMetadata

        val playIntent = Intent(this, MediaButtonReceiver::class.java).apply {
            setAction(Intent.ACTION_MEDIA_BUTTON)
            putExtra(
                Intent.EXTRA_KEY_EVENT, KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY)
            )
        }
        val playPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, playIntent, PendingIntent.FLAG_IMMUTABLE)

        val pauseIntent = Intent(this, MediaButtonReceiver::class.java).apply {
            setAction(Intent.ACTION_MEDIA_BUTTON)
            putExtra(
                Intent.EXTRA_KEY_EVENT, KeyEvent(
                    KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE
                )
            )
        }
        val pausePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 1, pauseIntent, PendingIntent.FLAG_IMMUTABLE)

        val nextIntent = Intent(this, MediaButtonReceiver::class.java).apply {
            setAction(Intent.ACTION_MEDIA_BUTTON)
            putExtra(
                Intent.EXTRA_KEY_EVENT, KeyEvent(
                    KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT
                )
            )
        }
        val nextPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 2, nextIntent, PendingIntent.FLAG_IMMUTABLE)

        val prevIntent = Intent(this, MediaButtonReceiver::class.java).apply {
            setAction(Intent.ACTION_MEDIA_BUTTON)
            putExtra(
                Intent.EXTRA_KEY_EVENT, KeyEvent(
                    KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS
                )
            )
        }
        val prevPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 3, prevIntent, PendingIntent.FLAG_IMMUTABLE)


        val builder =
            NotificationCompat.Builder(this, chanelId).setSmallIcon(R.drawable.ic_media_play)
                .setContentTitle(mediaMetadata?.title ?: "No track") //TODO move to string resources
                .setContentText(mediaMetadata?.artist ?: "No artist")//TODO move to string resources

        mediaSession?.let { builder.setStyle(MediaStyleNotificationHelper.MediaStyle(it)) }
        player?.let {
            builder.setPriority(NotificationCompat.PRIORITY_LOW).setOngoing(it.isPlaying)
        }


        builder.setShowWhen(false)
            .addAction(R.drawable.ic_media_previous, "Track back", prevPendingIntent).addAction(
                if (player?.isPlaying == true) R.drawable.ic_media_pause
                else R.drawable.ic_media_play,
                if (player?.isPlaying == true) "Pause" else "Play",
                if (player?.isPlaying == true) pausePendingIntent else playPendingIntent
            ).addAction(R.drawable.ic_media_next, "Next", nextPendingIntent)

        return builder.build()
    }

    private fun updateNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, buildNotification())
    }

    fun updateTrackInfo() {
        val mediaMetadata = player?.currentMediaItem?.mediaMetadata
        _currentTitle.value = mediaMetadata?.title?.toString() ?: TEXT_EMPTY
        _currentArtist.value = mediaMetadata?.artist?.toString() ?: TEXT_EMPTY
    }

    fun setPlayList(mediaItems: List<MediaItem>, startIndex: Int = 0) {
        this.mediaItems = mediaItems
        player?.apply {
            setMediaItems(mediaItems, startIndex, 0)
            prepare()
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
        val index = mediaItems.indexOf(item)

        if (index == -1) return

        player?.seekTo(/* mediaItemIndex = */ index,/* positionMs = */ 0)

        if (!isPlaying()){
            play()
        }
    }

    fun changeShuffleMode() {

        if (isPlaying().not()) return

        val shuffleMode = player?.shuffleModeEnabled?.not() ?: return
        player?.shuffleModeEnabled = shuffleMode
        _shuffle.update { shuffleMode }
    }

    fun changeRepeatMode() {

        if (isPlaying().not()) return

        val newRepeatMode = when(player?.repeatMode){
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL-> Player.REPEAT_MODE_OFF
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
        val index = mediaItems.indexOf(item)

        if (index == -1) return
        mediaItems = mediaItems.toMutableList().apply { remove(item) }
        player?.removeMediaItem(index)

    }

    private fun startProgressTracking() {
        progressTrackingJob?.cancel()


        progressTrackingJob = serviceScope.launch(Dispatchers.Main) {
            while (true) {
                if (player?.isPlaying == true) {
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