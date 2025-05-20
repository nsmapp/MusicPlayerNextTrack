package by.niaprauski.utils.extension

fun Long.toTrackTime(): String  {
    val min = this / 60
    val sec = this % 60

    return "$min:$sec"
}