package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter

val IntDoubleAdapter = object : ColumnAdapter<Int, Double> {

    override fun decode(databaseValue: Double) = databaseValue.toInt()

    override fun encode(value: Int) = value.toDouble()
}

val IntLongAdapter = object : ColumnAdapter<Int, Long> {

    override fun decode(databaseValue: Long) = databaseValue.toInt()

    override fun encode(value: Int) = value.toLong()
}
