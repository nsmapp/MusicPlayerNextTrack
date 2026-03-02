package by.niaprauski.utils.extension

import by.niaprauski.utils.constants.TEXT_EMPTY

fun Int.toBoldUnicode(): String {
    val boldOffset = 0x1D7CE
    return this.toString().map { char ->
        if (char in '0'..'9') {
            Character.toChars(boldOffset + (char - '0')).joinToString(TEXT_EMPTY)
        } else {
            char.toString()
        }
    }.joinToString(TEXT_EMPTY)
}