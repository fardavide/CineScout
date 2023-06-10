package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.id.DatabaseTmdbSeasonId

val TmdbSeasonIdAdapter = object : ColumnAdapter<DatabaseTmdbSeasonId, Long> {

    override fun decode(databaseValue: Long) = DatabaseTmdbSeasonId(value = databaseValue.toInt())

    override fun encode(value: DatabaseTmdbSeasonId) = value.value.toLong()
}
