package by.niaprauski.utils.models

interface ITrack {
    val id: String
    val fileName: String
    val artist: String
    val isRadio: Boolean
    val duration: Long
}