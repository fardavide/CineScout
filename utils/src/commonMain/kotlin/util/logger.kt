package util

import co.touchlab.kermit.Logger

fun Logger.v(message: Any, tag: String, throwable: Throwable? = null) {
    v(message.toString(), tag, throwable)
}

fun Logger.d(message: Any, tag: String, throwable: Throwable? = null) {
    d(message.toString(), tag, throwable)
}

fun Logger.i(message: Any, tag: String, throwable: Throwable? = null) {
    i(message.toString(), tag, throwable)
}

fun Logger.w(message: Any, tag: String, throwable: Throwable? = null) {
    w(message.toString(), tag, throwable)
}

fun Logger.e(message: Any, tag: String, throwable: Throwable? = null) {
    e(message.toString(), tag, throwable)
}
