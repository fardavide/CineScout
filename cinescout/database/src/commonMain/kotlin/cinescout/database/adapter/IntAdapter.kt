package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter

val IntAdapter = object : ColumnAdapter<Int, Double> {

    override fun decode(databaseValue: Double) = databaseValue.toInt()

    override fun encode(value: Int) = value.toDouble()
}
