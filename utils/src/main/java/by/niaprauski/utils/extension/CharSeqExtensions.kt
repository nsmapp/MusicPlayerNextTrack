package by.niaprauski.utils.extension

fun CharSequence?.orDefault(default: String): String =
    if (this.isNullOrEmpty()) default else this.toString()

inline fun CharSequence?.ifNullOrEmpty(defaultValue: () -> CharSequence?): CharSequence? {
    return if (this.isNullOrEmpty()) defaultValue() else this
}