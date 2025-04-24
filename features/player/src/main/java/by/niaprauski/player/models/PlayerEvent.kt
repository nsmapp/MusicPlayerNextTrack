package by.niaprauski.player.models

sealed class PlayerEvent {

    object OpenSettings: PlayerEvent()
    object OpenLibrary: PlayerEvent()
    object Nothing: PlayerEvent()
}