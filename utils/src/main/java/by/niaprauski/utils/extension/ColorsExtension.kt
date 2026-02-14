package by.niaprauski.utils.extension

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt

fun Color.toHexString(): String = String.format("#%08X", this.toArgb())


fun String.toColor(): Color =
    try {
        Color(this.toColorInt())
    }catch (e: Exception){
        Log.e("!!! Error", e.message.toString())
        Color.Red
    }

fun Color.darken(fraction: Float = 0.2f): Color {
    return lerp(this, Color.Black, fraction.coerceIn(0f, 1f))
}


fun Color.lighten(fraction: Float = 0.2f): Color {
    return lerp(this, Color.White, fraction.coerceIn(0f, 1f))
}