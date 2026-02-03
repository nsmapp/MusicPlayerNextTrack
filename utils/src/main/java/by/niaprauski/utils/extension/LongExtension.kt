package by.niaprauski.utils.extension

fun Long.toTrackTime(): String  {
    val min = this / 60
    val sec = (this % 60).toString().padStart(2, '0')

    return "$min:$sec"
}