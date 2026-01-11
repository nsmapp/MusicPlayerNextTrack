package by.niaprauski.playerservice.utils

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.view.KeyEvent
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaButtonReceiver
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaStyleNotificationHelper
import by.niaprauski.utils.intents.OpenAppIntent

class NotificationCreator {

    private val chanelId = "by.niaprauski.nexttreck.chanel"

    @OptIn(UnstableApi::class)
    fun buildNotification(
        context: Context,
        player: ExoPlayer?,
        mediaSession: MediaSession?
    ): Notification {
        createNotificationChannel(context)

        val mediaMetadata = player?.currentMediaItem?.mediaMetadata

        val playPendingIntent: PendingIntent = createPendingIntent(
            context = context,
            code = KeyEvent.KEYCODE_MEDIA_PLAY,
            requestCode = 0
        )

        val pausePendingIntent: PendingIntent = createPendingIntent(
            context = context,
            code = KeyEvent.KEYCODE_MEDIA_PAUSE,
            requestCode = 1
        )

        val nextPendingIntent: PendingIntent = createPendingIntent(
            context = context,
            code = KeyEvent.KEYCODE_MEDIA_NEXT,
            requestCode = 2
        )

        val prevPendingIntent: PendingIntent = createPendingIntent(
            context = context,
            code = KeyEvent.KEYCODE_MEDIA_PREVIOUS,
            requestCode = 3
        )

        val builder =
            NotificationCompat.Builder(context, chanelId).setSmallIcon(R.drawable.ic_media_play)
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

        builder.setContentIntent(createOpenActivityIntent(context))
        
        return builder.build()
    }

    private fun createOpenActivityIntent(context: Context): PendingIntent {
        val openActivityIntent = Intent(OpenAppIntent.OPEN_APP_ACTION
        ).apply {
            `package` = context.packageName
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val openAppIntent = PendingIntent.getActivity(
            /* context = */ context,
            /* requestCode = */ 0,
            /* intent = */ openActivityIntent,
            /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return openAppIntent
    }

    @OptIn(UnstableApi::class)
    private fun createPendingIntent(
        context: Context,
        code: Int,
        requestCode: Int
    ): PendingIntent {
        val playIntent = Intent(context, MediaButtonReceiver::class.java)
            .apply {
                setAction(Intent.ACTION_MEDIA_BUTTON)
                putExtra(
                    Intent.EXTRA_KEY_EVENT,
                    KeyEvent(KeyEvent.ACTION_DOWN, code)
                )
            }
        val playPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(
                context,
                requestCode,
                playIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        return playPendingIntent
    }

    //TODO move to string resources
    private fun createNotificationChannel(context: Context) {
        val name = "Next Track Channel"
        val descriptionText = "Next track music chanel"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(chanelId, name, importance)
        channel.description = descriptionText

        val notificationManager: NotificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


}