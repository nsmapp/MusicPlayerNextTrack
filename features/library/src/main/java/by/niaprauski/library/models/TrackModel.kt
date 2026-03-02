package by.niaprauski.library.models

import androidx.compose.runtime.Immutable
import by.niaprauski.utils.extension.toBoldUnicode

@Immutable
data class TrackModel(
    val id: Long,
    val fileName: String,
    val pathOrUrl: String,
    val isIgnore: Boolean,
    val isRadio: Boolean,
    val duration: Long,
    val favorite: Int,
){

    val nameWithFavorite: String
        get() = if (favorite == 0) fileName else {
            "${favorite.toBoldUnicode()}♥\uFE0E $fileName"
        }

}