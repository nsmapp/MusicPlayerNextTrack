package by.niaprauski.utils.extension

inline fun <T> Result<T>.onComplete(action: () -> Unit): Result<T> {
    action()
    return this
}