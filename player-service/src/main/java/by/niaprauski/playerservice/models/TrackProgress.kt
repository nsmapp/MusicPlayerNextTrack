package by.niaprauski.playerservice.models

import by.niaprauski.utils.constants.TEXT_EMPTY

data class TrackProgress(
    val progress: Float,
    val currentPosition: String,
){
    companion object{
        val DEFAULT = TrackProgress(
            progress = 0f,
            currentPosition = TEXT_EMPTY
        )
    }
}
