package cinescout.test.kotlin

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity

class FakeLogger private constructor(private val logWriter: FakeLogWriter) {

    fun lastLogEvent(): LogEvent? = logWriter.lastLogEvent()
    fun requireLastLogEvent(): LogEvent = requireNotNull(lastLogEvent()) { "No log event was written" }

    companion object {

        fun init(logWriter: FakeLogWriter = FakeLogWriter()) = FakeLogger(logWriter = logWriter)
            .also { Logger.setLogWriters(logWriter) }
    }
}

class FakeLogWriter : LogWriter() {

    private var events: List<LogEvent> = emptyList()

    fun lastLogEvent(): LogEvent? = events.lastOrNull()

    override fun log(
        severity: Severity,
        message: String,
        tag: String,
        throwable: Throwable?
    ) {
        events += LogEvent(severity, message, tag, throwable)
    }
}

data class LogEvent(
    val severity: Severity,
    val message: String,
    val tag: String,
    val throwable: Throwable?
)
