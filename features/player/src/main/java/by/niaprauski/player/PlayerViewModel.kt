package by.niaprauski.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.niaprauski.player.models.PlayerEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PlayerViewModel: ViewModel() {


    private val _event by lazy { Channel<PlayerEvent>() }
    val event: Flow<PlayerEvent> by lazy { _event.receiveAsFlow() }

    fun openLibrary(){
        viewModelScope.launch {
            _event.send(PlayerEvent.OpenLibrary)
        }
    }

    fun openSettings(){
        viewModelScope.launch {
            _event.send(PlayerEvent.OpenSettings)
        }
    }


}