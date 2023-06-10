package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.id.DatabaseTraktSeasonId

val TraktSeasonIdAdapter = object : ColumnAdapter<DatabaseTraktSeasonId, Long> {

    override fun decode(databaseValue: Long) = DatabaseTraktSeasonId(value = databaseValue.toInt())

    override fun encode(value: DatabaseTraktSeasonId) = value.value.toLong()
}
