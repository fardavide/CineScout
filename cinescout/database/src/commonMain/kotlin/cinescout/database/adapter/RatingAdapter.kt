package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter

val RatingAdapter = object : ColumnAdapter<Int, Long> {

    override fun decode(databaseValue: Long) = databaseValue.toInt()

    override fun encode(value: Int) = value.toLong()
}
