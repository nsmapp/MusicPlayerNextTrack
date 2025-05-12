package by.niaprauski.domain.models

data class Track(
    val id: Long,
    val title: String,
    val artist: String,
    val path: String,
    val isIgnore: Boolean,
)