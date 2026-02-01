package by.niaprauski.domain.models

data class Track(
    val id: Long,
    val fileName: String,
    val pathOrUrl: String,
    val isIgnore: Boolean,
    val isRadio: Boolean,
    val duration: Long,
)