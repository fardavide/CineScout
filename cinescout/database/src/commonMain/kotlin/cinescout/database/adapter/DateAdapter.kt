package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import korlibs.time.Date

val DateAdapter = object : ColumnAdapter<Date, Long> {
    override fun decode(databaseValue: Long) = Date(encoded = databaseValue.toInt())
    override fun encode(value: Date) = value.encoded.toLong()
}
