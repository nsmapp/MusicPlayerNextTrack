package by.niaprauski.utils.models

interface ITrack {
    val fileName: String

    val artist: String
    val pathOrUrl: String
    val isRadio: Boolean
}