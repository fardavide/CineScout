package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import com.soywiz.klock.DateTime

val DateTimeAdapter = object : ColumnAdapter<DateTime, Double> {
    override fun decode(databaseValue: Double) = DateTime(unixMillis = databaseValue)
    override fun encode(value: DateTime) = value.unixMillis
}
