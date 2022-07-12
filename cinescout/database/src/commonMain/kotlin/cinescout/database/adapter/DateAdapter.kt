package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import com.soywiz.klock.Date

val DateAdapter = object : ColumnAdapter<Date, Long> {
    override fun decode(databaseValue: Long): Date = Date(encoded = databaseValue.toInt())
    override fun encode(value: Date): Long = value.encoded.toLong()
}
