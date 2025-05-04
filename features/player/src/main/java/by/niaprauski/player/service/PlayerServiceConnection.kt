package by.niaprauski.player.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import kotlinx.coroutines.flow.MutableStateFlow

class PlayerServiceConnection(
    private val context: Context
) : ServiceConnection {

    val service = MutableStateFlow<PlayerService?>(null)
    var isBound = false

    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        (binder as? PlayerService.PlayerBinder)?.getService()?.let {
            service.value = it
        }
        isBound = true
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        service.value = null
        isBound = false
    }

    fun bind() {
        if (!isBound) {
            val intent = Intent(context, PlayerService::class.java)
            context.bindService(intent, this, Context.BIND_AUTO_CREATE)
        }
    }

    fun unbind() {
        if (isBound) {
            context.unbindService(this)
            isBound = false
            service.value = null
        }
    }
}