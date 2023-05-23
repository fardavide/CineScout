package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

val DurationAdapter = object : ColumnAdapter<Duration, Long> {
    override fun decode(databaseValue: Long) = databaseValue.minutes
    override fun encode(value: Duration) = value.inWholeMinutes
}
