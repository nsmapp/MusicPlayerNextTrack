package by.niaprauski.navigation.screens.player

import by.niaprauski.navigation.Dest
import kotlinx.serialization.Serializable

@Serializable
sealed class PlayerDest: Dest {

    companion object{
        fun root(
            radioTrack: String? = null,
            singleAudioTrack: String? = null,
        ) = Player(
            radioTrack = radioTrack,
            singleAudioTrack = singleAudioTrack
        )
    }

    @Serializable
    data class Player(
        val radioTrack: String?,
        val singleAudioTrack: String?,
    ): PlayerDest()
}