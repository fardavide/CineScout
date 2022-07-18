package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter

val RatingAdapter = object : ColumnAdapter<Double, Double> {

    override fun decode(databaseValue: Double) = databaseValue

    override fun encode(value: Double) = value
}
